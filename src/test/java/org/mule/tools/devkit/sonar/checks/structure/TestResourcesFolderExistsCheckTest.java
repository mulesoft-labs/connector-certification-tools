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

public class TestResourcesFolderExistsCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNotExists() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/test-resources-folder-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final TestResourcesFolderExistsCheck check = new TestResourcesFolderExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getOnlyElement(pomIssues, null);
        assertThat(first.ruleKey(), is("test-resources-folder-exists"));
        assertThat(first.message(), is("Test Resources directory doesn't exist."));
    }
}
