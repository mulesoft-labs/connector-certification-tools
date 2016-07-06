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

public class SnapshotArtifactVersionPresentCheckTest {

    @Test
    public void checkSnapshotInArtifactVersion() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/snapshot-artifact-present/snapshot-artifact-not-present"));
        final SnapshotArtifactVersionPresentCheck check = new SnapshotArtifactVersionPresentCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("snapshot-artifact-is-present"));
        assertThat(connectorIssue.message(),
                is("Project artifact [certification-plugin] MUST have a SNAPSHOT version. Current version is [1.0.0] but it should be [1.0.0-SNAPSHOT]."));
    }
}
