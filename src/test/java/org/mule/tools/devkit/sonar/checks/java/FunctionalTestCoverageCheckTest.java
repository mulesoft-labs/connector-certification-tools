package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class FunctionalTestCoverageCheckTest {

    @Test
    public void detectedByProcessor() {
        FunctionalTestCoverageCheck check = new FunctionalTestCoverageCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestCoverageCheck.java", check);
    }

    @Test
    public void detectedByTestSuite() {
        FunctionalTestCoverageCheck check = new FunctionalTestCoverageCheck();
        JavaCheckVerifier.verify("src/test/java/org/mule/modules/certification/automation/runner/FunctionalTestSuite.java", check);
    }
}
