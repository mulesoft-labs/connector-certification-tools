package org.mule.tools.devkit.sonar.checks.structure;

import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.StructureLanguage;
import org.mule.tools.devkit.sonar.checks.Check;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import static org.mule.tools.devkit.sonar.checks.structure.StructureCheck.REPOSITORY;

@Check(language = StructureLanguage.KEY, repository = REPOSITORY)
public interface StructureCheck {
    String REPOSITORY = "connector-certification-struct";
    Iterable<ConnectorIssue> analyze(MavenProject mavenProject);

}
