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

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

public class DevkitLatestVersionCheckTest {

    @Test
    public void checkDevkitVersionIsTrue() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-is-latest-version"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        assertThat(Iterables.size(pomIssues), is(0));
    }

    @Test
    public void checkDevkitVersionIsFalse() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-is-not-latest-version"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("devkit-latest-version"));
        assertThat(connectorIssue.message(),
                is(String.format("Current connector Devkit version '%s' is not up to date. If feasible, use the latest DevKit stable version '%s'.",
                        check.getCurrentVersion(), check.getLatestVersion())));
    }

    @Test
    public void checkDevkitVersionIsUnstable() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-is-unstable-version"));
        final DevkitLatestVersionCheck check = new DevkitLatestVersionCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);
        String currentVersion = mavenProject.getModel().getParent().getVersion();
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("devkit-latest-version"));
        assertThat(connectorIssue.message(),
                is(String.format("Current connector Devkit version '%s' is not the last stable version. If feasible, use the latest sta stable one '%s'.",
                        currentVersion, check.getLatestVersion())));
    }
}