package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class FunctionalTestPerProcessorCheckTest {

    @Test
    public void runWithMissingTests() {
        FunctionalTestPerProcessorCheck check = new FunctionalTestPerProcessorCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestPerProcessorCheckMissingTest.java", check);
    }

    @Test
    public void runWithMisplacedTestFile() {
        FunctionalTestPerProcessorCheck check = new FunctionalTestPerProcessorCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestPerProcessorCheckMisplacedFile.java", check);
    }

}
