package org.mule.tools.devkit.sonar.checks.structure;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Rule(key = DemoExistCheck.KEY, name = "Demo directory must be present", description = "There must exist a directory named 'demos, and it should contain at least one directory containing a demo. That demo must have a README.md file describing the demo.'", priority = Priority.CRITICAL, tags = { "connector-certification"
})
public class DemoExistCheck implements StructureCheck {

    public static final String KEY = "demo-exist";

    private static final DirectoryStream.Filter<Path> DIRECTORY_FILTER = new DirectoryStream.Filter<Path>() {

        @Override
        public boolean accept(Path file) throws IOException {
            return (Files.isDirectory(file));
        }
    };

    private final FileSystem fileSystem;

    public DemoExistCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        final Path demosDir = fileSystem.baseDir().toPath().resolve("demo");

        if (!Files.exists(demosDir) || !demosDir.toFile().isDirectory()) {
            issues.add(new ConnectorIssue(KEY, "'demo' directory doesn't exist or it's a file rather than a directory."));
        } else {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(demosDir, DIRECTORY_FILTER)) {
                final List<Path> demos = Lists.newArrayList(Iterables.filter(stream, new Predicate<Path>() {

                    @Override
                    public boolean apply(@Nullable Path input) {
                        return input != null && input.toFile().isDirectory();
                    }
                }));
                if (demos.size() < 1) {
                    issues.add(new ConnectorIssue(KEY, "'demo' directory exists, but contains no demos."));
                } else {
                    for (final Path demoDir : demos) {
                        final Path readmePath = demoDir.resolve("README.md");
                        if (!Files.exists(readmePath)) {
                            issues.add(new ConnectorIssue(KEY, String.format("demo named '%s' is missing a README.md file explaining the purpose of the demo.",
                                    demoDir.getFileName())));
                        }
                    }
                }
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
        return issues;
    }

}
