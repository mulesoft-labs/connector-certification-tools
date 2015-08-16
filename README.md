# AnyPoint Connectors Certification Verifier

The objective of this project is to automate most of the current certification rules
http://mulesoft.github.io/connector-certification-docs/

## Features

There are 4 types of different rules supported:

* source.pom: 
* source.xml: 
* source.java:
* structure: 

Rules can be declared in the file src/main/resources/rules.json

## Usage

 mvn exec:java -Dexec.mainClass="org.mule.tools.devkit.sonar.Main" -Dexec.arguments="<connector-module-path>"



