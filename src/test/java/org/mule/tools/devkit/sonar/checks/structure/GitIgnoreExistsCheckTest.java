package org.mule.tools.devkit.sonar.checks.structure;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.internal.google.common.collect.Iterables;

public class GitIgnoreExistsCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNotExists() {
        final File baseDir = new File("src/test/files/structure/.gitignore-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final GitIgnoreExistsCheck check = new GitIgnoreExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(first.message(), is(".gitignore doesn't exist."));
    }

    @Test
    public void checkMoreThanOneGitIgnoreFile() {
        final File baseDir = new File("src/test/files/structure/.gitignore-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final GitIgnoreExistsCheck check = new GitIgnoreExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(first.message(), is("More than one .gitignore file in project."));
    }

    @Test
    public void validateGitIgnoreFileContent() {
        final File baseDir = new File("src/test/files/structure/.gitignore-exists/exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final GitIgnoreExistsCheck check = new GitIgnoreExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(
                first.message(),
                is(".gitignore file in project is missing the following exclusions: 'target/, .classpath, .settings/, .project, .factorypath, .idea/, *.iml, *.ipr, *.iws, bin/, .DS_Store, automation-credentials.properties, muleLicenseKey.lic'."));
    }
}
