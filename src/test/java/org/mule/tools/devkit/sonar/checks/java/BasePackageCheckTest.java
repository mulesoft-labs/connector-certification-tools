package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class BasePackageCheckTest {

    @Test
    public void checkPackageInModule() {
        BasePackageCheck check = new BasePackageCheck();
        JavaCheckVerifier.verify("src/test/files/java/base-package/BasePackageInModule.java", check);
    }

    @Test
    public void checkPackageInAny() {
        BasePackageCheck check = new BasePackageCheck();
        JavaCheckVerifier.verify("src/test/files/java/base-package/BasePackageInAny.java", check);
    }

}
