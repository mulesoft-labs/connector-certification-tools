package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.mule.tools.devkit.sonar.checks.java.AvoidNullChecksOnProcessorCheck;
import org.mule.tools.devkit.sonar.checks.java.BasePackageCheck;
import org.mule.tools.devkit.sonar.checks.java.DevKitAnnotationsOrderCheck;
import org.mule.tools.devkit.sonar.checks.java.DevKitRedundantAnnotationsCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestPerMetadataCategoryCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestPerProcessorCheck;
import org.mule.tools.devkit.sonar.checks.java.FunctionalTestSuiteCheck;
import org.mule.tools.devkit.sonar.checks.java.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.java.MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck;
import org.mule.tools.devkit.sonar.checks.java.NoAssertionsInBeforeOrAfterCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfComplexArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.NumberOfSimpleAndOptionalArgumentsCheck;
import org.mule.tools.devkit.sonar.checks.java.PayloadDeprecationCheck;
import org.mule.tools.devkit.sonar.checks.java.RedundantExceptionNameCheck;
import org.mule.tools.devkit.sonar.checks.java.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.checks.java.RestCallDeprecationCheck;
import org.mule.tools.devkit.sonar.checks.java.TestCasesExtendAbstractTestCasesCheck;
import org.mule.tools.devkit.sonar.checks.java.TestSuiteCheck;
import org.sonar.plugins.java.api.JavaCheck;

public class ConnectorsChecks {

    private ConnectorsChecks() {
    }

    /**
     * FIXME: Replace using {@link org.reflections.Reflections}.
     */
    @Deprecated
    public static Iterable<Class<? extends JavaCheck>> javaChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(FunctionalTestPerProcessorCheck.class);
        builder.add(FunctionalTestPerMetadataCategoryCheck.class);
        builder.add(AvoidNullChecksOnProcessorCheck.class);
        builder.add(NumberOfComplexArgumentsCheck.class);
        builder.add(NumberOfSimpleAndOptionalArgumentsCheck.class);
        builder.add(PayloadDeprecationCheck.class);
        builder.add(RefOnlyInComplexTypesCheck.class);
        builder.add(LicenseByCategoryCheck.class);
        builder.add(RestCallDeprecationCheck.class);
        builder.add(RedundantExceptionNameCheck.class);
        builder.add(BasePackageCheck.class);
        builder.add(DevKitAnnotationsOrderCheck.class);
        builder.add(DevKitRedundantAnnotationsCheck.class);
        return builder.build();
    }

    /**
     * FIXME: Replace using {@link org.reflections.Reflections}.
     */
    @Deprecated
    public static Iterable<Class<? extends JavaCheck>> javaTestChecks() {
        final ImmutableList.Builder<Class<? extends JavaCheck>> builder = ImmutableList.builder();
        builder.add(FunctionalTestSuiteCheck.class);
        builder.add(TestSuiteCheck.class);
        builder.add(TestCasesExtendAbstractTestCasesCheck.class);
        builder.add(MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck.class);
        builder.add(NoAssertionsInBeforeOrAfterCheck.class);
        return builder.build();
    }
}
