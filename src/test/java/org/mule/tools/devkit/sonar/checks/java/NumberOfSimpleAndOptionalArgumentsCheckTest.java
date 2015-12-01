package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class NumberOfSimpleAndOptionalArgumentsCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {
        NumberOfSimpleAndOptionalArgumentsCheck check = new NumberOfSimpleAndOptionalArgumentsCheck();
        check.maxArgumentsAllowed = 4;

        SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/NumberOfSimpleAndOptionalArgumentsCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier
                .verify(file.getCheckMessages())
                .next()
                .atLine(20)
                .withMessage(
                        "Processor 'failingMethod' has 6 simple-type parameters marked as @Optional (more than 4, which is the maximum allowed). It's strongly recommended that all optional parameters are grouped inside a separate POJO class.");
    }
}
