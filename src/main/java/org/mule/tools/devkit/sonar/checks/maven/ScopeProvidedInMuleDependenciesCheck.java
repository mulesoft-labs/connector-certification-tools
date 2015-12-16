package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = ScopeProvidedInMuleDependenciesCheck.KEY, name = "Mule dependencies should be declared with <scope>provided</scope> in pom.xml", description = "This rule checks that Mule dependencies (with groupId 'org.mule.*') are declared with <scope>provided</scope> in pom.xml", tags = { "connector-certification" })
public class ScopeProvidedInMuleDependenciesCheck implements MavenCheck {

    public static final String KEY = "mule-scope-provided";

    private static final String ORG_MULE_GROUP_ID = "org.mule.";
    private static final String COM_MULESOFT_GROUP_ID = "com.mulesoft.";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        @SuppressWarnings("unchecked")
        List<Dependency> dependencies = mavenProject.getDependencies();
        if (dependencies != null) {
            for (Dependency dependency : dependencies) {
                if (hasValidGroupId(dependency.getGroupId()) && hasValidScope(dependency.getScope())) {
                    issues.add(new ConnectorIssue(KEY, String.format("Artifact '%s' is a Mule dependency and should be declared with <scope>provided</scope>.",
                            dependency.getArtifactId())));
                }
            }
        }
        return issues;
    }

    private static boolean hasValidGroupId(String groupId) {
        return groupId.startsWith(ORG_MULE_GROUP_ID) || groupId.startsWith(COM_MULESOFT_GROUP_ID);
    }

    private static boolean hasValidScope(String scope) {
        return StringUtils.isEmpty(scope) || scope.equals("compile");
    }
}
