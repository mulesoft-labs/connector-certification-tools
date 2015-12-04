package org.mule.tools.devkit.sonar;

import org.mule.tools.devkit.sonar.checks.pom.ConnectorPomCheck;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point for all extensions
 */
@Properties({ @Property(key = ConnectorCertificationPlugin.MY_PROPERTY, name = "Connector Certification Plugin", description = "A property for the plugin", defaultValue = "Rules for automating the certification of a Connector") })
public final class ConnectorCertificationPlugin extends SonarPlugin {

    public static final String MY_PROPERTY = "org.mule.tools.devkit.sonar";

    @Override
    public List getExtensions() {
        return Arrays.asList(

        // server extensions -> objects are instantiated during server startup
                ConnectorCertificationRulesDefinition.class, MvnLanguage.class,

                // Java checks
                ConnectorCertificationProfile.class, ConnectorCertificationCheckRegistrar.class,

                // Maven checks
                ConnectorCertificationMvnProfile.class, ConnectorPomCheck.class);
    }
}
