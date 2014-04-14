[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
===========

This document illustrates the usecase built for exception handling for **DB Outbound Endpoint**



**Use-case**: 
Build an example app which is 

1. Get records from Database and put them in SFTP server.
2. Should be able to handle DB-outbound SQL Exceptions **Response time out**.
3. Should be able to handle DB-outbound SQL Exceptions **Unable to connect**.
4. Should be to  handle **IO Exception** in case of SFTP is not up/running and save the contents via File Connector in error folder.
5. In case of any exceptions then console them with following details using 'Exception Notifications Listener'.
  <br />Exception Notification with: Action name, Type, Class, Source.


Prerequisites
===============

In order to build and run this project you'll need:  

* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).
* An Oracle Database with user id 'WSLDB'.
* A Store Procedure 'SLEEP'
* User EXECUTE Permissions on the dbms_lock module
* A browser to make a request to your Mule application.



Test for 'Response time out' Exception
=======================

1. Change SQL Statement under JDBC Connector Query Key to 'callspSLEEP'.


Test for 'Unable to connect' Exception
=======================

1. Change SQL Statement under JDBC Connector Query Key to 'selectQ'.


Test for 'IO Exception' Exception
=======================

1. Change SQL Statement under JDBC Connector Query Key to 'selectQ'.
 
  
Running the application
=======================

1. Right click on DBErrorHandlingFlow.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'db-outbound-exception-handling'		       +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at<http://localhost:8081/testErrorHandling>.
	

Oracle Store Procedure	to delay the response.
=================================

1. First you’ll need to give your user EXECUTE permissions on the dbms_lock module:

C:\>sqlplus sys@clayoracle3 as sysdba
...
SQL> GRANT EXECUTE ON dbms_lock TO WSLDB;

2. writing a Store Procedure to sleep

--Functionality: 
	--Read for no. of seconds from input to which DB should execute sleep method.
	--and ends up in returing a OUT String as ''SLEEP for <no. of seconds> seconds'.

CREATE OR REPLACE PROCEDURE SLEEP(seconds IN VARCHAR2, returnStr OUT VARCHAR2)
IS 
BEGIN
  dbms_lock.sleep(TO_NUMBER(seconds));
  DBMS_OUTPUT.PUT_LINE('SLEEP for '|| seconds  || 'seconds ');
  returnStr := 'SLEEP for '||seconds || 'seconds' ;
  
END;


3. Execution of SP uing SQL Plus after it is created using command

DECLARE
    inParam1 INTEGER:='3';
    returnStr VARCHAR2(255);
BEGIN
  
  SLEEP(inParam1,returnStr );
  
END;

4. SP Output would be 

SLEEP for 3 seconds 

Statement processed.

3.93 seconds
	
Resources
===========

● [Using Interceptors] (http://www.mulesoft.org/documentation/display/current/Using+Interceptors)


