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

@Rule(key = ReadmeExistsCheck.KEY, name = "README.md should be present", description = "There should exist a file named 'README.md' in root folder", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class ReadmeExistsCheck implements StructureCheck {

    public static final String KEY = "readme-exists";

    private static final String RELEASE_NOTES_FILE = "README.md";
    private final FileSystem fileSystem;

    public ReadmeExistsCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        Path path = fileSystem.baseDir().toPath().resolve(RELEASE_NOTES_FILE);
        if (!Files.exists(path)) {
            issues.add(new ConnectorIssue(KEY, String.format("File %s is missing.", path.toFile().getName())));
        }
        return issues;
    }
}
