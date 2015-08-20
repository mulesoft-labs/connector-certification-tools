# AnyPoint Connectors Certification Verifier

The objective of this project is to automate most of the current certification rules
http://mulesoft.github.io/connector-certification-docs/.

## Features

There are 4 types of different rules supported. New rules can be configured in the file src/main/resources/rules.json. For each rule, the following attributes must be declared:

* **type**: Rule id to be used. The following types are currently supported: *source.pom*, *source.xml*,*source.java*  and *structure*.
* **id**: Unique id assigned to this rule.
* **severity**: Criticity of the rule. It can be either *critical*, *major* , *minor* or *<info>*.
* **brief**: Short description of the objective of the rule. 
* **description**: Full description of the objective of the rule.
* **section**: Section within the certification documentation where this rule is declared.
* **accept**: If the expression is satisfied over the processed file, the *assert* expression will be executed. The type of *accept* expressions depends on the rule type. 
* **assert**: Assert expression to be verified over the file being processed. The type of expression depends on the rule type.

### Rule Type 'source.pom'

This rules can be configured with a XPath expression that needs to be satisfied. Both assert and accept expresion are XPath expressions. **accept** and **assert**  must be a valid XPath expressions.

```json
{
      "type": "source.pom",
      "id": "dist_management_other",
      "severity": "critical",
      "brief": "pom.xml distributionManagement must be properly configured.",
      "description": "Premium and Select connectors define 'http://repository-master.mulesoft.org/releases/' as repository.",
      "section": "3.3 Code Compliance.",
      "accept": "not(/pom:project/pom:properties/pom:category[text()='Premium' or text()='Select'])",
      "assert": "/pom:project/pom:distributionManagement/pom:repository/pom:id[text()='mulesoft-releases'] and /pom:project/pom:distributionManagement/pom:repository/pom:url[text()='http://repository-master.mulesoft.org/releases/']"
}
```
### Rule Type 'source.java'

This rule enables the inspections of source java classes. In this case, the *accept* expression can be a XPath expression of the file name and an annotation for the classes. The *assert* must be a class implementing the org.mule.tools.devkit.sonar.rule.sverifier.SourceTreeVerifier class or either org.mule.tools.devkit.sonar.rule.sverifier.ConnectorClassVerifier. 

```json
{
      "type": "source.java",
      "id": "processor_params",
      "severity": "major",
      "brief": "Mandatory parameters should be part of the payload.",
      "description": "It's strongly recommended that all mandatory parameters be defined as part of the payload. Consider creating a complex object as grouping all the parameters.",
      "section": "Best Practices",
      "accept": ".+/.+Connector.java$;org.mule.api.annotations.Connector",
      "assert": "org.mule.tools.devkit.sonar.rule.sverifier.ProcessorParametersVerifier"
}
```

### Rule Type 'structure'

This rule allows the existences of files within the project. Assert expressions could contains variables that can be used to express file patterns. Supported variables are 

* *connector_package*: Connector package using file separators insteaf of ''..
* *connector_processor*: Connectors processor name in camel case format.

```json
{
      "type": "structure",
      "id": "test_by_processor",
      "severity": "critical",
      "brief": "There must be an unit test class per processor",
      "description": "There must be an unit test class per processor",
      "section": "Directory Structure",
      "assert": "src/test/java/${connector_package}/automation/functional/${connector_processor}TestCases.java"
}
```

### Rule Type 'source.xml'

TBD

## Ignoring Inspections 

In some cases, connector could have false positives and you would like to ignore them. For this, a *.certignore* file can be added to the root of the connector to disable the inspection error. An example of this:

```java

processor_params_readonly:createBatchForQuery = The processor setPassword requires complex object due the complexity of the system.

```
Error UUID will be reported as part of the report.

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
* Create a Maven Mojo
* Extend support of expressions variables such as connector_name and category



