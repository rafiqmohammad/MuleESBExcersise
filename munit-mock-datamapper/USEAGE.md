[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Adding 'mappings' folder to Source Path](#Adding-'mappings'-folder-to-Source-Path)  
* [Step 1:](#step-1)
[Running the application](#running-the-application)  

Purpose
===========

Use-case: Transform payload to xml using Data-Mapper(mock).

This document illustrates the concept of **Mocking Data-Mapper** with a basic usecase. Here mocking is achieved using MUnit Framework.

Prerequisites
=============

In order to run this project you'll need:  

* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition) .
* MUnit setup.

Adding 'mappings' folder to Source Path
=================

Steps follows with the explaination on how to **mock Data Mapper**

### Step 1: Add Datamapper to Source Path
![Adding Mappings folder to Source path](images/image001.png)

Running the application
=======================

1. Navigate to src/test/java folder
2. Right click on MockDataMapperMunitTetCase_1.java and select **Run As JUnit Test**.
3. Switchover to JUnit wundow to see the output.
![Junit window](images/image002.png)

Webinars and additional documentation related to Mule ESB can be found under [Resources](http://www.mulesoft.com/resources) menu option.
