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

public class ScopeProvidedInMuleDependenciesCheckTest {

    @Test
    public void checkNoScope() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/scope-provided-in-mule-dependencies/no-scope"));
        final ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("mule-scope-provided"));
        assertThat(connectorIssue.message(), is("Artifact 'mule-devkit-annotations' is a Mule dependency and should be declared with <scope>provided</scope>."));
    }

    @Test
    public void checkScopeCompile() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/scope-provided-in-mule-dependencies/scope-compile"));
        final ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("mule-scope-provided"));
        assertThat(connectorIssue.message(), is("Artifact 'mule-devkit-annotations' is a Mule dependency and should be declared with <scope>provided</scope>."));
    }

}
