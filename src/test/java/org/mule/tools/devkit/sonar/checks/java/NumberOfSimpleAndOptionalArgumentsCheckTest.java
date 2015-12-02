package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class NumberOfSimpleAndOptionalArgumentsCheckTest {

    @Test
    public void detected() {
        NumberOfSimpleAndOptionalArgumentsCheck check = new NumberOfSimpleAndOptionalArgumentsCheck();
        check.maxArgumentsAllowed = 4;

        JavaCheckVerifier.verify("src/test/files/java/NumberOfSimpleAndOptionalArgumentsCheck.java", check);
    }
}
