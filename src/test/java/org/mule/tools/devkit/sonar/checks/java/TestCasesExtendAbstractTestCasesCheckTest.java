package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class TestCasesExtendAbstractTestCasesCheckTest {

    @Test
    public void checkNoInheritance() {
        TestCasesExtendAbstractTestCasesCheck check = new TestCasesExtendAbstractTestCasesCheck();
        JavaCheckVerifier.verify("src/test/files/java/test-cases-extend-abstract-test-cases/NoInheritanceTestCases.java", check);
    }

    @Test
    public void checkWrongInheritance() {
        TestCasesExtendAbstractTestCasesCheck check = new TestCasesExtendAbstractTestCasesCheck();
        JavaCheckVerifier.verify("src/test/files/java/test-cases-extend-abstract-test-cases/WrongInheritanceTestCases.java", check);
    }

}
