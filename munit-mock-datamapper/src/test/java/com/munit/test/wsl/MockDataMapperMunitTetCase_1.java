package com.munit.test.wsl;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.munit.test.commons.MockDataMapperResponse;

/**
 * @author Mohammad rafiq
 *
 */
public class MockDataMapperMunitTetCase_1 extends FunctionalMunitSuite {
	
	/*
	 * @return xml file munitMockDataMapper for UnitTesting
	 */
	@Override
	protected String getConfigResources() {
		return "munitMockDataMapper.xml";
	}
	/**
	 * Test-case: assertNotNull of the flow 'munitTestDataMapperFlow'
	 **/
	@Test
	public void mockDMTest() throws Exception {
		
		//getXMLPayload  serve a xml response  when DM component is mocked
		String xmlPayload=MockDataMapperResponse.getXMLPayload();
		
		//mocing DM component
		whenMessageProcessor("transform").ofNamespace("data-mapper").thenReturn(muleMessageWithPayload(xmlPayload));
		
		MuleEvent response = runFlow("munitMockDataMapperFlow",testEvent("mock message"));
		//check for response payload NotNull
		assertNotNull(response.getMessageAsString());
	}
}
