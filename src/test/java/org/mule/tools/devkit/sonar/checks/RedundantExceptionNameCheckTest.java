package org.mule.tools.devkit.sonar.checks;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class RedundantExceptionNameCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {

        // Use an instance of the check under test to raise the issue.
        RedundantExceptionNameCheck check = new RedundantExceptionNameCheck();

        SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/RedundantExceptionNameCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier.verify(file.getCheckMessages()).next().atLine(5)
                .withMessage("Exception 'RedundantExceptionNameCheckConnectorException' in processor 'failingMethod' should be renamed to 'RedundantExceptionNameCheckException'.");

    }
}
