package com.wsl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
 
public class InterceptorFlowTestCase extends FunctionalTestCase
{
 
    @Test
    public void testDefaultJavaComponentShortcut() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        MuleMessage message = client.send("vm://in", "Hello World",null);
        assertNotNull(message);
        String payload = message.getPayloadAsString();
        assertNotNull(payload);
        //note that there is an exclamation mark on the end that was added by the interceptor
        assertEquals("Hello World!", payload);
    }
 
    @Override
    protected String getConfigResources()
    {
        return "ExampleOnMuleInterceptor.xml";
    }
}