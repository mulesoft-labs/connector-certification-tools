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

public class ReadmeExistsCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNotExists() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/readme-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final ReadmeExistsCheck check = new ReadmeExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("readme-exists"));
        assertThat(first.message(), is("File README.md is missing."));
    }

}
