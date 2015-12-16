package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.sonar.api.batch.fs.FileSystem;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LicenseDeclarationFilesCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNoLicenseFiles() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/no-license-files");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(2));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("license-declaration-files"));
        assertThat(first.message(), is("File LICENSE.md is missing. Please add a license file."));
        final ConnectorIssue second = Iterables.getLast(pomIssues);
        assertThat(second.ruleKey(), is("license-declaration-files"));
        assertThat(second.message(), is("File LICENSE_HEADER.txt is missing. Please add a license file."));
    }

    @Test
    public void checkCommunityWithWrongLicense() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/community-with-wrong-license");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(2));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("license-declaration-files"));
        assertThat(first.message(), startsWith("Difference in license file LICENSE.md. Please check the diff between expected and actual:"));
        assertThat(first.message(), containsString("LICENSE.md.community"));
        final ConnectorIssue second = Iterables.getLast(pomIssues);
        assertThat(second.ruleKey(), is("license-declaration-files"));
        assertThat(second.message(), startsWith("Difference in license file LICENSE_HEADER.txt. Please check the diff between expected and actual:"));
        assertThat(second.message(), containsString("LICENSE_HEADER.txt.community"));
    }

    @Test
    public void checkCertifiedWithWrongLicense() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/certified-with-wrong-license");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(2));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("license-declaration-files"));
        assertThat(first.message(), startsWith("Difference in license file LICENSE.md. Please check the diff between expected and actual:"));
        assertThat(first.message(), containsString("LICENSE.md.certified"));
        final ConnectorIssue second = Iterables.getLast(pomIssues);
        assertThat(second.ruleKey(), is("license-declaration-files"));
        assertThat(second.message(), startsWith("Difference in license file LICENSE_HEADER.txt. Please check the diff between expected and actual:"));
        assertThat(second.message(), containsString("LICENSE_HEADER.txt.certified"));
    }

    @Test
    public void checkSelectWithWrongLicense() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/select-with-wrong-license");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(2));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("license-declaration-files"));
        assertThat(first.message(), startsWith("Difference in license file LICENSE.md. Please check the diff between expected and actual:"));
        assertThat(first.message(), containsString("LICENSE.md.select"));
        final ConnectorIssue second = Iterables.getLast(pomIssues);
        assertThat(second.ruleKey(), is("license-declaration-files"));
        assertThat(second.message(), startsWith("Difference in license file LICENSE_HEADER.txt. Please check the diff between expected and actual:"));
        assertThat(second.message(), containsString("LICENSE_HEADER.txt.select"));
    }

    @Test
    public void checkStandardWithWrongLicense() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/standard-with-wrong-license");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(Iterables.size(pomIssues), is(2));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("license-declaration-files"));
        assertThat(first.message(), startsWith("Difference in license file LICENSE.md. Please check the diff between expected and actual:"));
        assertThat(first.message(), containsString("LICENSE.md.standard"));
        final ConnectorIssue second = Iterables.getLast(pomIssues);
        assertThat(second.ruleKey(), is("license-declaration-files"));
        assertThat(second.message(), startsWith("Difference in license file LICENSE_HEADER.txt. Please check the diff between expected and actual:"));
        assertThat(second.message(), containsString("LICENSE_HEADER.txt.standard"));
    }

    @Test
    public void checkPremiumWithWrongLicense() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/license-declaration-files/premium-with-wrong-license");
        when(fs.baseDir()).thenReturn(baseDir);
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(baseDir);
        final LicenseDeclarationFilesCheck check = new LicenseDeclarationFilesCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(mavenProject);

        assertThat(pomIssues, Matchers.<ConnectorIssue> emptyIterable());
    }
}
