package org.mule.tools.devkit.sonar.checks.structure;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;

import com.google.common.collect.Iterables;

public class TestDataBuilderExistCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNotExist() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/test-data-builder-exists/not-exists");
        when(fs.baseDir()).thenReturn(baseDir);
        final TestDataBuilderExistsCheck check = new TestDataBuilderExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("test-data-builder-exists"));
        assertThat(first.message(), is("TestDataBuilder.java doesn't exist."));
    }

    @Test
    public void checkNoIcons() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/test-data-builder-exists/exists-wrong-location");
        when(fs.baseDir()).thenReturn(baseDir);
        final TestDataBuilderExistsCheck check = new TestDataBuilderExistsCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("test-data-builder-exists"));
        assertThat(
                first.message(),
                is("TestDataBuilder.java exists, but it's not in the appropriate folder (src/test/files/structure/test-data-builder-exists/exists-wrong-location/sub/TestDataBuilder.java). It must be located in 'org.mule.modules.<connector-project>.automation.functional.TestDataBuilder.java'."));
    }

}
