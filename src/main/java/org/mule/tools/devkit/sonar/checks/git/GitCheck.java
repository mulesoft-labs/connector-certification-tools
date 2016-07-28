package org.mule.tools.devkit.sonar.checks.git;

import org.mule.tools.devkit.sonar.checks.Check;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.resources.Project;

import static org.mule.tools.devkit.sonar.checks.git.GitLanguage.KEY;

@FunctionalInterface
@Check(language = KEY, repository = "connector-certification-git")
public interface GitCheck {
    void analyse(Project project, SensorContext context, InputFile inputFile);
}
