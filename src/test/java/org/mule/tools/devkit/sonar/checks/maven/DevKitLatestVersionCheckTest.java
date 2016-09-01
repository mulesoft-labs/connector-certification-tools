package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;

import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mule.tools.devkit.sonar.utils.DevkitUtils.getLatestDevKitVersion;

public class DevKitLatestVersionCheckTest {

    private MavenProject mavenProject;

    @Test
    public void checkDevKitVersionIsLatest() throws IOException, XmlPullParserException, XMLStreamException {
        Iterable<ConnectorIssue> pomIssues = analyze("src/test/files/maven/devKit-latest-version/devKit-version-is-latest");
        assertThat(pomIssues, emptyIterable());
    }

    @Test
    public void checkDevKitVersionIsNotLatest() throws IOException, XmlPullParserException, XMLStreamException {
        devKitVersionIsRevisionOrNotLatest("src/test/files/maven/devKit-latest-version/devKit-version-is-not-latest");
    }

    @Test
    public void checkDevkitVersionIsRevision() throws IOException, XmlPullParserException, XMLStreamException {
        devKitVersionIsRevisionOrNotLatest("src/test/files/maven/devKit-latest-version/devKit-version-is-revision");
    }

    private void devKitVersionIsRevisionOrNotLatest(String filePath) {
        Iterable<ConnectorIssue> pomIssues = analyze(filePath);
        assertThat(pomIssues, iterableWithSize(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("devkit-latest-version"));
        assertThat(connectorIssue.message(),
                is(String.format("Current connector DevKit version '%s' is not the latest stable version. If feasible, use version '%s'.",
                        mavenProject.getModel()
                                .getParent()
                                .getVersion(), getLatestDevKitVersion())));
    }

    private Iterable<ConnectorIssue> analyze(String fileName) {
        mavenProject = PomUtils.createMavenProjectFromPomFile(new File(fileName));
        return new DevKitLatestVersionCheck().analyze(mavenProject);
    }
}
