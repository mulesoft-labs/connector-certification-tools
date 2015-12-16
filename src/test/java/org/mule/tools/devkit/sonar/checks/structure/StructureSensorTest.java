package org.mule.tools.devkit.sonar.checks.structure;

import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.resources.Project;

import java.io.File;

import static org.mockito.Mockito.mock;

public class StructureSensorTest {

    @Test
    public void testMariano() {
        DefaultFileSystem fs = new DefaultFileSystem().setBaseDir(new File("src/test/java/"));
        // File file = new File("src/test/java/org/sonar/plugins/java/JavaSquidSensorTest.java");
        // fs.add(new DefaultInputFile(file.getPath()).setFile(file).setLanguage("java"));
        Project project = mock(Project.class);
        ResourcePerspectives resourcePerspectives = mock(ResourcePerspectives.class);

        // SonarComponents sonarComponents = createSonarComponentsMock(fs);
        // DefaultJavaResourceLocator javaResourceLocator = new DefaultJavaResourceLocator(fs, resourcePerspectives, mock(SuppressWarningsFilter.class));
        // StructureSensor structureSensor = new StructureSensor(resourcePerspectives, mock(FileSystem.class));
        // SensorContext context = mock(SensorContext.class);
        // when(context.getResource(any(InputPath.class))).thenReturn(org.sonar.api.resources.File.create("src/test/java/org/sonar/plugins/java/JavaSquidSensorTest.java"));
        //
        // structureSensor.analyse(project, context);

    }
    //
    // @Test
    // public void checkNoLicenseFiles() throws IOException, XmlPullParserException {
    // final MavenProject mavenProject = createMavenProjectFromPom("StructureSensorTest-NoDistribution-pom.xml");
    // final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
    // final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
    //
    // assertThat(Iterables.size(pomIssues), is(1));
    // ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
    // assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
    // assertThat(connectorIssue.message(),
    // is("Distribution Management must be properly configured in pom.xml under a <distributionManagement> tag, according to connector category."));
    // }
    //
    // @Test
    // public void checkCommunityWithWrongLicense() throws IOException, XmlPullParserException {
    // final MavenProject mavenProject = createMavenProjectFromPom("StructureSensorTest-NoDeployRepo-pom.xml");
    // final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
    // final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
    //
    // assertThat(Iterables.size(pomIssues), is(1));
    // ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
    // assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
    // assertThat(connectorIssue.message(), is("Distribution Management is missing required <repository> configuration."));
    // }
    //
    // @Test
    // public void checkNoSnapshotRepo() throws IOException, XmlPullParserException {
    // final MavenProject mavenProject = createMavenProjectFromPom("StructureSensorTest-NoSnapshotRepo-pom.xml");
    // final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
    // final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
    //
    // assertThat(Iterables.size(pomIssues), is(1));
    // ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
    // assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
    // assertThat(connectorIssue.message(), is("Distribution Management is missing required <snapshotRepository> configuration."));
    // }
    //
    // @Test
    // public void checkWrongDistributionConfig() throws IOException, XmlPullParserException {
    // final MavenProject mavenProject = createMavenProjectFromPom("StructureSensorTest-WrongDistributionConfig-pom.xml");
    // final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
    // final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
    //
    // assertThat(Iterables.size(pomIssues), is(1));
    // ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
    // assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
    // assertThat(connectorIssue.message(), is("Premium connectors must have a <repository> tag configured with <id>mulesoft-ee-releases</id>."));
    //
    // }
}
