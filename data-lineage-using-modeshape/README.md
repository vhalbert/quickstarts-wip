The data-lineage-using-modeshape quickstart demonstrates using the metadata from Teiid VDB and/or model artifacts.


This will demonstrate the following:
-  an example for publishing a VDB artifact from the command line.
-  installing the metadata viewer workspace into the dashboard builder
-  using the metadata viewer to analyze the metadata sequenced from the published VDB


Prerequisites
--------------
The jboss server must have ModeShape installed.   See http://modeshape.jboss.org  to download an EAP dist kit.
Currently supporting only 3.8.x version.



System requirements
-------------------

-  If you have not done so, please review the System Requirements (../README.md)
-  Modeshape must be installed

####################
#   Server Setup
####################


1)  Start the server

	Open a command line and navigate to the "bin" directory under the root directory of the JBoss server.
	The run the below command, with optional configuration to use.

	For Linux:   ./standalone.sh [-c standalone-teiid.xml]	
	for Windows: standalone.bat [-c standalone-teiid.xml]


2)  ModeShape.vdb Deployment, there are several options:

NOTE: This artifact is already deployed in DV 6.1

	(1) Open the teiid-designer-project-set in Teiid Designer and deploy 
	(2) copy the following files to the $JBOSS_HOME/standalone/deployments directory
     	(a) teiid-designer-project-set/DV61_ModeShape/ModeShape.vdb
     	(b) teiid-designer-project-set/DV61_ModeShape/ModeShape.vdb.dodeploy
    (3) use the admin console (http://localhost:9990/console)
    (4) run jboss-cli with command:  deploy /path/to/ModeShape.vdb

You should see the server log indicate the VDB is active with a message like:  TEIID40003 VDB ModeShape.1 is set to ACTIVE


##################################
# Dashboard setup
##################################

assumes the server setup above has been done.

1) Log into the dashboard using the Showcase administration

	-  http://{hostname}:8080/dashboard


NOTE:  perform the following steps in their respective order.

2) Configure the connection to the ModeShape vdb

- create external connection: (custom datasource) using the following information:

name: TeiidReadingModeshape
url: jdbc:teiid:Modeshape;passThruAuthentication="true"
driver:  teiid
username:  teiidUser
password:  ?


3)  Import the metaviewer dashboard, which maps the KPI's to the external connection.

 	- dashboard/metaviewer_dashboard.kpiex


3)  Import the metaviewer workspace, which organizes the dashboard KPI's.

 	- dashboard/metaviewer_workspace.cex


The default role permission assigned to the metaviewer is "user". 

##################################
# Publish a VDB to be sequenced
##################################

On the command line, in this quickstart, run:

	mvn -s ../settings.xml clean install -Dpub

This will publish the Teiid artifacts in the publish_files directory.


##################################
# Viewing the Relational Model metadata and/or VDB metadata
##################################

Log out of the dashboard and back in using the teiidUser.


