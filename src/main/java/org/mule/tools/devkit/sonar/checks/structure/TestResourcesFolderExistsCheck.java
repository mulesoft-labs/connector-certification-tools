package org.mule.tools.devkit.sonar.checks.structure;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(key = TestResourcesFolderExistsCheck.KEY, name = "Test Resources directory must be present", description = "There must exist a directory named 'src/test/resources/'", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class TestResourcesFolderExistsCheck extends ResourceExistsCheckBase {

    public static final String KEY = "test-resources-folder-exists";

    public TestResourcesFolderExistsCheck(FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    protected String resourcePath() {
        return "src/test/resources";
    }

    @Override
    protected String ruleKey() {
        return KEY;
    }
}
