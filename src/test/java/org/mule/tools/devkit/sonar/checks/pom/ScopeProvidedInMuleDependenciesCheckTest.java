package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ScopeProvidedInMuleDependenciesCheckTest extends BasicPomTestBase {

    @Test
    public void checkScopeProvidedInMuleDependencies() throws IOException, XmlPullParserException {

        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck();
        final Iterable<PomIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        PomIssue pomIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(pomIssue.ruleKey().rule(), is("mule-scope-provided"));
        assertThat(pomIssue.message(), is("Artifact 'mule-devkit-annotations' is a Mule dependency and should be declared with <scope>provided</scope>."));
    }

}
