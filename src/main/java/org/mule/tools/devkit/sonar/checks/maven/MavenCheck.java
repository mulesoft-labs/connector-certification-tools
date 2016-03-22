package org.mule.tools.devkit.sonar.checks.maven;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

public interface MavenCheck {

    Iterable<ConnectorIssue> analyze(MavenProject mavenProject);

}
