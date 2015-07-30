package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.rule.DirectoryStructureRule;
import org.mule.tools.devkit.sonar.rule.DocumentationImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class DirectoryStructureRuleTest {

    @Test
    public void validateSinglePath() throws IOException {
        final Rule.Documentation documentation = new DocumentationImpl("id", "brief", "description", "section");
        final Rule rule = new DirectoryStructureRule(documentation, "README.md$", "README.md");

        final Path childPath = Paths.get("README.md");
        final Path rootPath = TestData.rootPath();
        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        assertTrue("File could not be found.", rule.verify(rootPath, childPath));
    }

    @Test
    public void validatePath() throws IOException {
        final Rule.Documentation documentation = new DocumentationImpl("id", "brief", "description", "section");

        final Rule rule = new DirectoryStructureRule(documentation, "src/test/java", "src/test/java/${CONNECTOR_PACKAGE}/automation/functional/${PROCESSOR}TestCases");
        final Path childPath = Paths.get("src/test/java");
        final Path rootPath = TestData.rootPath();
        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        //  assertTrue("File could not be found.", rule.verify(rootPath, childPath));
    }

}