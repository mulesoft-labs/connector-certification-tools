package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.ModuleValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectVerifierTest {

    @Test public void validatePom() throws IOException {
        final Path path = Paths.get("/Users/pveiga/repo/salesforce-connector/");

        final ModuleValidator projectChecker = ModuleValidator.create();
        projectChecker.validator(path);

    }
}
