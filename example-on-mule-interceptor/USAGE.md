[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Initial setup of the Mule project](#initial-setup-of-the-mule-project)  
* [Step 1: Install the Oracle EBS Financial connector from the update site](#step-1-install-the-oracle-ebs-financial-connector-from-the-update-site)  
* [Step 2: Create a new Mule project](#step-2-create-a-new-mule-project)  
* [Step 3: Store the credentials](#step-3-store-the-credentials)  
* [Step 4: Create a Oracle EBS Global element](#step-4-create-a-oracle-ebs-global-element)
[Building the demo](#building-the-demo)  
* [Step 1: Building the "create" flow](#step-1-building-the-create-flow)   
* [Step 2: Building the "get" flow](#step-2-building-the-get-flow)  
* [Step 3: Building the "save" flow](#step-3-building-the-save-flow)  
* [Step 4: Building the "update" flow](#step-4-building-the-update-flow)
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
=======

This document illustrates the concept of **Mule-interceptor** with a basic usecase.

Prerequisites
=============

In order to run this project you'll need:  

* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).


Building the demo
=================

Document follows with the explainationon how to create the flow as per the usecase.
### Step 1: Building the "create" flow

1. Filter the Palette by "vm" and drag and drop an **VM Inbound Endpoint** in the canvas.  
2.
3. 

Running the application
=======================

1. Right click on example-on-mule-interceptor.mflow and select **Run As Mule Application**.
2. Check the console to see when the application starts.

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ++ Started app 'example-on-mule-interceptor'                           ++

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

1. Hit the endpoint at<http://localhost:8081/get> and check the operation payload.

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ++ Started app 'example-on-mule-interceptor'              ++

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

As you can see the payload of each operations is printed to the console.

Resources
=========

Here's a list of features used in this demo with a link to their documentation

●     [Mule Expression Language](http://www.mulesoft.org/documentation/display/MULE3USER/Mule+Expression+Language)  
●     [Configuring Endpoints](http://www.mulesoft.org/documentation/display/MULE3USER/Configuring+Endpoints)  
●     [Studio transformers](http://www.mulesoft.org/documentation/display/MULE3STUDIO/Studio+Transformers)  
●     [Flow references](http://www.mulesoft.org/documentation/display/MULE3STUDIO/Flow+Ref+Component+Reference)
 

Webinars and additional documentation related to Mule ESB can be found under [Resources](http://www.mulesoft.com/resources) menu option.
