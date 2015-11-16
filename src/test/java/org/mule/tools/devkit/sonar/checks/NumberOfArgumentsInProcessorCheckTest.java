package org.mule.tools.devkit.sonar.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class NumberOfArgumentsInProcessorCheckTest {

    @Test
    public void detected() {

        // Use an instance of the check under test to raise the issue.
        NumberOfArgumentsInProcessorCheck check = new NumberOfArgumentsInProcessorCheck();
        check.maxArgumentsAllowed = 4;

        // Verifies that the check will raise the adequate issues with the expected message.
        // In the test file, lines which should raise an issue have been commented out
        // by using the following syntax: "// Noncompliant {{EXPECTED_MESSAGE}}"
        JavaCheckVerifier.verify("src/test/files/NumberOfArgumentsInProcessorCheck.java", check);
    }
}
