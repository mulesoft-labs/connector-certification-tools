package org.mule.tools.devkit.sonar.checks;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class RefOnlyInComplexTypesCheckTest {

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void detected() {

        // Use an instance of the check under test to raise the issue.
        RefOnlyInComplexTypesCheck check = new RefOnlyInComplexTypesCheck();

        SourceFile file = JavaAstScanner.scanSingleFile(new File("src/test/files/RefOnlyInComplexTypesCheck.java"), new VisitorsBridge(check));

        checkMessagesVerifier.verify(file.getCheckMessages()).next().atLine(8)
                .withMessage("Processor 'failingMethod' contains variable 's1' of type 'SomeComplexType' (complex type) not annotated with @RefOnly.");

    }

}
