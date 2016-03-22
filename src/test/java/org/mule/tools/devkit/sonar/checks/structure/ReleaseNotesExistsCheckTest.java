package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Iterables;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReleaseNotesExistsCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNotExists() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/release-notes-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final ReleaseNotesExistsCheck check = new ReleaseNotesExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("release-notes-exists"));
        assertThat(first.message(), is("File release-notes.adoc is missing."));
    }

    @Test
    public void checkWrongContent() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/release-notes-exists/wrong-content");
        when(fs.baseDir()).thenReturn(baseDir);
        final ReleaseNotesExistsCheck check = new ReleaseNotesExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("release-notes-exists"));
        assertThat(first.message(), is("File release-notes.adoc found but doesn't have proper content (size: 14 bytes)."));
    }

}
