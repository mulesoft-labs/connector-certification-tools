package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class FunctionalTestSuiteCheckTest {

    @Test
    public void runWithNoAnnotation() {
        FunctionalTestSuiteCheck check = new FunctionalTestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestSuiteCheckNoAnnotation.java", check);
    }

    @Test
    public void runWithAnnotationNoTestsDeclared() {
        FunctionalTestSuiteCheck check = new FunctionalTestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestSuiteCheckNoTestsDeclared.java", check);
    }

    @Test
    public void runWithAnnotationNoTestCasesSuffix() {
        FunctionalTestSuiteCheck check = new FunctionalTestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestSuiteCheckNoTestCasesSuffix.java", check);
    }

}
