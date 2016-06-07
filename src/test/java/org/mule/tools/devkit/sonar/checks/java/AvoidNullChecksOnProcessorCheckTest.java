package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class AvoidNullChecksOnProcessorCheckTest {

    @Test
    public void detected() {
        JavaCheckVerifier.verify("src/test/files/java/AvoidNullChecksOnProcessorCheck.java", new AvoidNullChecksOnProcessorCheck());
    }

}
