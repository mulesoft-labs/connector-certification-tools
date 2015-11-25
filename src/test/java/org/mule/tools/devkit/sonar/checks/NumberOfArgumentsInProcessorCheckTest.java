package org.mule.tools.devkit.sonar.checks;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class NumberOfArgumentsInProcessorCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {
        NumberOfArgumentsInProcessorCheck check = new NumberOfArgumentsInProcessorCheck();
        check.maxArgumentsAllowed = 4;

        SourceFile file = JavaAstScanner
                .scanSingleFile(new File("src/test/files/NumberOfArgumentsInProcessorCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier.verify(file.getCheckMessages())
                .next().atLine(19).withMessage("Processor failingMethod has 5 complex-type parameters (more than 4 which is max allowed)");
    }
}
