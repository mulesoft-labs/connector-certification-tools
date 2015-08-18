package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PomRuleTest {

    @Test public void testFrameworkOverwrite() throws IOException {
        final Rule rule = TestData.findRule("ctf_version_overwritten");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("pom.xml");

        assertTrue("File was not accepted.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(1, verify.size());
    }

    @Test public void testFrameworkOverwriteOk() throws IOException {
        final Rule rule = TestData.findRule("ctf_version_overwritten");
        final Path rootPath = TestData.compliantTestPath();
        final Path childPath = Paths.get("pom.xml");

        assertTrue("File was not accepted.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(0, verify.size());
    }

    @Test public void testDistributionManagementOk() throws IOException {
        final Rule rule = TestData.findRule("dist_management_premium");
        final Path rootPath = TestData.compliantTestPath();
        final Path childPath = Paths.get("pom.xml");

        assertTrue("File was not accepted.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(0, verify.size());
    }

    @Test public void testDevKitVersionOk() throws IOException {
        final Rule rule = TestData.findRule("devkit_version");
        final Path rootPath = TestData.compliantTestPath();
        final Path childPath = Paths.get("pom.xml");

        assertTrue("File was not accepted.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(0, verify.size());
    }

    @Test public void testDevKitVersionWrong() throws IOException {
        final Rule rule = TestData.findRule("devkit_version");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("pom.xml");

        assertTrue("File was not accepted.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(1, verify.size());
    }

}