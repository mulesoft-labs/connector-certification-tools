package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.mule.tools.devkit.sonar.checks.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.NumberOfArgumentsInProcessorCheck;
import org.mule.tools.devkit.sonar.checks.RefOnlyInComplexTypesCheck;

import java.util.List;

public class JavaChecks {

    public static List<Class> getChecks() {
        return ImmutableList.<Class>of(NumberOfArgumentsInProcessorCheck.class, RefOnlyInComplexTypesCheck.class, LicenseByCategoryCheck.class);
    }

}
