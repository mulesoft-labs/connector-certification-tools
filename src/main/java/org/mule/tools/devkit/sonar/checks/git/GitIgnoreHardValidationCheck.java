package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.check.Rule;

import static org.mule.tools.devkit.sonar.checks.git.GitIgnoreHardValidationCheck.KEY;
import static org.sonar.check.Priority.CRITICAL;

@Rule(key = KEY,
        name = ".gitignore pattern hard rules",
        description = ".gitignore files should follow some common rules.",
        priority = CRITICAL,
        tags = "connector-certification")
public class GitIgnoreHardValidationCheck extends GitIgnoreValidationCheck {

    public static final String KEY = "gitignore-soft-validation";

    public GitIgnoreHardValidationCheck() {
        super(KEY, ".gitignore file in project is missing '%s'.", "\\*\\.class", "\\*\\.jar", "\\*\\.war",
                "[/]?target[/]?", "[*]?[/]?\\.classpath",
                "[*]?[/]?\\.settings[/]?", "[*]?[/]?\\.project",
                "[*]?[/]?\\.factorypath", "[/]?\\.idea[/]?", "\\*\\.iml", "\\*\\.ipr", "\\*\\.iws", "[/]?bin[/]?", "[/]?\\.DS_Store", "automation-credentials\\.properties",
                "muleLicenseKey.lic");
    }
}
