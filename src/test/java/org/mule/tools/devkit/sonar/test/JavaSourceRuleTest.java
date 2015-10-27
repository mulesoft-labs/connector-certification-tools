package org.mule.tools.devkit.sonar.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.tools.devkit.sonar.Context;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JavaSourceRuleTest {

    @Before
    public void setup() {

        final Context.ConnectorModel connectorModel = Mockito.mock(Context.ConnectorModel.class);
        Mockito.when(connectorModel.getProperty(null)).then(var -> Arrays.asList("Processor", "Processor2"));

        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getConnectorModel()).thenReturn(connectorModel);
    }

    @Test
    public void testProcessDefaultPayloadVerifier() throws IOException {

        final Rule rule = TestData.findRule("default_payload");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(9, verify.size());
    }

    @Test
    public void testProcessParametersVerifier() throws IOException {
        final Rule rule = TestData.findRule("processor_params");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(2, verify.size());
    }

    @Test
    public void testRefOnlyParametersVerifier() throws IOException {
        final Rule rule = TestData.findRule("processor_params_readonly");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(9, verify.size());
    }

    @Test
    public void testCategoryVerifier() throws IOException {
        final Rule rule = TestData.findRule("connector_category");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(2, verify.size());
    }

    @Test
    public void testCategoryVerifierOk() throws IOException {
        final Rule rule = TestData.findRule("connector_category");
        final Path rootPath = TestData.compliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        assertEquals(0, verify.size());
    }

    @Test
    public void testMetadataCategory() throws IOException {
        final Rule rule = TestData.findRule("metadata_category");
        final Path rootPath = TestData.compliantTestPath();
        final Path childPath = Paths.get("src/main/java/org/sample/MyConnector.java");

        // @Todo: Complete...
        assertTrue("File could not be found.", !rule.accepts(rootPath, childPath));

        // final Set<ValidationError> verify = rule.verify(rootPath, childPath);
        // assertEquals(0, verify.size());
    }

}