package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class NumberOfComplexArgumentsCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {
        NumberOfComplexArgumentsCheck check = new NumberOfComplexArgumentsCheck();
        check.maxArgumentsAllowed = 4;

        SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/java/NumberOfComplexArgumentsCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier.verify(file.getCheckMessages()).next().atLine(20)
                .withMessage("Processor 'failingMethod' has 5 complex-type parameters (more than 4, which is the maximum allowed).");
    }
}
