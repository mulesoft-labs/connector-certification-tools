package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class MinMuleVersionCheckTest {

    @Test
    public void validMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        JavaCheckVerifier.verifyNoIssue("src/test/files/java/MinMuleVersionValidConnector.java", check);
    }

    @Test
    public void invalidMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        JavaCheckVerifier.verify("src/test/files/java/MinMuleVersionInvalidConnector.java", check);
    }

    @Test
    public void invalidTypeMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        JavaCheckVerifier.verify("src/test/files/java/MinMuleVersionInvalidTypeConnector.java", check);
    }

    @Test
    public void emptyMinMuleVersionConnector() {
        MinMuleVersionCheck check = new MinMuleVersionCheck();
        JavaCheckVerifier.verify("src/test/files/java/MinMuleVersionEmptyConnector.java", check);
    }

}
