package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.util.List;

public class MavenSensor implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(MavenSensor.class);

    private final MavenProject mavenProject;
    private final ResourcePerspectives resourcePerspectives;
    private final FileSystem fileSystem;

    public MavenSensor(ResourcePerspectives resourcePerspectives, FileSystem fileSystem) {
        this.mavenProject = PomUtils.createMavenProjectFromPom();
        this.resourcePerspectives = resourcePerspectives;
        this.fileSystem = fileSystem;
    }

    private void logAndRaiseIssue(InputFile pomFile, PomIssue pomIssue) {
        logger.info(pomIssue.message());
        Issuable issuable = resourcePerspectives.as(Issuable.class, pomFile);
        if (issuable != null) {
            issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of(ConnectorCertificationRulesDefinition.getPomRepositoryKey(), pomIssue.ruleKey()))
                    .message(pomIssue.message()).build());
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        final InputFile pomFile = Iterables.getOnlyElement(fileSystem.inputFiles(fileSystem.predicates().matchesPathPattern("pom.xml")));
        for (PomCheck pomCheck : buildPomAllChecks()) {
            final Iterable<PomIssue> analyse = pomCheck.analyze(mavenProject);
            for (PomIssue issue : analyse) {
                logAndRaiseIssue(pomFile, issue);
            }
        }
    }

    private Iterable<PomCheck> buildPomAllChecks() {
        List<PomCheck> scanners = Lists.newArrayList();
        scanners.add(new ScopeProvidedInMuleDependenciesCheck());
        scanners.add(new SnapshotDependenciesCheck());
        scanners.add(new SourceDeploymentForStandardCategoryCheck());
        scanners.add(new TestingFrameworkNotOverwrittenCheck());
        return scanners;
    }
}
