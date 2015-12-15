package org.mule.tools.devkit.sonar.checks.structure;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.resources.Project;

public interface StructureCheck {

    Iterable<ConnectorIssue> analyze(MavenProject mavenProject, Project project);

}
