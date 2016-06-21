package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.ImmutableList;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(key = GitIgnoreValidatePatterns.KEY, name = ".gitignore pattern rules", description = ".gitignore files should follow some common rules.", priority = Priority.MINOR, tags = { "connector-certification"
})
public class GitIgnoreValidatePatterns extends GitIgnoreExistsCheck {

    public static final String KEY = "gitignore-patterns";

    public GitIgnoreValidatePatterns(FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        setApplyByTrue(false);
        setRequiredGitignoreFields(ImmutableList.of("\\*\\.classpath", "\\*\\.project", "\\*\\.settings"));
        setIssueMessage("The following patterns should not contain an asterisk '%s'.");
        return super.analyze(mavenProject);
    }

    @Override
    protected String ruleKey() {
        return KEY;
    }
}
