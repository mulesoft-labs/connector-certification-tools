package org.mule.tools.devkit.sonar.checks.structure;

import org.junit.Before;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.internal.google.common.collect.Iterables;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GitIgnoreExistsCheckTest {

    private FileSystem fs;

    @Before
    public void setUp() {
        fs = mock(FileSystem.class);
    }

    @Test
    public void checkNotExists() {
        File baseDir = new File("src/test/files/structure/.gitignore-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        GitIgnoreExistsCheck check = new GitIgnoreExistsCheck(fs);
        Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(first.message(), is(".gitignore doesn't exist."));
    }

    @Test
    public void checkMoreThanOneGitIgnoreFile() {
        File baseDir = new File("src/test/files/structure/.gitignore-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        GitIgnoreExistsCheck check = new GitIgnoreExistsCheck(fs);
        Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(first.message(), is("More than one .gitignore file in project."));
    }
}
