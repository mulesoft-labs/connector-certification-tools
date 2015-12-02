package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class RestCallDeprecatedCheckTest {

    @Test
    public void detected() {
        RestCallDeprecatedCheck check = new RestCallDeprecatedCheck();

        JavaCheckVerifier.verify("src/test/files/java/RestCallDeprecatedCheck.java", check);
    }
}
