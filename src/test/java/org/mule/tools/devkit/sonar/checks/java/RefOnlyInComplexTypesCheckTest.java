package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RefOnlyInComplexTypesCheckTest {

    @Test
    public void detected() {
        RefOnlyInComplexTypesCheck check = new RefOnlyInComplexTypesCheck();

        JavaCheckVerifier.verify("src/test/files/java/RefOnlyInComplexTypesCheck.java", check);
    }

}
