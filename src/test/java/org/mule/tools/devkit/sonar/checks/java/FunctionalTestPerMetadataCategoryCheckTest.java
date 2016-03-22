package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class FunctionalTestPerMetadataCategoryCheckTest {

    @Test
    public void runWithMissingTests() {
        FunctionalTestPerMetadataCategoryCheck check = new FunctionalTestPerMetadataCategoryCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestPerMetaDataCategoryCheckMissingTest.java", check);
    }

    @Test
    public void runWithMisplacedTestFile() {
        FunctionalTestPerMetadataCategoryCheck check = new FunctionalTestPerMetadataCategoryCheck();
        JavaCheckVerifier.verify("src/test/files/java/FunctionalTestPerMetaDataCategoryCheckMisplacedFile.java", check);
    }

}
