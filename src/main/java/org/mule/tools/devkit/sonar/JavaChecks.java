package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.mule.tools.devkit.sonar.checks.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.NumberOfArgumentsInProcessorCheck;
import org.mule.tools.devkit.sonar.checks.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.checks.RestCallDeprecatedCheck;

import java.util.Collection;

public class JavaChecks {

    private JavaChecks() {
    }

    public static Collection<Class> getChecks() {
        return ImmutableList.<Class> builder().add(NumberOfArgumentsInProcessorCheck.class).add(RefOnlyInComplexTypesCheck.class).add(LicenseByCategoryCheck.class)
                .add(RestCallDeprecatedCheck.class).build();
    }

}
