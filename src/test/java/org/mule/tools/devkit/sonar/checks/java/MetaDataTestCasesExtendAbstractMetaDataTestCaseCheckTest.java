package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class MetaDataTestCasesExtendAbstractMetaDataTestCaseCheckTest {

    @Test
    public void checkNoInheritance() {
        MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck check = new MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck();
        JavaCheckVerifier.verify("src/test/files/java/metadata-test-cases-extend-abstract-metadata-test-case/NoInheritanceTestCases.java", check);
    }

    @Test
    public void checkWrongInheritance() {
        MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck check = new MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck();
        JavaCheckVerifier.verify("src/test/files/java/metadata-test-cases-extend-abstract-metadata-test-case/WrongInheritanceTestCases.java", check);
    }

}
