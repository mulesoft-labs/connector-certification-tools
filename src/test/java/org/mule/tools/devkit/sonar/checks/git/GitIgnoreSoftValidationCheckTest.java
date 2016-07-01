package org.mule.tools.devkit.sonar.checks.git;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

import java.io.File;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;
import static org.mule.tools.devkit.sonar.checks.git.GitIgnoreHardValidationCheck.KEY;

public class GitIgnoreSoftValidationCheckTest {

    private SensorContext sensorContext;
    private InputFile inputFile;
    private Project project;
    private GitIgnoreHardValidationCheck check;
    private NewIssue issue;
    private NewIssueLocation location;
    private List<String> patterns = Lists.newArrayList("*.class", "*.jar", "*.war",
            "target", ".classpath",
            ".settings", ".project",
            ".factorypath", ".idea", "*.iml", "*.ipr", "*.iws", "bin", ".DS_Store", "automation-credentials.properties",
            "muleLicenseKey.lic");

    @Before
    public void setUp() {
        check = new GitIgnoreHardValidationCheck();
        sensorContext = mock(SensorContext.class);
        inputFile = mock(InputFile.class);
        project = mock(Project.class);
        issue = mock(NewIssue.class);
        location = mock(NewIssueLocation.class);
    }

    @Test
    public void analyzeInvalidTest() {
        when(inputFile.file()).thenReturn(new File("src/test/files/git/.gitignore-soft-validation/invalid/.gitignore"));
        when(issue.forRule(eq(RuleKey.of("git", KEY)))).thenReturn(issue);
        when(issue.newLocation()).thenReturn(location);
        when(location.message(anyString())).thenReturn(location);
        when(location.on(same(inputFile))).thenReturn(location);
        when(issue.at(same(location))).thenReturn(issue);
        when(sensorContext.newIssue()).thenReturn(issue);
        check.analyse(project, sensorContext, inputFile);
        for (String pattern : patterns) {
            verify(location).message(eq(String.format(".gitignore file in project is missing '%s'.", pattern)));
        }
        verify(issue, times(patterns.size())).save();
    }


    @Test
    public void analyzeValidTest() {
        when(inputFile.file()).thenReturn(new File("src/test/files/git/.gitignore-soft-validation/valid/.gitignore"));
        when(sensorContext.newIssue()).thenThrow(new RuntimeException("No validation issue should be found."));
        check.analyse(project, sensorContext, inputFile);
    }
}
