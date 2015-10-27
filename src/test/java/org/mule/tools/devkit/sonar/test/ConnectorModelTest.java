package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.ConnectorModelIml;
import org.mule.tools.devkit.sonar.Context;

import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class ConnectorModelTest {

    @Test
    public void testParsing() {
        final Path basePath = TestData.noCompliantTestPath();
        final Path pomPath = basePath.resolve("src/main/java/org/sample/MyConnector.java");

        final Context.ConnectorModel model = new ConnectorModelIml(pomPath);

        // Test package ...
        assertEquals("org.sample", model.getPackage());

    }

}