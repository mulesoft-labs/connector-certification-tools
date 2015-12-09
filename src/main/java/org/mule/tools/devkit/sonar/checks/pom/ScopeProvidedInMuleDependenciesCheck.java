package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = ScopeProvidedInMuleDependenciesCheck.KEY, name = "Mule dependencies should be declared with <scope>provided</scope> in pom.xml", description = "This rule checks that Mule dependencies (with groupId 'org.mule.*') are declared with <scope>provided</scope> in pom.xml", tags = { "connector-certification" })
public class ScopeProvidedInMuleDependenciesCheck implements PomCheck {

    public static final String KEY = "mule-scope-provided";

    private static final String ORG_MULE_GROUP_ID = "org.mule.";
    private static final String COM_MULESOFT_GROUP_ID = "com.mulesoft.";

    @Override
    public Iterable<PomIssue> analyze(MavenProject mavenProject) {

        final List<PomIssue> issues = Lists.newArrayList();
        @SuppressWarnings("unchecked")
        List<Dependency> dependencies = mavenProject.getDependencies();
        for (Dependency dependency : dependencies) {
            if (dependency.getGroupId().startsWith(ORG_MULE_GROUP_ID) || dependency.getGroupId().startsWith(COM_MULESOFT_GROUP_ID)) {
                if (StringUtils.isEmpty(dependency.getScope()) || dependency.getScope().equals("compile")) {
                    issues.add(new PomIssue(KEY, String.format("Artifact '%s' is a Mule dependency and should be declared with <scope>provided</scope>.",
                            dependency.getArtifactId())));
                }
            }
        }
        return issues;
    }
}
