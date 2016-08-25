package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RestCallDeprecationCheckTest {

    @Test
    public void detected() {
        RestCallDeprecationCheck check = new RestCallDeprecationCheck();
        JavaCheckVerifier.verify("src/test/files/java/RestCallDeprecationCheck.java", check);
    }
}
