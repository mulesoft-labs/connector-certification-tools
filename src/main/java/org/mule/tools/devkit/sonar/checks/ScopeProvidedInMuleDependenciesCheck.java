//package org.mule.tools.devkit.sonar.checks;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.maven.model.Dependency;
//import org.apache.maven.project.MavenProject;
//import org.checkerframework.checker.nullness.qual.NonNull;
//import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.sonar.api.rule.RuleKey;
//import org.sonar.check.Rule;
//import org.sonar.plugins.java.api.tree.ClassTree;
//import org.sonar.plugins.java.api.tree.IdentifierTree;
//
//import java.util.List;
//
//@Rule(key = ScopeProvidedInMuleDependenciesCheck.KEY,
//        name = "Mule dependencies should be declared with <scope>provided</scope> in pom.xml",
//        description = "This rule checks that Mule dependencies (with groupId 'org.mule.*') are declared with <scope>provided</scope> in pom.xml",
//        tags = { "connector-certification" })
//public class ScopeProvidedInMuleDependenciesCheck extends AbstractConnectorPomCheck {
//
//    public static final String KEY = "mule-scope-provided";
//
//    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);
//    private static final String MULE_GROUP_ID_1 = "org.mule.";
//    private static final String MULE_GROUP_ID_2 = "com.mulesoft.";
//
//    private static final Logger logger = LoggerFactory.getLogger(LicenseByCategoryCheck.class);
//
//    private final MavenProject mavenProject;
//
//
//    public ScopeProvidedInMuleDependenciesCheck(MavenProject mavenProject) {
//        this.mavenProject = mavenProject;
//    }
//
//    @Override
//    protected RuleKey getRuleKey() {
//        return RULE_KEY;
//    }
//
//    @Override
//    protected void verifyPom(@NonNull ClassTree classTree) {
//        List<Dependency> dependencies = mavenProject.getDependencies();
//
//        for(Dependency dependency : dependencies){
//            if(dependency.getGroupId().contains(MULE_GROUP_ID_1) || dependency.getGroupId().contains(MULE_GROUP_ID_2)){
//                if(StringUtils.isEmpty(dependency.getScope()) || !dependency.getScope().equals("provided")){
//                    final String message = String.format("Artifact '%s' is a Mule dependency and should be declared with <scope>provided</scope>.", dependency.getArtifactId());
//                    logAndRaiseIssue(classTree, message);
//                }
//            }
//            logger.debug("Parsed dependency artifact -> {}", dependency.getArtifactId());
//        }
//    }
//
//}
