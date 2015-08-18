# AnyPoint Connectors Certification Verifier

The objective of this project is to automate most of the current certification rules
http://mulesoft.github.io/connector-certification-docs/

## Features

There are 4 types of different rules supported. New rules can be declared in the file src/main/resources/rules.json.

### Rule Type 'source.pom'

This rules can be configured with a XPath expression that needs to be satisfied. Both assert and accept expresion are XPath expressions.

```json
{
      "type": "source.pom",
      "id": "dist_management_other",
      "severity": "critical",
      "brief": "pom.xml distributionManagement must be properly configured.",
      "description": "Premium and Select connectors define 'http://repository-master.mulesoft.org/releases/' as repository.",
      "section": "3.3 Code Compliance.",
      "acceptRegexp": "not(/pom:project/pom:properties/pom:category[text()='Premium' or text()='Select'])",
      "assert": "/pom:project/pom:distributionManagement/pom:repository/pom:id[text()='mulesoft-releases'] and /pom:project/pom:distributionManagement/pom:repository/pom:url[text()='http://repository-master.mulesoft.org/releases/']"
    }
```

### Rule Type 'source.xml

### Rule Type 'source.java'

### Rule Type 'structure'


## Usage

Execution could be started executing:

```
 analize.sh connector-module-path
```

## Pendings

* Support disabling errors per project. 
* Improve documentation
* Complete structure checks
* Integrate with Sonar 
* Create a Mojo project
* 


