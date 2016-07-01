package org.mule.tools.devkit.sonar;

import org.mule.tools.devkit.sonar.checks.git.GitLanguage;
import org.mule.tools.devkit.sonar.checks.git.GitSensor;
import org.mule.tools.devkit.sonar.checks.maven.MavenSensor;
import org.mule.tools.devkit.sonar.checks.structure.StructureSensor;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point for all extensions
 */
public final class ConnectorCertificationPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        return Arrays.asList(

                // server extensions -> objects are instantiated during server startup
                ConnectorCertificationRulesDefinition.class,

                // Languages.
                GitLanguage.class, MvnLanguage.class, StructureLanguage.class,

                // batch extensions
                // Java checks
                ConnectorCertificationJavaProfile.class, ConnectorCertificationCheckRegistrar.class,

                // Maven checks
                ConnectorCertificationMvnProfile.class, MavenSensor.class,

                // Structure checks
                ConnectorCertificationStructProfile.class, StructureSensor.class,

                // Git checks
                ConnectorCertificationGitProfile.class, GitSensor.class);
    }
}
