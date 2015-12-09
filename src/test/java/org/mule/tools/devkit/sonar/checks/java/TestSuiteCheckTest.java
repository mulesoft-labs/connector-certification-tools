package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

public class TestSuiteCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void noRunWithAnnotation() {
        TestSuiteCheck check = new TestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/TestSuiteCheckNoAnnotation.java", check);
    }

    @Test
    public void runWithAnnotationNoRunnerClass() {
        TestSuiteCheck check = new TestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/TestSuiteCheckNoRunnerClass.java", check);
    }

    @Test
    public void runWithAnnotationOtherRunnerClass() {
        TestSuiteCheck check = new TestSuiteCheck();
        JavaCheckVerifier.verify("src/test/files/java/TestSuiteCheckOtherRunnerClass.java", check);
    }
}
