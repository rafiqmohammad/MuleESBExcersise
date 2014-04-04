package org.mule.transport.sftp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

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
import org.mule.transport.sftp.notification.SftpNotifier;
import org.mule.util.IOUtils;
import org.mule.util.lock.LockFactory;
public class CustomSftpMessageReceiver extends AbstractPollingMessageReceiver {

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
       System.out.println("in POLL Method");
		if (logger.isDebugEnabled())
        {
            logger.debug("Polling. Called at endpoint " + endpoint.getEndpointURI());
        }
        try
        {
            String[] files = sftpRRUtil.getAvailableFiles(false);
        	System.out.println("files.length: "+files.length);
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
                    logger.debug("Polling. " + files.length + " files found at " + endpoint.getEndpointURI()
                                 + ":" + Arrays.toString(files));
                }
                //Code was moved to this method.....................................
                routeFile(files);                
                if (logger.isDebugEnabled())
                {
                    logger.debug("Polling. Routed all " + files.length + " files found at "
                                 + endpoint.getEndpointURI());
                    
                    System.out.println("Polling. Routed all " + files.length + " files found at "
                                 + endpoint.getEndpointURI());
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
            System.out.println("Error in poll"+ e);
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
                List<byte[]> fileContentsinBA= new ArrayList<byte[]>();
                for (String file : files)
                {
                    if (getLifecycleState().isStopping())
                    {
                        break;
                    }
                    Lock fileLock = lockFactory.createLock(connector.getName() + file);
                    if (fileLock.tryLock(10, TimeUnit.MILLISECONDS))
                    {
                        System.out.println("Processing file : "+file);
                    	try
                        {
                        	InputStream inputStream = sftpRRUtil.retrieveFile(file, notifier);
                        	fileContentsinBA.add(IOUtils.toByteArray(inputStream));
                        	if (logger.isDebugEnabled())
                            {
                                logger.debug("Routing file: " + file);
                            }
                        	
                        }
                        catch (Exception e)
                        {
                            fileLock.unlock();
                        }
                    }
                }
                 MuleMessage message = createMuleMessage(fileContentsinBA);

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
        System.out.println("the filter said no, now trying to close the payload stream");
        
        try {
            final SftpInputStream payload = (SftpInputStream) message.getPayload();
            payload.close();
        }
        catch (Exception e) {
            logger.debug("unable to close payload stream", e);
        }
        return super.handleUnacceptedFilter(message);
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
}
