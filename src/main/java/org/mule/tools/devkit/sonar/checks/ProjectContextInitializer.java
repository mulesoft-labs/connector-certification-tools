package org.mule.tools.devkit.sonar.checks;

import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Initializer;
import org.sonar.api.resources.Project;

public class ProjectContextInitializer extends Initializer {

    private static final Logger logger = LoggerFactory.getLogger(ProjectContextInitializer.class);

    private final MavenProject mavenProject;

    public ProjectContextInitializer(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void execute(Project project) {
        String category = mavenProject.getProperties().getProperty("category");
        logger.info("Found category in pom: " + category);
    }

}
