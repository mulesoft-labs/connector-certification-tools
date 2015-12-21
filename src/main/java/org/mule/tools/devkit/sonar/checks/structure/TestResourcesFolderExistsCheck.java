package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Rule(key = TestResourcesFolderExistsCheck.KEY, name = "Test Resources directory must be present", description = "There must exist a directory named 'src/test/resources/'", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class TestResourcesFolderExistsCheck implements StructureCheck {

    public static final String KEY = "test-resources-folder-exists";
    public static final String TEST_FOLDER = "src/test/resources";
    private final FileSystem fileSystem;

    public TestResourcesFolderExistsCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        Path path = fileSystem.baseDir().toPath().resolve(TEST_FOLDER);
        if (!Files.exists(path)) {
            issues.add(new ConnectorIssue(KEY, String.format("Test Resources directory doesn't exist.")));
        }
        return issues;
    }
}
