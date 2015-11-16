package org.mule.tools.devkit.sonar;

import com.google.common.collect.Iterables;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.plugins.java.api.JavaCheck;

/**
 * Declare rule metadata in server repository of rules. That allows to list the rules
 * in the page "Rules".
 */
public class JavaRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "connector-certification";

    @Override
    public void define(Context context) {
        NewRepository repo = context.createRepository(REPOSITORY_KEY, "java");
        repo.setName("Connector Certification");

        // We could use a XML or JSON file to load all rule metadata, but
        // we prefer use annotations in order to have all information in a single place
        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        annotationLoader.load(repo, Iterables.toArray(JavaFileCheckRegistrar.checkClasses(), Class.class));
        repo.done();
    }
}
