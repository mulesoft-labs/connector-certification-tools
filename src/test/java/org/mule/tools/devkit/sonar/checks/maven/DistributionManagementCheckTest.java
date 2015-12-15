package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DistributionManagementCheckTest extends BasicPomTestBase {

    @Test
    public void checkNoDistributionManagement() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("DistributionManagementCheckTest-NoDistribution-pom.xml");
        final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
        assertThat(connectorIssue.message(),
                is("Distribution Management must be properly configured in pom.xml under a <distributionManagement> tag, according to connector category."));
    }

    @Test
    public void checkNoDeployRepo() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("DistributionManagementCheckTest-NoDeployRepo-pom.xml");
        final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
        assertThat(connectorIssue.message(), is("Distribution Management is missing required <repository> configuration."));
    }

    @Test
    public void checkNoSnapshotRepo() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("DistributionManagementCheckTest-NoSnapshotRepo-pom.xml");
        final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
        assertThat(connectorIssue.message(), is("Distribution Management is missing required <snapshotRepository> configuration."));
    }

    @Test
    public void checkWrongDistributionConfig() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("DistributionManagementCheckTest-WrongDistributionConfig-pom.xml");
        final DistributionManagementByCategoryCheck check = new DistributionManagementByCategoryCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("distribution-management-by-category"));
        assertThat(connectorIssue.message(), is("Premium connectors must have a <repository> tag configured with <id>mulesoft-ee-releases</id>."));

    }
}
