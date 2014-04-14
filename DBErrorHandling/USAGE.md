[Purpose](#purpose)  
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
===========

This document illustrates the usecase built for exception handling for **DB Outbound Endpoint**



**Use-case**: 
Build an example app which is 

1) Get records from Database and put them in SFTP server.
2) Should be able to handle DB-outbound SQL Exceptions **Response time out**
3) Should be able to handle DB-outbound SQL Exceptions **Unable to connect**
4) Should be to  IO Exception in case of SFTP is not up/running and save the contents via File Connector in error folder.
5) In case of any exceptions then console them with following details using 'Exception Notifications Listener' 
  Exception Notification with: Action name, Type, Class, Source.


Running the application
=======================

1. Right click on DBErrorHandlingFlow.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'db-outbound-exception-handling'		       +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at<http://localhost:8081/testErrorHandling>.
	

Resources
===========

● [Using Interceptors] (http://www.mulesoft.org/documentation/display/current/Using+Interceptors)

● [What are Interceptors?] (http://ricston.com/blog/interceptors/)

● [Example on Interceptor Flow Test Case] (https://gist.github.com/cmordue/4552292)
	

