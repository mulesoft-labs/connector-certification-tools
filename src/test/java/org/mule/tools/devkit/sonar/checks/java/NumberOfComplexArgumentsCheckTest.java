package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class NumberOfComplexArgumentsCheckTest {

    @Test
    public void detected() {
        NumberOfComplexArgumentsCheck check = new NumberOfComplexArgumentsCheck();
        check.maxArgumentsAllowed = 4;

        JavaCheckVerifier.verify("src/test/files/java/NumberOfComplexArgumentsCheck.java", check);
    }
}
