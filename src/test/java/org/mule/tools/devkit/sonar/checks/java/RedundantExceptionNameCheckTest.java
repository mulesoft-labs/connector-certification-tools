package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RedundantExceptionNameCheckTest {

    @Test
    public void detected() {
        RedundantExceptionNameCheck check = new RedundantExceptionNameCheck();

        JavaCheckVerifier.verify("src/test/files/java/RedundantExceptionNameCheck.java", check);
    }
}
