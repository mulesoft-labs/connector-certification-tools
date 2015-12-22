package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.ImmutableList;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ResourceExistsCheckBase implements StructureCheck {

    private final FileSystem fileSystem;

    public ResourceExistsCheckBase(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public final Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        Path path = fileSystem.baseDir().toPath().resolve(resourcePath());
        if (!Files.exists(path)) {
            return ImmutableList.of(new ConnectorIssue(ruleKey(), String.format("%s doesn't exist.", resourcePath())));
        }
        return ImmutableList.of();
    }

    protected abstract String resourcePath();

    protected abstract String ruleKey();

}
