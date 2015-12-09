package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestPerProcessorCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestSuiteCheck;
import org.mule.tools.devkit.sonar.checks.java.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfComplexArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfSimpleAndOptionalArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.RedundantExceptionNameCheck;
import org.mule.tools.devkit.sonar.checks.java.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.checks.java.RestCallDeprecatedCheck;
import org.mule.tools.devkit.sonar.checks.java.TestSuiteCheck;
import org.mule.tools.devkit.sonar.checks.pom.ScopeProvidedInMuleDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.pom.SnapshotDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.pom.SourceDeploymentForStandardCategoryCheck;
import org.mule.tools.devkit.sonar.checks.pom.TestingFrameworkNotOverwrittenCheck;
import org.sonar.plugins.java.api.JavaCheck;

public class ConnectorsChecks {

    private ConnectorsChecks() {
    }

    public static Iterable<Class<? extends JavaCheck>> javaChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(FunctionalTestPerProcessorCheck.class);
        builder.add(FunctionalTestSuiteCheck.class);
        builder.add(NumberOfComplexArgumentsCheck.class);
        builder.add(NumberOfSimpleAndOptionalArgumentsCheck.class);
        builder.add(RefOnlyInComplexTypesCheck.class);
        builder.add(LicenseByCategoryCheck.class);
        builder.add(RestCallDeprecatedCheck.class);
        builder.add(RedundantExceptionNameCheck.class);
        builder.add(TestSuiteCheck.class);
        return builder.build();
    }

    public static Iterable<Class<?>> pomChecks() {
        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        builder.add(ScopeProvidedInMuleDependenciesCheck.class);
        builder.add(SnapshotDependenciesCheck.class);
        builder.add(SourceDeploymentForStandardCategoryCheck.class);
        builder.add(TestingFrameworkNotOverwrittenCheck.class);
        return builder.build();
    }

}
