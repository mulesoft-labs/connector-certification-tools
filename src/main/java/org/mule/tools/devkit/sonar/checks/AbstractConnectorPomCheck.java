package org.mule.tools.devkit.sonar.checks;

import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;

public class AbstractConnectorPomCheck implements Sensor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnectorClassCheck.class);

    public static final String KEY = "license-by-category";
    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);

    protected RuleKey getRuleKey(){
        return RULE_KEY;
    }

    private final MavenProject mavenProject;
    private final ResourcePerspectives resourcePerspectives;

    public AbstractConnectorPomCheck(MavenProject mavenProject, ResourcePerspectives resourcePerspectives) {
        this.mavenProject = mavenProject;
        this.resourcePerspectives = resourcePerspectives;

    }

    protected void verifyPom(@NonNull Project project, @NonNull SensorContext sensorContext){}

    protected void logAndRaiseIssue(String message, SensorContext sensorContext) {
        logger.info(message);

    }

     @Override
     public void analyse(Project project, SensorContext sensorContext) {

         File resource = org.sonar.api.resources.File.fromIOFile(new java.io.File("pom.xml"), project);

         Issuable issuable = resourcePerspectives.as(Issuable.class,resource);
         if (issuable != null) {
             // can be used
             Issue issue = issuable.newIssueBuilder()
                     //repository : pmd, key : AvoidArrayLoops
                     .ruleKey(getRuleKey())
                     .line(10)
                     .message("Lallalala")
                     .build();
             //works
             issuable.addIssue(issue);

         }
     }

     @Override
     public boolean shouldExecuteOnProject(Project project) {
         return true;
     }
 }
