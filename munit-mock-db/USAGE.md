[Purpose](#purpose)  
[Procedure to Mock DB-endpoint](#Procedure-to-Mock-DB-endpoint)  
[Test the application](#test-the-application)  

Purpose
===========

This document illustrates the concept of **Mocking** and **Unit-Testing** of **Database-outbound-Endpoint** with a basic usecase. Mocking is achieved using MUnit Framework.

**Flow**: Starts with HTTP Inbound endpoint DB outbound endpoint which reads records some table and logger to log the results.
**Use-case**: Instead of reading records from the actual Database, **mock DB-endpoint** and do **unittesting**.

Unit Testing and Mocking using Test Case
================================================
	* Create a Junit Test case under src/test/java folder and name of the test class should end with Test.
	* Import the munit related packages into test class.
	* configure the getresourceconfiguration method by pointing to configuration file.
	* Create test method which is used to invoke the flow.Pass the flow name as input parameter
	* Create test methods which covers the possitve,negative and null payload use case.


Procedure to Mock DB-endpoint
============
syntax:
		whenEndpointWithAddress("jdbc://<queryKey>").thenReturn( muleMessageWithPayload( <method to return result-set> ) );
Example	
		whenEndpointWithAddress("jdbc://selectQ").thenReturn( muleMessageWithPayload( jdbcPayload() ) );


Test the application
==================
	* Right click on the Junit Test case and run as Junit Test case.
	* Open the Junit console and see the test results.

For more understanding can refer the attached 'munit-mock-db' sample code.
