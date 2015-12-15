package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SnapshotDependenciesCheckTest extends BasicPomTestBase {

    @Test
    public void checkSnapshotsInDependencies() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final SnapshotDependenciesCheck check = new SnapshotDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-dependencies-not-allowed"));
        assertThat(connectorIssue.message(), is("Remove SNAPSHOT version from artifact 'mule-devkit-annotations'."));
    }

    @Test
    public void checkSnapshotInParentVersion() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("SnapshotDependenciesCheckTest-ParentVersion-pom.xml");
        final SnapshotDependenciesCheck check = new SnapshotDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-dependencies-not-allowed"));
        assertThat(connectorIssue.message(), is("Remove SNAPSHOT version from artifact 'mule-devkit-parent'."));
    }

    @Test
    public void checkSnapshotInProjectVersion() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("SnapshotDependenciesCheckTest-ProjectVersion-pom.xml");
        final SnapshotDependenciesCheck check = new SnapshotDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-dependencies-not-allowed"));
        assertThat(connectorIssue.message(), is("Remove SNAPSHOT version from artifact 'certification-plugin'."));
    }

}