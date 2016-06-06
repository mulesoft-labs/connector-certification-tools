package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class DevKitAnnotationsOrderCheckTest {

    @Test
    public void detected() {
        DevKitAnnotationsOrderCheck check = new DevKitAnnotationsOrderCheck();
        JavaCheckVerifier.verify("src/test/files/java/DevKitAnnotationsOrderCheck.java", check);
    }

}
