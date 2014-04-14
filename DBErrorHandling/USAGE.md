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
* A browser to make a request to your Mule application. 
* SFTP Server with a user id 'crushuser1' and up/running.
* An Oracle Database with user id 'WSLDB' and up/running.
* User EXECUTE Permissions on the dbms_lock module.
* A Store Procedure 'SLEEP'.

### Oracle Store Procedure	to delay the response.

1. First you’ll need to give your user EXECUTE permissions on the dbms_lock module: <br />
C:\>sqlplus sys@clayoracle3 as sysdba <br />
... <br />
SQL> GRANT EXECUTE ON dbms_lock TO WSLDB; <br />

2. writing a Store Procedure to sleep <br />
	Functionality:  <br />
	Read for no. of seconds from input to which DB should execute sleep method  <br />
	and ends up in returing a OUT String as 'SLEEP for \<no. of seconds\> seconds seconds'.<br />

	CREATE OR REPLACE PROCEDURE SLEEP(seconds IN VARCHAR2, returnStr OUT VARCHAR2) <br />
	IS <br />
	BEGIN <br />
	  dbms_lock.sleep(TO_NUMBER(seconds)); <br />
	  DBMS_OUTPUT.PUT_LINE('SLEEP for '|| seconds  || 'seconds '); <br />
	  returnStr := 'SLEEP for '||seconds || 'seconds' ; <br />
	END; <br />

3. Execution of SP uing SQL Plus after it is created. <br />
	DECLARE <br />
		inParam1 INTEGER:='3'; <br />
		returnStr VARCHAR2(255); <br />
	BEGIN <br />
		SLEEP(inParam1,returnStr ); <br />
	END; <br />
4. SP Output would be.  <br />
	
	SLEEP for 3 seconds  <br />
	Statement processed. <br />
	3.93 seconds <br />

### Test for 'Response time out' Exception
Change SQL Statement under JDBC Connector Query Key to 'callspSLEEP'.
Note: In this case one will not be able to push content save via SFTP/File connectors.

### Test for 'Unable to connect' Exception
Pull down Oracle DB and hit a request via browser.

### Test for 'IO Exception' Exception
1. Pull down SFTP Server and hit a request via browser.
2. Make usre DB is up and running.


Running the application
=======================

1. Right click on DBErrorHandlingFlow.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'db-outbound-exception-handling'		       +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at<http://localhost:8081/testErrorHandling?delay=5>.
	
	
Resources
===========

● [Using Interceptors] (http://www.mulesoft.org/documentation/display/current/Using+Interceptors). <br />
● [Oracle sproc for testing] (http://ourcraft.wordpress.com/2013/04/11/how-to-put-a-delay-in-an-oracle-sproc-for-testing) <br />
● [Crushftp Server] (http://www.crushftp.com/download.html)<br />