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

This document features MuleSoft´s Oracle EBS Financial connector and displays how to meet configuration requirements for using the Connector in a Mule project and how to incrementally build a "create-get-save-update" use case through the connector operations.

Prerequisites
=============

In order to build and run this project you'll need:  

* An Oracle Integration Repository instance.
* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).
* A browser to make a request to your Mule application.

Initial setup of the Mule project
=================================

### Step 1: Install the Oracle EBS connector from the update site

To begin building this application, start Mule Studio and  

1. Go to **Help \> Install New Software**. From the Work with dropdown select **MuleStudio Cloud Connectors Update Site.**  
2. Expand the Premium Option and look for the **Mule Oracle EBS Connector Mule Studio Extension.**  
3. Check its checkbox and click **Next**. Complete the installation process. After the connector is installed you'll be required to restart MuleStudio.  

### Step 2: Create a new Mule project

Once MuleStudio has launched, create a new project: 

1. Go to **File \> New \> Mule Project**  
2. In the New Mule Project configuration menu, provide a name for this project: **oracle-ebs-demo**  
3. Select **CloudHub Mule Runtime** from Runtime.
4. Click **Next** and provide a name for the flow: **oracle-ebs-demo.**  
5. Click **Finish**.  

### Step 3: Store the credentials

In src/main/app/mule-app.properties file that's on your project put the following key/value pairs and replace what's displayed **bold** with your credentials values.  

oracle.username=**&lt;username&gt;**  
oracle.password=**&lt;password&gt;**  
oracle.basePath=**&lt;basePath&gt;**  
oracle.responsibilityName=**&lt;responsibilityName&gt;**  
oracle.responsibilityApplName=**&lt;responsibilityApplName&gt;**  

### Step 4: Create a Oracle EBS Global element

1. Click on "Global Elements" tab.  
2. Click on "Create" to bring up Global Type dialog box.  
3. Filter by "OracleEBS".  
4. Select "OracleEBS" from "Cloud Connectors" section.  
5. Populate the fields with property placeholders.  
   ${oracle.username}
   ${oracle.password}
   ${oracle.basePath}
   ${oracle.responsibilityName}
   ${oracle.responsibilityApplName}
6. Click Ok.

![Connector credentials](images/image001.png)

Building the demo
=================

This demo will be incrementally creating a "create-get-save-update" type of application. Java and Property Transformers are used to put the necessary information on the Mule Message for subsequent operations to consume.

### Step 1: Building the "create" flow

1. Filter the Palette by "http" and drag and drop an **HTTP Inbound Endpoint** in the canvas.  
2. Filter the Palette by "oracleebs" and drag a **OracleEBS Cloud Connector** next to the Http Inbound Endpoint.  
3. Filter the Palette by "object to json" and drag a **Object to JSON Transformer** next to the OracleEBS Connector.  
4. Filter the Palette by "logger" and place a **Logger Component** next to the Object to JSON Transformer.  
5. Filter the Palette by "set payload" and place a **Set Payload Transformer** after the Logger.  
6. At last, filter the Palette by "flow" and drag a **Flow Scope** below the flow that's already in the canvas.  

Now let's setup the individual components:  

1. Double click the **empty flow**, once its properties dialog is displayed set "getEmail" as its Name.
2. Double click the **other flow**, once its properties dialog is displayed set "createEmail" as its Name.
3. Double click the **Http Inbound Endpoint**, in the properties dialog set the path as "create" and its display name as "/create".  
4. Double click on the **OracleEBS Cloud Connector** and configure it as follows:  
  a. Set "Create Email" as Display Name.  
  b. Select OracleEBS from the Config Reference dropdown.  
  c. Select "Create email" as Operation value.  
  d. In the Email Field Mappings section make sure that Define Attributes is selected and enter data in its input boxes.  
5. Open the **Logger Component** properties dialog by double clicking it.  
  a. Set "\#\#\# Create email operation payload \#[payload]" as Message.  
6. Open the **Set Payload Transformer** properties dialog by double clicking it.  
  a. Set "Demo Completed... Please check the logs for Payload." as Value.  

![Create email flow](images/image002.png)

### Step 2: Building the "get" flow  

1. Filter the Palette by "http" and drag and drop an **HTTP Inbound Endpoint** in the canvas into the "getEmail" flow created on Step 1.
2. Filter the Palette by "oracleebs" and drag a **OracleEBS Cloud Connector**.  
3. Filter the Palette by "object to json" and drag a **Object to JSON Transformer** next to the OracleEBS Connector.  
4. Filter the Palette by "logger" and place a **Logger Component** next to the Object to JSON Transformer.  
5. Filter the Palette by "set payload" and place a **Set Payload Transformer** after the Logger.  
6. At last, filter the Palette by "flow" and drag a **Flow Scope** below the "getEmail" Flow.

Now let's setup the individual components:

1. Double click the **empty flow**, once its properties dialog is displayed set "saveEmail" as its Name.
2. Double click the **Http Inbound Endpoint**, in the properties dialog set the path as "get" and its display name as "/get".  
3. Bring up the pattern properties of the **OracleEBS Cloud Connector** by double clicking it and configure it as follows:  
  a. Set "Get Email" as Display Name.  
  b. Select OracleEBS from the Config Reference dropdown.  
  c. Select "Get email" as Operation value.  
  d. Enter email id generated but running the create flow step in the id field.  
4. Open the **Logger Component** properties dialog by double clicking it.  
* Set "\#\#\# Get email operation payload \#[payload]" as Message.  
5. Open the **Set Payload Transformer** properties dialog by double clicking it.  
* Set "Demo Completed... Please check the logs for Payload." as Value.  

### Step 3: Building the "save" flow

1. Filter the Palette by "http" and drag and drop an **HTTP Inbound Endpoint** in the canvas into the "saveEmail" flow created on Step 2.
2. Filter the Palette by "oracleebs" and drag a **OracleEBS Cloud Connector**.  
3. Filter the Palette by "object to json" and drag a **Object to JSON Transformer** next to the OracleEBS Connector.  
4. Filter the Palette by "logger" and place a **Logger Component** next to the Object to JSON Transformer.
5. Filter the Palette by "set payload" and place a **Set Payload Transformer** after the Logger.
6. At last, filter the Palette by "flow" and drag a **Flow Scope** below the "saveEmail" Flow.

Now let's setup the individual components:

1. Double click the **empty flow**, once its properties dialog is displayed set "updateEmail" as its Name.
2. Double click the **Http Inbound Endpoint**, in the properties dialog set the path as "save" and its display name as "/save".  
3. Bring up the pattern properties of the **OracleEBS Cloud Connector** by double clicking it and configure it as follows:  
  a. Set "Save Email" as Display Name.  
  b. Select OracleEBS from the Config Reference dropdown.  
  c. Select "Save email" as Operation value.  
  d. In the Email Field Mappings section make sure that Define Attributes is selected.  
  e. If you want to update an existing record set emailId field along with other required fields or else just set other fields with create a new record if it doesn't exists.  
4. Open the **Logger Component** properties dialog by double clicking it.  
  a. Set "\#\#\# Save email operation payload \#[payload]" as Message.  
5. Open the **Set Payload Transformer** properties dialog by double clicking it.  
  a. Set "Demo Completed... Please check the logs for Payload." as Value.  

### Step 4: Building the "update" flow

1. Filter the Palette by "http" and drag and drop an **HTTP Inbound Endpoint** in the canvas into the "updateEmail" flow created on Step 3.
2. Filter the Palette by "oracleebs" and drag a **OracleEBS Cloud Connector**.
3. Filter the Palette by "object to json" and drag a **Object to JSON Transformer** next to the OracleEBS Connector.
4. Filter the Palette by "logger" and place a **Logger Component** next to the Object to JSON Transformer.
5. Filter the Palette by "set payload" and place a **Set Payload Transformer** after the Logger.

Now let's setup the individual components:

1. Double click the **Http Inbound Endpoint**, in the properties dialog set the path as "update" and its display name as "/update".  
2. Bring up the pattern properties of the **OracleEBS Cloud Connector** by double clicking it and configure it as follows:  
  a. Set "Update Email" as Display Name.  
  b. Select OracleEBS from the Config Reference dropdown.  
  c. Select "Update email" as Operation value.  
  d. In the Email Field Mappings section make sure that Define Attributes is selected.  
  e. To update an existing record set emailId field along with other fields that you want to update.  
3. Open the **Logger Component** properties dialog by double clicking it.  
  a. Set "\#\#\# Update email operation payload \#[payload]" as Message.  
4. Open the **Set Payload Transformer** properties dialog by double clicking it.  
  a. Set "Demo Completed... Please check the logs for Payload." as Value.  

Running the application
=======================

1. Right click on oracle-ebs-demo.mflow and select **Run As Mule Application**.
2. Check the console to see when the application starts.

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ++ Started app 'oracle-ebs-demo'                           ++

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

1. Hit the endpoint at<http://localhost:8081/get> and check the operation payload.

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    ++ Started app 'oracle-ebs-demo'                          ++

    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    INFO  2014-03-19 09:56:35,625 [[oracle-ebs-demo].connector.http.mule.default.receiver.02] org.mule.api.processor.LoggerMessageProcessor: ### Get Email -  {"actionType":null,"emailId":264913,"origSystem":null,"origSystemReference":null,"status":"A","parentObjectType":"PERSON","parentObjectId":401749,"primaryFlag":"N","contactPointPurpose":"BUSINESS","primaryByPurpose":"N","programUpdateDate":null,"createdByModule":"HZ_WS","createdByName":"HCCUSER","creationDate":1394746101000,"lastUpdateDate":1394747354000,"lastUpdatedByName":"HCCUSER","actualContentSource":"USER_ENTERED","emailFormat":"MAILHTML","emailAddress":"mule@localhost.com","commonObjId":null,"dynamicAttributeGroups":[],"contactPrefObjs":[],"origSysObjs":[{"actionType":null,"origSystemRefId":17857,"origSystem":"SST","origSystemReference":"EMAIL12345","objectType":"HZ_CONTACT_POINTS","objectId":264913,"status":"A","reasonCode":null,"oldOrigSystemReference":null,"startDateActive":1394746102000,"endDateActive":null,"programUpdateDate":null,"createdByModule":"HZ_WS","createdByName":"HCCUSER","creationDate":1394746102000,"lastUpdateDate":1394746102000,"lastUpdatedByName":"HCCUSER","dynamicAttributeGroups":[]}]}


As you can see the payload of each operations is printed to the console.

Resources
=========

Here's a list of features used in this demo with a link to their documentation

●     [Mule Expression Language](http://www.mulesoft.org/documentation/display/MULE3USER/Mule+Expression+Language)  
●     [Configuring Endpoints](http://www.mulesoft.org/documentation/display/MULE3USER/Configuring+Endpoints)  
●     [Studio transformers](http://www.mulesoft.org/documentation/display/MULE3STUDIO/Studio+Transformers)  
●     [Flow references](http://www.mulesoft.org/documentation/display/MULE3STUDIO/Flow+Ref+Component+Reference)
 

Webinars and additional documentation related to Mule ESB can be found under [Resources](http://www.mulesoft.com/resources) menu option.
