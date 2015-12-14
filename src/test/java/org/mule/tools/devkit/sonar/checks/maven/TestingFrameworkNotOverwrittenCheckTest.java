package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TestingFrameworkNotOverwrittenCheckTest extends BasicPomTestBase {

    @Test
    public void checkTestingFrameworkNotOverwritten() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final TestingFrameworkNotOverwrittenCheck check = new TestingFrameworkNotOverwrittenCheck();
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue connectorIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(connectorIssue.ruleKey(), is("testing-framework-not-overwritten"));
        assertThat(connectorIssue.message(), is("'connector-testing-framework' must not be overwritten."));
    }
}
