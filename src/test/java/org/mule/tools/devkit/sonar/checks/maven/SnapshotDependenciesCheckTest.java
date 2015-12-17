package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SnapshotDependenciesCheckTest {

    @Test
    public void checkSnapshotsInDependencies() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/snapshot-dependencies-not-allowed/snapshot-in-dependencies"));
        final SnapshotDependenciesCheck check = new SnapshotDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-dependencies-not-allowed"));
        assertThat(connectorIssue.message(),
                is("Project version is not a snapshot (1.0.0), so it should not declare any snapshot dependencies (mule-devkit-annotations:3.7.2-SNAPSHOT)."));
    }

    @Test
    public void checkSnapshotInParentVersion() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/snapshot-dependencies-not-allowed/snapshot-in-parent-version"));
        final SnapshotDependenciesCheck check = new SnapshotDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-dependencies-not-allowed"));
        assertThat(connectorIssue.message(),
                is("Project version is not a snapshot (1.0.0), so it should not inherit from a snapshot version parent (mule-devkit-parent:3.7.2-SNAPSHOT)."));
    }
}
