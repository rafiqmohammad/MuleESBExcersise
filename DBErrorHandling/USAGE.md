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
2. Should be able to handle DB-outbound SQL Exceptions **Response time out** in case of delay in response. Cut off time 3 sec
3. Should be able to handle DB-outbound SQL Exceptions **Unable to connect** in case of DB not up/running.
4. Should be able to handle **IO Exceptions** in case of SFTP is not up/running and save the contents via File Connector in error folder.
5. In case of any exceptions,console them with following details using **Exception Notifications Listener**.
  <br />Exception Notification with: Action name, Type, Class, Source.


Prerequisites
===============

In order to build and run this project you'll need:  

* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).
* To download and install [Crushftp Server] (http://www.crushftp.com/download.html) and have a User 'crushuser2'. 
* To download and install [Oracle Database] (http://www.oracle.com/technetwork/database/enterprise-edition/downloads/index.html)  and create a User 'WSLDB'.
* User EXECUTE Permissions on the dbms_lock module.
* A Store Procedure 'SLEEP'.
* A browser to make a request to your Mule application. 

### Oracle Store Procedure SLEEP.

1. First you’ll need to give your user EXECUTE permissions on the dbms_lock module: <br />
C:\>sqlplus sys@clayoracle3 as sysdba <br />
... <br />
SQL> GRANT EXECUTE ON dbms_lock TO WSLDB; <br />

2. Writing a Store Procedure to sleep <br />
	Functionality:  <br />
	Read for no. of seconds from input to which DB should execute sleep method  <br />
	and ends up in returing a OUT String as 'SLEEP for \<no. of seconds\> seconds'.<br />

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

Running the application
=======================

1. Right click on DBErrorHandlingFlow.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'db-outbound-exception-handling'		       +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at<http://localhost:8081/testErrorHandling?delay=5>.
	


### Test for 'Unable to connect' Exception
1. Change SQL Statement under JDBC Connector Query Key to 'selectQ'.<br />
2. Pull down Oracle DB and hit a request via browser.<br />
**Output**:<br />
DBErrorHandlingFlow:::: in SQLException block :::Cannot get connection for URL jdbc:oracle:thin:@//localhost:1521/XE : Io exception: The Network Adapter could not establish the connection (java.sql.SQLException):::<br />
This line indicates that Exception 'UnableToConnect' raised !  And you are in flow DBErrorHandlingUnableToConnectFlow <br />

### Test for 'Response time out' Exception

1. Change SQL Statement under JDBC Connector Query Key to 'callspSLEEP'.<br />
**Note**:<br />
	1. Connector is set QuertTimeout for 3 Seconds.<br />
	2. In this case, one will not be able to push content save via SFTP/File connectors.<br />
**Output**:<br />
DBErrorHandlingFlow:::: in SQLException block :::ORA-01013: user requested cancel of current operation <br />
: This line indicates that Exception 'ResponseTimeOut' raised !  And you are in flow DBErrorHandlingResponseTimeOutFlow <br />

### Test for 'IO Exception' Exception
1. Pull down SFTP Server and hit a request via browser.<br />
2. Make usre DB is up and running.<br />
**Output**:<br />
in IOException block  ::::::::::::::::::Error during login to crushuser2@localhost: java.net.ConnectException: Connection refused: connect:::::<br />
This line indicates that SFTP IO Exception raised !  And you are in flow DBErrorHandlingFlow1<br />
Writing file to: F:\crushrootfolder\crushuser2\err\20142314160418<br />
	
Resources
===========

● [Oracle sproc for testing] (http://ourcraft.wordpress.com/2013/04/11/how-to-put-a-delay-in-an-oracle-sproc-for-testing) <br />
