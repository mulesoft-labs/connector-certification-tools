package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Rule(key = ReleaseNotesExistsCheck.KEY, name = "Release Notes should be present", description = "There should exist a file named 'doc/release-notes.adoc'", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class ReleaseNotesExistsCheck implements StructureCheck {

    public static final String KEY = "release-notes-exists";

    private static final String RELEASE_NOTES_FILE = "release-notes.adoc";
    public static final String DOC_FOLDER = "doc";
    private final FileSystem fileSystem;

    public ReleaseNotesExistsCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        Path path = fileSystem.baseDir().toPath().resolve(DOC_FOLDER).resolve(RELEASE_NOTES_FILE);
        if (!Files.exists(path)) {
            issues.add(new ConnectorIssue(KEY, String.format("File %s is missing.", path.toFile().getName())));
        } else {
            try {
                final long fileSize = Files.size(path);
                if (fileSize < 100) {
                    issues.add(new ConnectorIssue(KEY, String.format("File %s found but doesn't have proper content (size: %s bytes).", path.toFile().getName(), fileSize)));
                }
            } catch (IOException e) {
                issues.add(new ConnectorIssue(KEY, String.format("Could not read file %s", path.toFile().getName())));
            }
        }
        return issues;
    }

}
