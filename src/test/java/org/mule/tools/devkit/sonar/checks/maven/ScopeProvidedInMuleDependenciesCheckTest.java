package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScopeProvidedInMuleDependenciesCheckTest extends BasicPomTestBase {

    @Test
    public void checkNoScope() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("mule-scope-provided"));
        assertThat(connectorIssue.message(), is("Artifact 'mule-devkit-annotations' is a Mule dependency and should be declared with <scope>provided</scope>."));
    }

    @Test
    public void checkScopeCompile() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom("ScopeProvidedInMuleDependenciesCheckTest-ScopeCompile-pom.xml");
        final ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("mule-scope-provided"));
        assertThat(connectorIssue.message(), is("Artifact 'mule-devkit-annotations' is a Mule dependency and should be declared with <scope>provided</scope>."));
    }

}
