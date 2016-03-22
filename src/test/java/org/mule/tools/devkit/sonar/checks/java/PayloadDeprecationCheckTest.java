package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class PayloadDeprecationCheckTest {

    @Test
    public void detected() {
        PayloadDeprecationCheck check = new PayloadDeprecationCheck();
        JavaCheckVerifier.verify("src/test/files/java/PayloadDeprecationCheck.java", check);
    }
}
