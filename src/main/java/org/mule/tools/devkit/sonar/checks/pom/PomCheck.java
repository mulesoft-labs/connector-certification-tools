package org.mule.tools.devkit.sonar.checks.pom;

import org.apache.maven.project.MavenProject;

public interface PomCheck {

    Iterable<PomIssue> analyze(MavenProject mavenProject);

}