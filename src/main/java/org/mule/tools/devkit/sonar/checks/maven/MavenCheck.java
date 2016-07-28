package org.mule.tools.devkit.sonar.checks.maven;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.Check;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import static org.mule.tools.devkit.sonar.MvnLanguage.KEY;
import static org.mule.tools.devkit.sonar.checks.maven.MavenCheck.REPOSITORY;

@Check(language = KEY, repository = REPOSITORY)
public interface MavenCheck {
    String REPOSITORY = "connector-certification-mvn";

    Iterable<ConnectorIssue> analyze(MavenProject mavenProject);
}
