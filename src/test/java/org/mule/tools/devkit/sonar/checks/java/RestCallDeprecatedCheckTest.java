package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class RestCallDeprecatedCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {

        // Use an instance of the check under test to raise the issue.
        RestCallDeprecatedCheck check = new RestCallDeprecatedCheck();

        SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/java/RestCallDeprecatedCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier.verify(file.getCheckMessages()).next().atLine(6).withMessage("@RestCall should be removed from processor 'failingMethod' as it has been deprecated.");

    }
}
