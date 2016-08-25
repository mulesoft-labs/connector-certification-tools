package org.mule.tools.devkit.sonar.checks.git;

import org.mule.tools.devkit.sonar.exception.SonarCheckException;
import org.reflections.Reflections;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.resources.Project;
import org.sonar.check.Rule;

import java.util.stream.Collectors;

import static com.google.common.base.Predicates.assignableFrom;
import static java.lang.Boolean.TRUE;

public class GitSensor implements Sensor {
    private Iterable<Class<?>> checks = new Reflections("org.mule.tools.devkit.sonar.checks.git")
            .getTypesAnnotatedWith(Rule.class)
            .stream()
            .filter(assignableFrom(GitCheck.class)::apply)
            .collect(Collectors.toList());

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        try {
            FileSystem fileSystem = sensorContext.fileSystem();
            for (Class<?> check : checks) {
                for (InputFile inputFile : fileSystem.inputFiles(fileSystem.predicates().matchesPathPattern(".gitignore"))) {
                    GitCheck.class.cast(check.newInstance()).analyse(project, sensorContext, inputFile);
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SonarCheckException(e);
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return TRUE;
    }
}
