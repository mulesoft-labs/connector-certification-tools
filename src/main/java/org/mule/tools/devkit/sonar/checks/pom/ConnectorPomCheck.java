package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import java.util.List;

public class ConnectorPomCheck implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorPomCheck.class);

    protected final MavenProject mavenProject;
    protected final ResourcePerspectives resourcePerspectives;

    public ConnectorPomCheck(MavenProject mavenProject, ResourcePerspectives resourcePerspectives) {
        this.mavenProject = mavenProject;
        this.resourcePerspectives = resourcePerspectives;
    }

    private void logAndRaiseIssue(Project project, PomIssue pomIssue) {
        logger.info(pomIssue.message());
        Issuable issuable = resourcePerspectives.as(Issuable.class, (Resource) project);
        if (issuable != null) {
            issuable.addIssue(issuable.newIssueBuilder().ruleKey(pomIssue.ruleKey()).message(pomIssue.message()).build());
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        for (PomCheck pomCheck : buildPomAllChecks()) {
            final Iterable<PomIssue> analyse = pomCheck.analyze(mavenProject);
            for (PomIssue issue : analyse) {
                logAndRaiseIssue(project, issue);
            }
        }
    }

    private Iterable<PomCheck> buildPomAllChecks() {
        List<PomCheck> scanners = Lists.newArrayList();
        scanners.add(new ScopeProvidedInMuleDependenciesCheck());
        scanners.add(new TestingFrameworkNotOverwrittenCheck());
        return scanners;
    }
}
