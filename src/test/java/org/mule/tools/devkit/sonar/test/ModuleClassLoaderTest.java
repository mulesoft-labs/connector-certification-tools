package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.ModuleClassLoader;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class ModuleClassLoaderTest {

    @Test public void testDependencies() throws IOException, XPathExpressionException, SAXException {

        final Path path = TestData.noCompliantTestPath();
        final ModuleClassLoader classLoader = new ModuleClassLoader(path);
        final URL[] urls = classLoader.getURLs();

        assertEquals(5, urls.length);

    }
}
