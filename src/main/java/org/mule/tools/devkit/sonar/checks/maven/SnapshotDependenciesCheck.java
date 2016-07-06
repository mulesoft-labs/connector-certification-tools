package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = SnapshotDependenciesCheck.KEY, name = "SNAPSHOT dependencies are NOT allowed in pom.xml", description = "Checks that no SNAPSHOT versions are declared in pom.xml", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class SnapshotDependenciesCheck implements MavenCheck {

    public static final String KEY = "snapshot-dependencies-not-allowed";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();

        if (!mavenProject.getVersion().endsWith(PomUtils.SNAPSHOT)) {
            Parent parent = mavenProject.getModel().getParent();
            if (parent != null && parent.getVersion().endsWith(PomUtils.SNAPSHOT)) {
                issues.add(new ConnectorIssue(KEY, String.format("Project version is not a snapshot (%s), so it should not inherit from a snapshot version parent (%s:%s).",
                        mavenProject.getVersion(), parent.getArtifactId(), parent.getVersion())));
            }

            if (mavenProject.getDependencies() != null) {
                @SuppressWarnings("unchecked")
                List<Dependency> dependencies = mavenProject.getDependencies();
                if (dependencies != null) {
                    for (Dependency dependency : dependencies) {
                        if (dependency.getVersion().endsWith(PomUtils.SNAPSHOT)) {
                            issues.add(new ConnectorIssue(KEY, String.format("Project version is not a snapshot (%s), so it should not declare any snapshot dependencies (%s:%s).",
                                    mavenProject.getVersion(), dependency.getArtifactId(), dependency.getVersion())));
                        }
                    }
                }
            }
        }

        return issues;
    }

}
