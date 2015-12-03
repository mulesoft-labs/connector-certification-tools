package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.mule.tools.devkit.sonar.checks.java.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfComplexArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfSimpleAndOptionalArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.RedundantExceptionNameCheck;
import org.mule.tools.devkit.sonar.checks.java.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.checks.java.RestCallDeprecatedCheck;
import org.mule.tools.devkit.sonar.checks.pom.ScopeProvidedInMuleDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.pom.SnapshotDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.pom.SourceDeploymentForStandardCategoryCheck;
import org.mule.tools.devkit.sonar.checks.pom.TestingFrameworkNotOverwrittenCheck;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.Collection;

public class ConnectorsChecks {

    private ConnectorsChecks() {
    }

    public static Collection<Class<? extends JavaCheck>> javaChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(NumberOfComplexArgumentsCheck.class);
        builder.add(NumberOfSimpleAndOptionalArgumentsCheck.class);
        builder.add(RefOnlyInComplexTypesCheck.class);
        builder.add(LicenseByCategoryCheck.class);
        builder.add(RestCallDeprecatedCheck.class);
        builder.add(RedundantExceptionNameCheck.class);
        return builder.build();
    }

    public static Collection<Class<?>> pomChecks() {
        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        builder.add(ScopeProvidedInMuleDependenciesCheck.class);
        builder.add(SnapshotDependenciesCheck.class);
        builder.add(SourceDeploymentForStandardCategoryCheck.class);
        builder.add(TestingFrameworkNotOverwrittenCheck.class);
        return builder.build();
    }

}
