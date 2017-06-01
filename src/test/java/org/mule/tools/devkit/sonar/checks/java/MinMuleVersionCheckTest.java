package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;

import static org.sonar.java.checks.verifier.JavaCheckVerifier.verify;
import static org.sonar.java.checks.verifier.JavaCheckVerifier.verifyNoIssue;

public class MinMuleVersionCheckTest {

    @Test
    public void testValidMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        verifyNoIssue("src/test/files/java/MinMuleVersionCheckIsValid.java", check);
    }

    @Test
    public void testEmptyMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        verify("src/test/files/java/MinMuleVersionCheckIsEmpty.java", check);
    }

    @Test
    public void testInvalidMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        verify("src/test/files/java/MinMuleVersionCheckIsInvalid.java", check);
    }

    @Test
    public void testMissingMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        verify("src/test/files/java/MinMuleVersionCheckIsMissing.java", check);
    }

    @Test
    public void testNoAttributesMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        verify("src/test/files/java/MinMuleVersionCheckNoAttributes.java", check);
    }

}
