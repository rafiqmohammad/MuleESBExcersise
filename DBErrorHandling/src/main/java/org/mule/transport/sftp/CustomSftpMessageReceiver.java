package org.mule.transport.sftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.zip.GZIPOutputStream;

import org.mule.api.MessagingException;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.execution.ExecutionCallback;
import org.mule.api.execution.ExecutionTemplate;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.construct.Flow;
import org.mule.processor.strategy.SynchronousProcessingStrategy;
import org.mule.transport.AbstractPollingMessageReceiver;
import org.mule.transport.sftp.SftpConnector;
import org.mule.transport.sftp.SftpInputStream;
import org.mule.transport.sftp.SftpReceiverRequesterUtil;
import org.mule.transport.sftp.notification.SftpNotifier;
import org.mule.util.IOUtils;
import org.mule.util.lock.LockFactory;
public class CustomSftpMessageReceiver extends AbstractPollingMessageReceiver implements Runnable {

	private SftpReceiverRequesterUtil sftpRRUtil = null;
    private LockFactory lockFactory;
    private boolean poolOnPrimaryInstanceOnly;

	public CustomSftpMessageReceiver(SftpConnector connector,
			FlowConstruct flowConstruct, 
			InboundEndpoint endpoint,
			long frequency)
			throws CreateException {
		super(connector, flowConstruct, endpoint);
		this.setFrequency(frequency);

        sftpRRUtil = new SftpReceiverRequesterUtil(endpoint);
	}

	@Override
    public void poll() throws Exception
    {
		if (logger.isDebugEnabled())
        {
            logger.debug("Polling. Called at endpoint " + endpoint.getEndpointURI());
        }
        try
        {
            String[] files = sftpRRUtil.getAvailableFiles(false);
            logger.info("No.of files existing for the current poll: "+files.length);
            if (files.length == 0)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Polling. No matching files found at endpoint " + endpoint.getEndpointURI());
                }
            }
            else
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Polling. " + files.length + " files found at " + endpoint.getEndpointURI() + ":" + Arrays.toString(files));
                }
                routeFile(files);                
                if (logger.isDebugEnabled())
                {
                    logger.debug("Polling. Routed all " + files.length + " files found at " + endpoint.getEndpointURI());
                }
            }
        }
        catch (MessagingException e)
        {
            //Already handled by TransactionTemplate
        }
        catch (Exception e)
        {
            logger.error("Error in poll", e);
            connector.getMuleContext().getExceptionListener().handleException(e);
            throw e;
        }
    }

	@Override
    protected void doInitialise() throws InitialisationException
    {
        this.lockFactory = getConnector().getMuleContext().getLockFactory();
        boolean synchronousProcessing = false;
        if (getFlowConstruct() instanceof Flow)
        {
            synchronousProcessing = ((Flow)getFlowConstruct()).getProcessingStrategy() instanceof SynchronousProcessingStrategy;
        }
        this.poolOnPrimaryInstanceOnly = Boolean.valueOf(System.getProperty("mule.transport.sftp.singlepollinstance","false")) || !synchronousProcessing;
    }

    @Override
    protected boolean pollOnPrimaryInstanceOnly()
    {
        return poolOnPrimaryInstanceOnly;
    }


    protected void routeFile(final String[] files) throws Exception
    {
        ExecutionTemplate<MuleEvent> executionTemplate = createExecutionTemplate();
        executionTemplate.execute(new ExecutionCallback<MuleEvent>()
        {
            @Override
            public MuleEvent process() throws Exception
            {
                // A bit tricky initialization of the notifier in this case since we don't
                // have access to the message yet...
                SftpNotifier notifier = new SftpNotifier((SftpConnector) connector, createNullMuleMessage(),
                        endpoint, flowConstruct.getName());
                //=========================	
                List<byte[]> cmprsdFilesCollection= new ArrayList<byte[]>();
                for (String file : files)
                {
                    if (getLifecycleState().isStopping())
                    {
                        break;
                    }
                    Lock fileLock = lockFactory.createLock(connector.getName() + file);
                    if (fileLock.tryLock(10, TimeUnit.MILLISECONDS))
                    {
                    	 if (logger.isDebugEnabled())
                         {
                             logger.debug("Processing file : "+file);
                         }
                    	try
                        {
                        	//retrieveFile contents as inputStream object.
                    		InputStream inputStream = sftpRRUtil.retrieveFile(file, notifier);
                    		//converts file-inputStream to ByteArray then compress contents and store in collection
                    		cmprsdFilesCollection.add(compressFileContentstoBA(IOUtils.toByteArray(inputStream)));
                        	
                        	if (logger.isDebugEnabled())
                            {
                                logger.debug("Routing file: " + file);
                            }
                        	
                        }
                        catch (Exception e)
                        {
                        	e.printStackTrace();
                        }
                    	finally{
                    		fileLock.unlock();
                    	}
                    }
                }
                 MuleMessage message = createMuleMessage(cmprsdFilesCollection);

                //message.setOutboundProperty(SftpConnector.PROPERTY_FILENAME, path);
                //message.setOutboundProperty(SftpConnector.PROPERTY_ORIGINAL_FILENAME, path);
                	
                // Now we have access to the message, update the notifier with the message
                notifier.setMessage(message);
                routeMessage(message);
                return null;
            }
        });
    }

    /**
     * SFTP-35
     */
    @Override 
    protected MuleMessage handleUnacceptedFilter(MuleMessage message) {
        logger.debug("the filter said no, now trying to close the payload stream");
        try {
            final SftpInputStream payload = (SftpInputStream) message.getPayload();
            payload.close();
        }
        catch (Exception e) {
            logger.debug("unable to close payload stream", e);
        }
        return super.handleUnacceptedFilter(message);
    }
    
    /**
     * this method is responsible to compress the byteArray
     * @param content
     * @return
     */
    public byte[] compressFileContentstoBA(byte[] content){
    	
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try{
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(content);
            gzipOutputStream.close();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public void doConnect() throws Exception
    {
        // no op
    }

    public void doDisconnect() throws Exception
    {
        // no op
    }

    protected void doDispose()
    {
        // no op
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
