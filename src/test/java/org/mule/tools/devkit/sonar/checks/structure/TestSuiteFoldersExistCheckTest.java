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

public class TestSuiteFoldersExistCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkSystemNotExists() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/test-suite-folders-exist/system-not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final TestSuiteFoldersExistCheck check = new TestSuiteFoldersExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getOnlyElement(pomIssues, null);
        assertThat(first.ruleKey(), is("test-suite-folders-exists"));
        assertThat(first.message(), is("System test suite directory doesn't exist."));
    }

}
