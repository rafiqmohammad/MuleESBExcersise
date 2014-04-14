package org.mule.transport.sftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.MuleEventContext;
import org.mule.api.transformer.TransformerException;

public class SFTPMessageReceiverDecompressor implements org.mule.api.lifecycle.Callable {

	  protected transient Log logger = LogFactory.getLog(getClass());
	/* 
	 * @see org.mule.api.lifecycle.Callable#onCall(org.mule.api.MuleEventContext)
	 * Returns a collection of Byte Array after decompression  
	 */
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		System.out.println("in onCall");
		List<byte[]> deCompressedContentsList= new ArrayList<byte[]>();
		ArrayList<byte[]> payload=(ArrayList<byte[]>)eventContext.getMessage().getPayload();
		for(int loopVar = 0; loopVar < payload.size(); loopVar++)
		{
			deCompressedContentsList.add(decompressContents(payload.get(loopVar)));
		}
		return (Object)deCompressedContentsList;
	}
	
	/**
	 * Takes input as byteArray and return a ByteArray by decompressing it. 
	 * @param contentBytes
	 * @return
	 */
	public byte[] decompressContents(byte[] contentBytes){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        try{
        	IOUtils.copy(new GZIPInputStream(new ByteArrayInputStream(contentBytes)), out);
        	
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return out.toByteArray();
	}



	
}
