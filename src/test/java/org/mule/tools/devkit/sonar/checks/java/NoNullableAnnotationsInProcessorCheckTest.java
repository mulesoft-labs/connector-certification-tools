package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class NoNullableAnnotationsInProcessorCheckTest {

    @Test
    public void detected() {
        NoNullAnnotationsInProcessorCheck check = new NoNullAnnotationsInProcessorCheck();
        JavaCheckVerifier.verify("src/test/files/java/NoNullAnnotationsInProcessorCheck.java", check);
    }

}
