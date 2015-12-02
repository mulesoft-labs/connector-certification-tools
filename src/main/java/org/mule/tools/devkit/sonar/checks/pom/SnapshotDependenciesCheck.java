package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Parent;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = SnapshotDependenciesCheck.KEY, name = "SNAPSHOT dependencies are NOT allowed in pom.xml", description = "Checks that no SNAPSHOT versions are declared in pom.xml", tags = { "connector-certification" })
public class SnapshotDependenciesCheck implements PomCheck {

    public static final String KEY = "snapshot-dependencies-not-allowed";

    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);
    private static final String SNAPSHOT = "SNAPSHOT";

    @Override
    public Iterable<PomIssue> analyze(MavenProject mavenProject) {
        final List<PomIssue> issues = Lists.newArrayList();
        List<Dependency> dependencies = mavenProject.getDependencies();

        if (mavenProject.getVersion().endsWith(SNAPSHOT)) {
            issues.add(new PomIssue(RULE_KEY, buildMessage(mavenProject.getArtifactId())));
        }

        Parent parent = (mavenProject.getModel()).getParent();
        if (parent != null && parent.getVersion().endsWith(SNAPSHOT)) {
            issues.add(new PomIssue(RULE_KEY, buildMessage(parent.getArtifactId())));
        }

        for (Dependency dependency : dependencies) {
            if (dependency.getVersion().endsWith(SNAPSHOT)) {
                issues.add(new PomIssue(RULE_KEY, buildMessage(dependency.getArtifactId())));
            }
        }
        return issues;
    }

    public String buildMessage(String arg) {
        return String.format("Remove SNAPSHOT version from artifact '%s'.", arg);
    }
}
