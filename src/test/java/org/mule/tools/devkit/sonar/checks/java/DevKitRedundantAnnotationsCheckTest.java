package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DevKitRedundantAnnotationsCheckTest {

    @Test
    public void detected() {
        JavaCheckVerifier.verify("src/test/files/java/DevKitRedundantAnnotationsCheck.java", new DevKitRedundantAnnotationsCheck());
    }

}
