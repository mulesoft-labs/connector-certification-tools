package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Lists;
import org.apache.commons.lang.WordUtils;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Rule(key = TestSuiteFoldersExistCheck.KEY, name = "Test Suite directories must be present", description = "There must exist 3 test suite directories: one for Functional ('automation/functional'), one for System ('automation/system') and one for Unit ('automation/unit'). Also, there must be a Runner package ('automation/runner').", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class TestSuiteFoldersExistCheck implements StructureCheck {

    public static final String KEY = "test-suite-folders-exists";
    public static final List<String> TEST_PACKAGES = new ArrayList<String>() {

        {
            add("functional");
            add("system");
            add("unit");
            add("runner");
        }
    };

    private final FileSystem fileSystem;

    public TestSuiteFoldersExistCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        displayDirectoryContents(fileSystem.baseDir(), issues);
        return issues;
    }

    public static void displayDirectoryContents(File dir, List<ConnectorIssue> issues) {
        try {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String subDir = file.getCanonicalPath();
                        if (subDir.endsWith("/automation")) {
                            File[] suitePackages = file.listFiles();
                            for (File suite : suitePackages) {
                                if (suite.isDirectory()) {
                                    TEST_PACKAGES.remove(suite.getName());
                                }
                            }
                            if (TEST_PACKAGES.size() > 0) {
                                for (String noSuite : TEST_PACKAGES) {
                                    issues.add(new ConnectorIssue(KEY, String.format("%s test suite directory doesn't exist.", WordUtils.capitalize(noSuite))));
                                }
                            }
                        }
                    }
                    displayDirectoryContents(file, issues);
                }
            }
        } catch (IOException e) {
            issues.add(new ConnectorIssue(KEY, String.format("Could not read file %s", dir.getName())));
        }
    }

}
