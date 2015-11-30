package org.mule.tools.devkit.sonar.checks;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;

import java.util.List;

@Rule(key = TestingFrameworkNotOverwrittenCheck.KEY,
        name = "Connector Testing Framework (CTF) version must not be overwritten",
        description = "This rule checks that the Connector Testing Framework (CTF) dependency is not overwritten in pom.xml (it is inherited from DevKit's Parent POM",
        tags = { "connector-certification" })
public class TestingFrameworkNotOverwrittenCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "testing-framework-not-overwritten";

    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);
    private static final String CTF_GROUP_ID = "org.mule.tools.devkit";
    private static final String CTF_ARTIFACT_ID = "connector-testing-framework";

    private static final Logger logger = LoggerFactory.getLogger(LicenseByCategoryCheck.class);

    private final MavenProject mavenProject;

    public TestingFrameworkNotOverwrittenCheck(MavenProject mavenProject) {
        this.mavenProject = mavenProject;
    }

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    @Override
    protected void verifyConnector(@NonNull ClassTree classTree, @NonNull IdentifierTree connectorAnnotation) {
        List<Dependency> dependencies = mavenProject.getDependencies();

        for(Dependency dependency : dependencies){
            if(dependency.getGroupId().equals(CTF_GROUP_ID) && dependency.getArtifactId().contains(CTF_ARTIFACT_ID)){
                final String message = String.format("'%s' must not be overwritten.", dependency.getArtifactId());
                logAndRaiseIssue(classTree, message);
            }
            logger.debug("Parsed dependency artifact -> {}", dependency.getArtifactId());
        }
    }

}
