#!/bin/sh

set -euo pipefail
IFS=$'\n\t'

export MVN_HOME="${HOME}/.m2/repository"
export JAVA_HOME="$(/usr/libexec/java_home -v 1.8)"

mvn compile -DskipTests

java -Dfile.encoding=UTF-8 -cp "${JAVA_HOME}/lib/tools.jar:./target/classes:${MVN_HOME}/com/fasterxml/jackson/core/jackson-annotations/2.3.0/jackson-annotations-2.3.0.jar:${MVN_HOME}/com/fasterxml/jackson/core/jackson-databind/2.3.0/jackson-databind-2.3.0.jar:${MVN_HOME}/com/fasterxml/jackson/core/jackson-core/2.3.0/jackson-core-2.3.0.jar:${MVN_HOME}/org/checkerframework/checker-qual/1.9.3/checker-qual-1.9.3.jar:${MVN_HOME}/org/slf4j/slf4j-api/1.7.12/slf4j-api-1.7.12.jar:${MVN_HOME}/org/slf4j/slf4j-simple/1.7.7/slf4j-simple-1.7.7.jar:${MVN_HOME}/junit/junit/4.12/junit-4.12.jar:${MVN_HOME}/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:${MVN_HOME}/org/mockito/mockito-all/1.9.5/mockito-all-1.9.5.jar:${MVN_HOME}/org/apache/velocity/velocity/1.7/velocity-1.7.jar:${MVN_HOME}/commons-collections/commons-collections/3.2.1/commons-collections-3.2.1.jar:${MVN_HOME}/commons-lang/commons-lang/2.4/commons-lang-2.4.jar:${MVN_HOME}/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar:${MVN_HOME}/org/mule/tools/devkit/mule-devkit-annotations/3.7.0/mule-devkit-annotations-3.7.0.jar:${MVN_HOME}/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar" org.mule.tools.devkit.sonar.Main $1
