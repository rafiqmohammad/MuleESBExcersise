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

●     Trigger flow via HTTPinbound-endpoint with no.of rows to be inserted. <br />
●     Read json file which should be the payload. <br />
●     Pause for 10seconds.<br />
●     Then insert records to DB by parsing JSON fields using MEL.<br />
●     Pause for 10 seconds.<br /> 
●     Convert JSON to XML.<br />
●     Pause for 30 seconds.<br />
●     Log payload at end of flow.<br />



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

2. Select 'Arguments' tab of the respective Junit class and add java agent. <br />


	-javaagent:F:\JIP\jipProfiler\profile.jar -Dprofile.properties=F:\JIP\jipProfiler\profile.properties
	
	
3.	Hit Apply and Close button.<br />

**Step 2:**

1. Execute flow <br />
 
	● Right click on the java class. <br />
	● Hover option 'Run as'. <br />
	● Select 'Junit Test'. <br />
	● A profile report should be generated as per profiler.properties file. <br />

JIP report Viewer
==================

**Step 1:**

1. Place jipViewer.jar and generated profile report in place and execute below command, GUI will be prompted. <br />

	java -jar jipViewer.jar profiles\profile-JUNIT-U2-v1.xml
	
	
**Step 2:** <br />

Analyse the report as per the options available. <br />
Under 'methods' tabs one can sort for the most expensive methods by a simple click on %net button.<br />



Resources
===========
● [What is JIP]  (http://confluence.concord.org/display/CCTR/attachments/15911/1574.pdf) <br />
● [JIP Source]  (http://sourceforge.net/projects/jiprof/) <br />
	
	Note:
		profile.jar will be under directory named "profile".
		Zip file also contains complete documentation on JIP under directory named "doc".
● [JIP Viewer jar] (http://trac.assembla.com/groudsim/export/193/trunk/GroudSim/profile/jipViewer.jar)<br />		
