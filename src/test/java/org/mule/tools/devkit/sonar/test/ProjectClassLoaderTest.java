package org.mule.tools.devkit.sonar.test;

import org.junit.Before;
import org.junit.Test;
import org.mule.tools.devkit.sonar.ProjectClassLoader;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;

public class ProjectClassLoaderTest {

    @Before public void setup() {

    }

    @Test public void testDependencies() throws IOException, XPathExpressionException, SAXException {

        final Path path = TestData.noCompliantTestPath();
        final ProjectClassLoader classLoader = new ProjectClassLoader(path);

    }
}
