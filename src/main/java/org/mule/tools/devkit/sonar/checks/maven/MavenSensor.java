package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
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

    private final ResourcePerspectives resourcePerspectives;
    private final FileSystem fileSystem;

    public MavenSensor(ResourcePerspectives resourcePerspectives, FileSystem fileSystem) {
        this.resourcePerspectives = resourcePerspectives;
        this.fileSystem = fileSystem;
    }

    private void logAndRaiseIssue(InputFile pomFile, ConnectorIssue connectorIssue) {
        logger.info(connectorIssue.message());
        Issuable issuable = resourcePerspectives.as(Issuable.class, pomFile);
        if (issuable != null) {
            issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of(ConnectorCertificationRulesDefinition.getMvnRepositoryKey(), connectorIssue.ruleKey()))
                    .message(connectorIssue.message()).build());
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(fileSystem.baseDir());
        if (PomUtils.isDevKitConnector(mavenProject)) {
            final InputFile pomFile = Iterables.getOnlyElement(fileSystem.inputFiles(fileSystem.predicates().matchesPathPattern("pom.xml")));
            for (MavenCheck mavenCheck : buildMavenChecks()) {
                final Iterable<ConnectorIssue> analyse = mavenCheck.analyze(mavenProject);
                for (ConnectorIssue issue : analyse) {
                    logAndRaiseIssue(pomFile, issue);
                }
            }
        }
    }

    private Iterable<MavenCheck> buildMavenChecks() {
        List<MavenCheck> scanners = Lists.newArrayList();
        scanners.add(new DistributionManagementByCategoryCheck());
        scanners.add(new ScopeProvidedInMuleDependenciesCheck());
        scanners.add(new SnapshotArtifactVersionPresentCheck());
        scanners.add(new SnapshotDependenciesCheck());
        scanners.add(new SourceDeploymentForStandardCategoryCheck());
        scanners.add(new TestingFrameworkNotOverwrittenCheck());
        return scanners;
    }
}
