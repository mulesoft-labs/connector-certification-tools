package org.mule.tools.devkit.sonar;

import org.mule.tools.devkit.sonar.checks.java.BasePackageCheck;
import org.mule.tools.devkit.sonar.checks.java.ConfigFriendlyNameCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestPerProcessorCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestSuiteCheck;
import org.mule.tools.devkit.sonar.checks.java.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.java.NoAssertionsInBeforeOrAfterCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfComplexArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfSimpleAndOptionalArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.PayloadDeprecationCheck;
import org.mule.tools.devkit.sonar.checks.java.RedundantExceptionNameCheck;
import org.mule.tools.devkit.sonar.checks.java.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.checks.java.RestCallDeprecationCheck;
import org.mule.tools.devkit.sonar.checks.java.TestCasesExtendAbstractTestCasesCheck;
import org.mule.tools.devkit.sonar.checks.java.TestSuiteCheck;
import org.mule.tools.devkit.sonar.checks.maven.DistributionManagementByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.maven.ScopeProvidedInMuleDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.maven.SnapshotDependenciesCheck;
import org.mule.tools.devkit.sonar.checks.maven.SourceDeploymentForStandardCategoryCheck;
import org.mule.tools.devkit.sonar.checks.maven.TestingFrameworkNotOverwrittenCheck;
import org.mule.tools.devkit.sonar.checks.structure.DemoExistCheck;
import org.mule.tools.devkit.sonar.checks.structure.IconsExistCheck;
import org.mule.tools.devkit.sonar.checks.structure.LicenseDeclarationFilesCheck;
import org.mule.tools.devkit.sonar.checks.structure.ReadmeExistsCheck;
import org.mule.tools.devkit.sonar.checks.structure.ReleaseNotesExistsCheck;
import org.mule.tools.devkit.sonar.checks.structure.TestResourcesFolderExistsCheck;
import org.mule.tools.devkit.sonar.checks.structure.TestSuiteFoldersExistCheck;
import org.mule.tools.devkit.sonar.checks.structure.UserManualExistsCheck;
import org.sonar.plugins.java.api.JavaCheck;

import com.google.common.collect.ImmutableList;

public class ConnectorsChecks {

    private ConnectorsChecks() {
    }

    public static Iterable<Class<? extends JavaCheck>> javaChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(ConfigFriendlyNameCheck.class);
        builder.add(FunctionalTestPerProcessorCheck.class);
        builder.add(NumberOfComplexArgumentsCheck.class);
        builder.add(NumberOfSimpleAndOptionalArgumentsCheck.class);
        builder.add(PayloadDeprecationCheck.class);
        builder.add(RefOnlyInComplexTypesCheck.class);
        builder.add(LicenseByCategoryCheck.class);
        builder.add(RestCallDeprecationCheck.class);
        builder.add(RedundantExceptionNameCheck.class);
        builder.add(BasePackageCheck.class);
        return builder.build();
    }

    public static Iterable<Class<? extends JavaCheck>> javaTestChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(FunctionalTestSuiteCheck.class);
        builder.add(TestSuiteCheck.class);
        builder.add(TestCasesExtendAbstractTestCasesCheck.class);
        builder.add(NoAssertionsInBeforeOrAfterCheck.class);
        return builder.build();
    }

    public static Iterable<Class<?>> mavenChecks() {
        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        builder.add(DistributionManagementByCategoryCheck.class);
        builder.add(ScopeProvidedInMuleDependenciesCheck.class);
        builder.add(SnapshotDependenciesCheck.class);
        builder.add(SourceDeploymentForStandardCategoryCheck.class);
        builder.add(TestingFrameworkNotOverwrittenCheck.class);
        return builder.build();
    }

    public static Iterable<Class<?>> structureChecks() {
        final ImmutableList.Builder<Class<?>> builder = ImmutableList.builder();
        builder.add(IconsExistCheck.class);
        builder.add(LicenseDeclarationFilesCheck.class);
        builder.add(ReadmeExistsCheck.class);
        builder.add(ReleaseNotesExistsCheck.class);
        builder.add(TestResourcesFolderExistsCheck.class);
        builder.add(TestSuiteFoldersExistCheck.class);
        builder.add(UserManualExistsCheck.class);
        builder.add(DemoExistCheck.class);
        return builder.build();
    }
}
