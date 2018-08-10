# jms-message-publisher-wls-gui
Message publisher for Oracle Weblogic server

A GUI tool for publishing message to JMS queue hosted in a Weblogic server.

## Dependecies
Following are dependencies for building the source:

* wlthint3client.jar (Oracle Weblogic Server)
* commons-lang (Apache Commons)

commons-lang is downloaded by Maven, however wlthint3client.jar is referenced via location installation of Oracle Weblogic server. Please make sure to update 'weblogic.home' in pom.xml file:

		<!-- Update with the location of Oracle Weblogic Server Home on your system-->
		<weblogic.home>/home/portal/Oracle/Middleware/Oracle_Home</weblogic.home>

## Build
Run

    mvn package

## Usage

Once build is successfully completed, jar file will be created in target folder. Change the file permission (if on Linux) to be executable and then run the jar file on JVM.

Enter following details on the GUI based on the needs:

* XML to be published - Location of file to be published
* Server - URL of Weblogic server
* JMS Factory - JNDI name of JMS Factory on Weblogic server
* JMS Queue - JNDI name of JMS Queue on Weblogic server
* Correlation ID - Correlation ID of the message
