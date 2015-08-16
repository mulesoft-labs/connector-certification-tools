package org.mule.tools.devkit.sonar.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mule.tools.devkit.sonar.Context;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.rule.DirectoryStructureRule;
import org.mule.tools.devkit.sonar.rule.DocumentationImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DirectoryStructureRuleTest {

    private Context context;

    @Before public void setup() {

        final Context.ConnectorModel connectorModel = Mockito.mock(Context.ConnectorModel.class);
        Mockito.when(connectorModel.getProperty(null)).then(var -> Arrays.asList("Processor", "Processor2"));

        this.context = Mockito.mock(Context.class);
        Mockito.when(context.getConnectorModel()).thenReturn(connectorModel);
    }

    @Test public void validateSinglePath() throws IOException {
        final Rule.Documentation documentation = new DocumentationImpl("id", "brief", "description", "section", Rule.Documentation.Severity.CRITICAL);
        final Rule rule = new DirectoryStructureRule(documentation, "README.md$", "README.md");

        final Path childPath = Paths.get("README.md");
        final Path rootPath = TestData.noCompliantTestPath();
        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

        assertTrue("File could not be found.", rule.verify(rootPath, childPath).isEmpty());
    }

    @Test public void validatePath() throws IOException {
        final Rule rule = TestData.findRule("processor_tests");
        final Path rootPath = TestData.noCompliantTestPath();
        final Path childPath = Paths.get("src/");

        assertTrue("File could not be found.", rule.accepts(rootPath, childPath));

    }

    @Test public void permutation() throws IOException {
        // Single line ...
        final List<List<String>> sample1 = Collections.singletonList(Arrays.asList("a1", "a2"));
        final List<List<String>> sample1p = DirectoryStructureRule.permute(sample1, 0);
        assertEquals("[[a1], [a2]]", sample1p.toString());

        final List<List<String>> sample2 = Arrays.asList(Arrays.asList("a1", "a2"), Collections.singletonList("b1"));
        final List<List<String>> sample2p = DirectoryStructureRule.permute(sample2, 0);
        assertEquals("[[a1, b1], [a2, b1]]", sample2p.toString());

        final List<List<String>> sample3 = Arrays.asList(Arrays.asList("a1", "a2"), Arrays.asList("b1", "b2"), Arrays.asList("c1", "c2"));
        final List<List<String>> sample3p = DirectoryStructureRule.permute(sample3, 0);
        assertEquals("[[a1, b1, c1], [a1, b1, c2], [a1, b2, c1], [a1, b2, c2], [a2, b1, c1], [a2, b1, c2], [a2, b2, c1], [a2, b2, c2]]", sample3p.toString());

    }

}