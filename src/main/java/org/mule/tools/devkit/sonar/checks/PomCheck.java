package org.mule.tools.devkit.sonar.checks;

import org.apache.maven.project.MavenProject;
import org.sonar.api.batch.SensorContext;

public interface PomCheck {

    Iterable<PomIssue> analyse(MavenProject mavenProject, SensorContext sensorContext);

}