package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.resources.Project;

@FunctionalInterface
public interface GitCheck {
    void analyse(Project project, SensorContext context, InputFile inputFile);
}
