package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class NoAssertionsInBeforeOrAfterCheckTest {

    @Test
    public void checkNoAssertionsInBeforeOrAfter() {
        NoAssertionsInBeforeOrAfterCheck check = new NoAssertionsInBeforeOrAfterCheck();
        JavaCheckVerifier.verify("src/test/files/java/NoAssertionsInBeforeOrAfterCheckTest.java", check);
    }

}
