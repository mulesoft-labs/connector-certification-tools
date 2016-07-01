package org.mule.tools.devkit.sonar.checks.structure;

import org.sonar.api.batch.fs.FileSystem;

import static org.mockito.Mockito.mock;

public class GitIgnoreValidatePatternsTest {

    final FileSystem fs = mock(FileSystem.class);

    /*@Test
    public void validateGitIgnoreFileContent() {
        final File baseDir = new File("src/test/files/structure/.gitignore-exists/exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final GitIgnoreValidationCheck check = new GitIgnoreValidatePatterns(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyse(null);
        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(
                first.message(),
                is("The following patterns should not contain an asterisk '*.classpath'."));
    }*/
}
