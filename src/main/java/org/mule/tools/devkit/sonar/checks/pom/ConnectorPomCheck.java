package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConnectorPomCheck implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(ConnectorPomCheck.class);

    protected final MavenProject mavenProject;
    protected final ResourcePerspectives resourcePerspectives;

    public ConnectorPomCheck(ResourcePerspectives resourcePerspectives) {
        this.mavenProject = createMavenProjectFromPom("pom.xml");
        this.resourcePerspectives = resourcePerspectives;
    }

    protected MavenProject createMavenProjectFromPom(String pomResource)  {
        MavenProject mavenProject;
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(pomResource))) {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            Model model = pomReader.read(reader);
            mavenProject = new MavenProject(model);
        } catch (IOException | XmlPullParserException e) {
            throw new IllegalStateException("Couldn't initalize pom", e);
        }
        return mavenProject;
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
        scanners.add(new SnapshotDependenciesCheck());
        scanners.add(new SourceDeploymentForStandardCategoryCheck());
        scanners.add(new TestingFrameworkNotOverwrittenCheck());
        return scanners;
    }
}
