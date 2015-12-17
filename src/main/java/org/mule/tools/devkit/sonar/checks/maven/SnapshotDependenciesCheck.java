package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = SnapshotDependenciesCheck.KEY, name = "SNAPSHOT dependencies are NOT allowed in pom.xml", description = "Checks that no SNAPSHOT versions are declared in pom.xml", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class SnapshotDependenciesCheck implements MavenCheck {

    public static final String KEY = "snapshot-dependencies-not-allowed";

    private static final String SNAPSHOT = "SNAPSHOT";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();

        if (mavenProject.getVersion().endsWith(SNAPSHOT)) {
            issues.add(new ConnectorIssue(KEY, buildMessage(mavenProject.getArtifactId())));
        }

        Parent parent = mavenProject.getModel().getParent();
        if (parent != null && parent.getVersion().endsWith(SNAPSHOT)) {
            issues.add(new ConnectorIssue(KEY, buildMessage(parent.getArtifactId())));
        }

        List<Dependency> dependencies = mavenProject.getDependencies();
        if (dependencies != null) {
            for (Dependency dependency : dependencies) {
                if (dependency.getVersion().endsWith(SNAPSHOT)) {
                    issues.add(new ConnectorIssue(KEY, buildMessage(dependency.getArtifactId())));
                }
            }
        }
        return issues;
    }

    public String buildMessage(String arg) {
        return String.format("Remove SNAPSHOT version from artifact '%s'.", arg);
    }
}
