package org.mule.tools.devkit.sonar.checks.structure;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;


public interface StructureCheck {

    Iterable<ConnectorIssue> analyze(MavenProject mavenProject);

}
