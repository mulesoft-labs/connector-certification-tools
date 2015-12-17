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

public class IconsExistCheckTest {

    final FileSystem fs = mock(FileSystem.class);

    @Test
    public void checkNoIconsFolder() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/icons-exist/no-icons-folder");
        when(fs.baseDir()).thenReturn(baseDir);
        final IconsExistCheck check = new IconsExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("icons-exist"));
        assertThat(first.message(), is("Folder 'icons' is missing."));
    }

    @Test
    public void checkNoIcons() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/icons-exist/no-icons");
        when(fs.baseDir()).thenReturn(baseDir);
        final IconsExistCheck check = new IconsExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("icons-exist"));
        assertThat(first.message(), is("Folder 'icons' has no png files."));
    }

    @Test
    public void checkWrongFilename() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/icons-exist/wrong-filename");
        when(fs.baseDir()).thenReturn(baseDir);
        final IconsExistCheck check = new IconsExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("icons-exist"));
        assertThat(first.message(), is("Unexpected file found in 'icons' folder: 'mongo-connector-no-size.png'."));
    }

    @Test
    public void checkWrongSizes() throws IOException, XmlPullParserException {
        final File baseDir = new File("src/test/files/structure/icons-exist/wrong-size");
        when(fs.baseDir()).thenReturn(baseDir);
        final IconsExistCheck check = new IconsExistCheck(fs);
        final Iterable<ConnectorIssue> pomIssues = check.analyze(null);

        assertThat(Iterables.size(pomIssues), is(1));
        final ConnectorIssue first = Iterables.getFirst(pomIssues, null);
        assertThat(first.ruleKey(), is("icons-exist"));
        assertThat(first.message(), is("Icon file 'mongo-connector-48x32.png' found but image size is incorrect (24x16)."));
    }
}
