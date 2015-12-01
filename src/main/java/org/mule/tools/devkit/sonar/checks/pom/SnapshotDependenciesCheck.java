package org.mule.tools.devkit.sonar.checks.pom;

import org.apache.maven.project.MavenProject;
import org.sonar.check.Rule;

@Rule(key = ScopeProvidedInMuleDependenciesCheck.KEY, name = "Mule dependencies should be declared with <scope>provided</scope> in pom.xml", description = "This rule checks that Mule dependencies (with groupId 'org.mule.*') are declared with <scope>provided</scope> in pom.xml", tags = { "connector-certification" })
public class SnapshotDependenciesCheck implements PomCheck {

    @Override
    public Iterable<PomIssue> analyze(MavenProject mavenProject) {
        return null;
    }
}
