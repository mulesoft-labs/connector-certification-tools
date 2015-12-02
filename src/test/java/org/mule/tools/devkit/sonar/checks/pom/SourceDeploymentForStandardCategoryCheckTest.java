package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SourceDeploymentForStandardCategoryCheckTest extends BasicPomTestBase {

    @Test
    public void checkSourceDeployment() throws IOException, XmlPullParserException {
        final MavenProject mavenProject = createMavenProjectFromPom(pomForCurrentClass());
        final SourceDeploymentForStandardCategoryCheck check = new SourceDeploymentForStandardCategoryCheck();
        final Iterable<PomIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(1));
        PomIssue pomIssue = Iterables.getOnlyElement(pomIssues);
        assertThat(pomIssue.ruleKey().rule(), is("source-deployment-for-standard-category"));
        assertThat(pomIssue.message(), is("Standard connectors must declare a 'maven-source-plugin' in pom.xml to prevent the deployment of its sources."));
    }

}
