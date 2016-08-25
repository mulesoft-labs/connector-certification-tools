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

public class DemosExistCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNoDemosFolder() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/demo-exist/no-demos-folder");
        when(fs.baseDir()).thenReturn(baseDir);
        final DemoExistCheck check = new DemoExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("demo-exist"));
        assertThat(first.message(), is("'demo' directory doesn't exist or it's a file rather than a directory."));
    }

    @Test
    public void checkEmptyDemosFolder() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/demo-exist/empty-demo-folder");
        when(fs.baseDir()).thenReturn(baseDir);
        final DemoExistCheck check = new DemoExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("demo-exist"));
        assertThat(first.message(), is("'demo' directory exists, but contains no demos."));
    }

    @Test
    public void checkDemoNoReadme() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/demo-exist/demo-no-readme");
        when(fs.baseDir()).thenReturn(baseDir);
        final DemoExistCheck check = new DemoExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("demo-exist"));
        assertThat(first.message(), is("demo named 'demo-one' is missing a README.md file explaining the purpose of the demo."));
    }

    @Test
    public void checkIOException() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/demo-exist/demos-is-file");
        when(fs.baseDir()).thenReturn(baseDir);
        final DemoExistCheck check = new DemoExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("demo-exist"));
        assertThat(first.message(), is("'demo' directory doesn't exist or it's a file rather than a directory."));
    }

}
