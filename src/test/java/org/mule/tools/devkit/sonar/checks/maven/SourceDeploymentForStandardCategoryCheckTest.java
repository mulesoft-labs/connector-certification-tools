package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SourceDeploymentForStandardCategoryCheckTest extends BasicPomTestBase {

    @Test
    public void checkSourceDeployment() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final SourceDeploymentForStandardCategoryCheck check = new SourceDeploymentForStandardCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("source-deployment-for-standard-category"));
        assertThat(connectorIssue.message(), is("Standard connectors must declare a 'maven-source-plugin' in pom.xml to prevent the deployment of its sources."));
    }

    @Test
    public void checkSourceDeploymentEmptyBuild() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("SourceDeploymentForStandardCategoryCheckTest-EmptyBuild-pom.xml");
        final SourceDeploymentForStandardCategoryCheck check = new SourceDeploymentForStandardCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("source-deployment-for-standard-category"));
        assertThat(connectorIssue.message(), is("Standard connectors must declare a 'maven-source-plugin' in pom.xml to prevent the deployment of its sources."));
    }

    @Test
    public void checkSourceDeploymentWrongPhase() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("SourceDeploymentForStandardCategoryCheckTest-WrongPhase-pom.xml");
        final SourceDeploymentForStandardCategoryCheck check = new SourceDeploymentForStandardCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("source-deployment-for-standard-category"));
        assertThat(connectorIssue.message(), is("Standard connectors must declare a 'maven-source-plugin' in pom.xml to prevent the deployment of its sources."));
    }

}
