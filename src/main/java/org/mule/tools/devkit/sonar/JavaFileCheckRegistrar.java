package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.mule.tools.devkit.sonar.checks.*;

/**
 * Provide the "checks" (implementations of rules) classes that are gonna be executed during
 * source code analysis.
 * <p/>
 * This class is a batch extension by implementing the {@link CheckRegistrar} interface.
 */
public class JavaFileCheckRegistrar implements CheckRegistrar {

    /**
     * Register the classes that will be used to instantiate checks during analysis.
     */
    @Override
    public void register(RegistrarContext registrarContext) {
        // Call to registerClassesForRepository to associate the classes with the correct repository key
        registrarContext.registerClassesForRepository(JavaRulesDefinition.REPOSITORY_KEY, checkClasses(), testCheckClasses());
    }

    /**
     * Lists all the checks provided by the plugin
     */
    public static Iterable<Class<? extends JavaCheck>> checkClasses() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(NumberOfArgumentsInProcessorCheck.class);
        builder.add(LicenseByCategoryCheck.class);
        builder.add(RefOnlyInComplexTypesCheck.class);
        return builder.build();
    }

    /**
     * Lists all the test checks provided by the plugin
     */
    public static ImmutableList<Class<? extends JavaCheck>> testCheckClasses() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        return builder.build();
    }
}
