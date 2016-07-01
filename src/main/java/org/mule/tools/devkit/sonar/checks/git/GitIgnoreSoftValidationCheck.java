package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.check.Rule;

import static org.mule.tools.devkit.sonar.checks.git.GitIgnoreSoftValidationCheck.KEY;
import static org.sonar.check.Priority.MINOR;

@Rule(key = KEY,
        name = ".gitignore pattern soft rules",
        description = ".gitignore files should follow some common rules.",
        priority = MINOR,
        tags = "connector-certification")
public class GitIgnoreSoftValidationCheck extends GitIgnoreValidationCheck {

    public static final String KEY = "gitignore-hard-validation";

    public GitIgnoreSoftValidationCheck() {
        super(KEY, "'%s' should not contain an asterisk (*).", "\\*\\.classpath", "\\*\\.project", "\\*\\.settings");
    }
}
