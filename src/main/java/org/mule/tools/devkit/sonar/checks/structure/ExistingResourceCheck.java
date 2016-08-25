package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.ImmutableList;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ExistingResourceCheck implements StructureCheck {

    private final FileSystem fileSystem;

    public ExistingResourceCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        Path path = fileSystem.baseDir().toPath().resolve(resourcePath());
        if (!Files.exists(path)) {
            return ImmutableList.of(new ConnectorIssue(ruleKey(), String.format("%s doesn't exist.", resourcePath())));
        }
        return ImmutableList.of();
    }

    protected abstract String resourcePath();

    protected abstract String ruleKey();

    protected FileSystem getFileSystem() {
        return fileSystem;
    }
}
