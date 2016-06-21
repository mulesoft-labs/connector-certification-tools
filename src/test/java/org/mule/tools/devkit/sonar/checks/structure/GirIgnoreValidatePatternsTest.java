package org.mule.tools.devkit.sonar.checks.structure;

import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.internal.google.common.collect.Iterables;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GirIgnoreValidatePatternsTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void validateGitIgnoreFileContent() {
        final File baseDir = new File("src/test/files/structure/.gitignore-exists/exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final GitIgnoreExistsCheck check = new GitIgnoreValidatePatterns(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);
        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("gitignore-exist"));
        assertThat(
                first.message(),
                is("The following patterns should not contain an asterisk '*.classpath'."));
    }
}
