package org.mule.tools.devkit.sonar.checks.structure;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(key = ReadmeExistsCheck.KEY, name = "README.md should be present", description = "There should exist a file named 'README.md' in root folder", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class ReadmeExistsCheck extends ExistingResourceCheck {

    public static final String KEY = "readme-exists";

    public ReadmeExistsCheck(FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    protected String resourcePath() {
        return "README.md";
    }

    @Override
    protected String ruleKey() {
        return KEY;
    }
}
