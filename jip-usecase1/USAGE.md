[Purpose](#purpose)  
[Usecase](#usecase)  
[Prerequisites](#prerequisites)    
[Profiling using JIP](#profiling-using-jip)    
[Resources](#resources)  

Purpose
=======

This document illustrates how to profile mule application using JIP with sample usecase.

Usecase
========

●     Trigger flow via HTTPinbound-endpoint with no.of rows to be inserted. 

●     Read json file which should be the payload. 

●     Pause for 10seconds.

●     Then insert records to DB by parsing JSON fields using MEL.

●     Pause for 10 seconds. 

●     Convert JSON to XML.

●     Pause for 30 seconds.

●     Log payload at end of flow.



Prerequisites
=============

### To run the profiler, you need the following:

	● profile.jar.
	● a profile properties file (optional).


These files are loaded by the application classloader and should not be in the extensions loader path. The jar files need to be in the same directory. The properties file can be anywhere.
To use the profiler, you need to use the following JVM arguments:

	-javaagent:[DIR]\profile.jar -Dprofile.properties=[DIR]\profile.properties

Note:where [DIR] must be a fully qualified path is the directory that contains the profile.jar 


### To see the profiled report, you need the following:

	● jipViewer.jar
	● a profiled XML file



Profiling using JIP
=======================

**Step 1:**

1. Add java agent to VM <br />
 
	● Right click on the java class.<br />
	● Hover option ‘Run as'.<br />
	● Select ‘Run Configurations'. <br />

2. Select 'Arguments' tab of the respective Junit class and add java agent.<br />
	-javaagent:F:\JIP\jipProfiler\profile.jar -Dprofile.properties=F:\JIP\jipProfiler\profile.properties
	
3.	Hit Apply and Close button.<br />

**Step 2:**

● Right click on the java class.<br />
● Hover option 'Run as'.<br />
● Select 'Junit Test'.<br />
● A report should be generated as per profiler.properties file.<br />

JIP report Viewer
==================

**Step 1:**
1. Place jipViewer.jar and generated profile report in place and execute below command, GUI will be prompted. 

	Example:
		java -jar jipViewer.jar profiles\profile-JUNIT-U2-v1.xml

**Step 2: **
	Analyse the report as per the options available. 
	Under 'methods' tabs one can sort for the most expensive methods by a simple click on %net button.



Resources
===========
		  
● [Mule Aggregators](http://www.mulesoft.org/documentation/display/current/Routing+Message+Processors#RoutingMessageProcessors-RoutingMessageProcessors-All)	

