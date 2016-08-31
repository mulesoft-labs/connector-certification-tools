package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
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

import static org.mule.tools.devkit.sonar.utils.PomUtils.createMavenProjectFromPomFile;
import static org.mule.tools.devkit.sonar.utils.PomUtils.isDevKitConnector;

public class MavenSensor implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(MavenSensor.class);

    private final ResourcePerspectives resourcePerspectives;

    public MavenSensor(ResourcePerspectives resourcePerspectives) {
        this.resourcePerspectives = resourcePerspectives;
    }

    private void logAndRaiseIssue(InputFile pomFile, ConnectorIssue connectorIssue) {
        logger.info(connectorIssue.message());
        Issuable issuable = resourcePerspectives.as(Issuable.class, pomFile);
        if (issuable != null) {
            issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of(MavenCheck.REPOSITORY, connectorIssue.ruleKey()))
                    .message(connectorIssue.message()).build());
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        FileSystem fileSystem = sensorContext.fileSystem();
        MavenProject mavenProject = createMavenProjectFromPomFile(fileSystem.baseDir());
        if (isDevKitConnector(mavenProject)) {
            for (InputFile pomFile : fileSystem.inputFiles(fileSystem.predicates().matchesPathPattern("pom.xml"))) {
                for (MavenCheck mavenCheck : buildMavenChecks()) {
                    Iterable<ConnectorIssue> analyse = mavenCheck.analyze(mavenProject);
                    for (ConnectorIssue issue : analyse) {
                        logAndRaiseIssue(pomFile, issue);
                    }
                }
            }
        }
    }

    private Iterable<MavenCheck> buildMavenChecks() {
        List<MavenCheck> scanners = Lists.newArrayList();
        scanners.add(new DevKitLatestVersionCheck());
        scanners.add(new DistributionManagementByCategoryCheck());
        scanners.add(new ScopeProvidedInMuleDependenciesCheck());
        scanners.add(new SnapshotConnectorArtifactCheck());
        scanners.add(new SnapshotDependenciesCheck());
        scanners.add(new SourceDeploymentForStandardCategoryCheck());
        scanners.add(new TestingFrameworkNotOverwrittenCheck());
        return scanners;
    }
}
