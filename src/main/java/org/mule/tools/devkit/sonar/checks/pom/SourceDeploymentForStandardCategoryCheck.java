package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = SourceDeploymentForStandardCategoryCheck.KEY, name = "Source deployment for Standard connectors", description = "Checks that the source code is not deployed when releasing a new version of a Standard connector. To prevent this, the 'maven-source-plugin' must be added to the pom.xml: "
        + "```xml\n"
        + "<build>\n"
        + "\t<plugins>\n"
        + "\t\t<plugin>\n"
        + "\t\t\t<artifactId>maven-source-plugin</artifactId>\n"
        + "\t\t    <version>2.4</version>\n"
        + "\t\t    <executions>\n"
        + "\t\t    \t<execution>\n"
        + "\t\t        \t<id>attach-sources</id>\n"
        + "\t\t            <phase>none</phase>\n"
        + "\t\t        </execution>\n"
        + "\t\t    </executions>\n"
        + "\t\t</plugin>\n"
        + "\t</plugins>\n"
        + "<build>\n"
        + "```\n\n"
        + "IMPORTANT: since DevKit 3.7.2, the Standard category is no longer supported thus the connector should be migrated to either Premium, Select or Certified.", tags = { "connector-certification" })
public class SourceDeploymentForStandardCategoryCheck implements PomCheck {

    public static final String KEY = "source-deployment-for-standard-category";
    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);
    private static final String SOURCE_PLUGIN_GROUP_ID = "org.apache.maven.plugins";
    private static final String SOURCE_PLUGIN_ARTIFACT_ID = "maven-source-plugin";
    private static final Logger logger = LoggerFactory.getLogger(SourceDeploymentForStandardCategoryCheck.class);

    public static final Predicate<Dependency> HAS_SOURCE_PLUGIN = new Predicate<Dependency>() {

        @Override
        public boolean apply(@Nullable Dependency input) {
            return input != null && input.getGroupId().equals(SOURCE_PLUGIN_GROUP_ID) && input.getArtifactId().equals(SOURCE_PLUGIN_ARTIFACT_ID);
        }
    };

    @Override
    public Iterable<PomIssue> analyze(MavenProject mavenProject) {
        final List<PomIssue> issues = Lists.newArrayList();

        String category = mavenProject.getProperties().getProperty("category");
        logger.debug("Parsed Category version -> {}", category);

        final boolean hasSourcePlugin = Iterables.any(mavenProject.getDependencies(), HAS_SOURCE_PLUGIN);

        if (category.toUpperCase().equals("STANDARD") && !hasSourcePlugin) {
            issues.add(new PomIssue(RULE_KEY, String.format("Standard connectors must declare a 'maven-source-plugin' in pom.xml to prevent the deployment of its sources.")));
        }

        return issues;
    }
}
