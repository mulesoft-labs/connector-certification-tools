package org.mule.tools.devkit.sonar.checks.structure;

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
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rule.RuleKey;

import java.io.File;
import java.util.List;

public class StructureSensor implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(StructureSensor.class);

    private final MavenProject mavenProject;
    private final ResourcePerspectives resourcePerspectives;
    private final FileSystem fileSystem;

    public StructureSensor(ResourcePerspectives resourcePerspectives, FileSystem fileSystem) {
        this.mavenProject = PomUtils.createMavenProjectFromPomFile();
        this.resourcePerspectives = resourcePerspectives;
        this.fileSystem = fileSystem;
    }

    private void logAndRaiseIssue(Resource pomFile, ConnectorIssue connectorIssue) {
        logger.info(connectorIssue.message());
        Issuable issuable = resourcePerspectives.as(Issuable.class, pomFile);
        if (issuable != null) {
            issuable.addIssue(issuable.newIssueBuilder().ruleKey(RuleKey.of(ConnectorCertificationRulesDefinition.getStructRepositoryKey(), connectorIssue.ruleKey()))
                    .message(connectorIssue.message()).build());
        }
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        return true;
    }

    @Override
    public void analyse(Project project, SensorContext sensorContext) {
        if (PomUtils.isDevKitConnector(mavenProject)) {
            final Directory demo = Directory.fromIOFile(new File("demo"), project);
            for (StructureCheck structureCheck : buildStructureChecks()) {
                final Iterable<ConnectorIssue> analyse = structureCheck.analyze(mavenProject, project);
                for (ConnectorIssue issue : analyse) {
                    logAndRaiseIssue(demo, issue);
                }
            }
        }
    }

    private Iterable<StructureCheck> buildStructureChecks() {
        List<StructureCheck> scanners = Lists.newArrayList();
//        scanners.add(new LicenseDeclarationFilesCheck());
        return scanners;
    }
}
