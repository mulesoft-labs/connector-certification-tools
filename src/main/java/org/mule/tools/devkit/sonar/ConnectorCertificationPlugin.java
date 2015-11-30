package org.mule.tools.devkit.sonar;

import org.mule.tools.devkit.sonar.checks.AbstractConnectorPomCheck;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the entry point for all extensions
 */
@Properties({
        @Property(
                key = ConnectorCertificationPlugin.MY_PROPERTY,
                name = "Connector Certification Plugin",
                description = "A property for the plugin",
                defaultValue = "Rules for automating the certification of a Connector") })
public final class ConnectorCertificationPlugin extends SonarPlugin {

    public static final String MY_PROPERTY = "org.mule.tools.devkit.sonar";

    @Override
    public List getExtensions() {
        return Arrays.asList(

                ConnectorCertificationProfile.class,

                // Batch extension -> objects instantiated during Maven sonar:sonar run
                ConnectorCertificationCheckRegistrar.class,

                // server extensions -> objects are instantiated during server startup
                ConnectorCertificationRulesDefinition.class,

                AbstractConnectorPomCheck.class


        );
    }
}
