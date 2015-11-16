package org.mule.tools.devkit.sonar;

import java.util.Arrays;
import java.util.List;

import org.mule.tools.devkit.sonar.batch.*;
import org.mule.tools.devkit.sonar.checks.ProjectContextInitializer;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

/**
 * This class is the entry point for all extensions
 */
@Properties({
        @Property(
                key = CertificationPlugin.MY_PROPERTY,
                name = "Connector Certification Plugin",
                description = "A property for the plugin",
                defaultValue = "Rules for automating the certification of a Connector") })
public final class CertificationPlugin extends SonarPlugin {

    public static final String MY_PROPERTY = "org.mule.tools.devkit.sonar";

    @Override
    public List getExtensions() {
        return Arrays.asList(
                ProjectContextInitializer.class,

                // server extensions -> objects are instantiated during server startup
                JavaRulesDefinition.class,

                // batch extensions -> objects are instantiated during code analysis
                JavaFileCheckRegistrar.class
        );
    }
}
