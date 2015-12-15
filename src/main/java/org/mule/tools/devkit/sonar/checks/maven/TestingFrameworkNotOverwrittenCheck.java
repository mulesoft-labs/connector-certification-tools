package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = TestingFrameworkNotOverwrittenCheck.KEY, name = "Connector Testing Framework (CTF) version must not be overwritten", description = "This rule checks that the Connector Testing Framework (CTF) dependency is not overwritten in pom.xml (it is inherited from DevKit's Parent POM", tags = { "connector-certification" })
public class TestingFrameworkNotOverwrittenCheck implements MavenCheck {

    public static final String KEY = "testing-framework-not-overwritten";

    private static final String CTF_GROUP_ID = "org.mule.tools.devkit";
    private static final String CTF_ARTIFACT_ID = "connector-testing-framework";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        @SuppressWarnings("unchecked")
        List<Dependency> dependencies = mavenProject.getDependencies();
        for (Dependency dependency : dependencies) {
            if (dependency.getGroupId().equals(CTF_GROUP_ID) && dependency.getArtifactId().startsWith(CTF_ARTIFACT_ID)) {
                issues.add(new ConnectorIssue(KEY, String.format("'%s' must not be overwritten.", dependency.getArtifactId())));
            }
        }
        return issues;
    }

}
