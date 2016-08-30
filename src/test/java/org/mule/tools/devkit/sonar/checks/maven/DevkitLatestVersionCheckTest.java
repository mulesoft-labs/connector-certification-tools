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

import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class DevkitLatestVersionCheckTest {

    @Test
    public void checkDevkitVersionIsLatest() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-version-is-latest"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        assertThat(pomIssues, emptyIterable());
    }

    @Test
    public void checkDevkitVersionIsNotLatest() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-version-is-not-latest"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        String devkitVersion = mavenProject.getModel().getParent().getVersion();
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("devkit-latest-version"));
        assertThat(connectorIssue.message(),
                is(String.format("Current connector Devkit version '%s' is not the latest stable version. If feasible, use version '%s'.",
                        PomUtils.getCurrentDevkitVersion(devkitVersion), PomUtils.getLatestDevkitVersion())));
    }

    @Test
    public void checkDevkitVersionIsRevision() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-version-is-revision"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        String currentDevkitVersion = mavenProject.getModel().getParent().getVersion();
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("devkit-latest-version"));
        assertThat(connectorIssue.message(),
                is(String.format("Current connector Devkit version '%s' is not the latest stable version. If feasible, use version '%s'.",
                        currentDevkitVersion, PomUtils.getLatestDevkitVersion())));
    }
}