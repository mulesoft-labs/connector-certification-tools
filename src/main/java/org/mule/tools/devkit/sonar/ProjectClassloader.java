package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ProjectClassLoader extends URLClassLoader {

    final private static Logger logger = LoggerFactory.getLogger(ProjectClassLoader.class);

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final static DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder = null;

    static {
        try {
            domFactory.setNamespaceAware(true);
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public ProjectClassLoader(final @NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        super(initUrls(basePath), null);
    }

    private static URL[] initUrls(@NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        final List<URL> result = new ArrayList<>();

        // Add maven module target dir ...
        final Path targetPath = basePath.resolve("target/classes/");
        if (!Files.exists(targetPath)) {
            throw new IllegalStateException("Maven target directory could not be found." + targetPath.toAbsolutePath().toString());
        }
        result.add(targetPath.toUri().toURL());

        // Add maven
        final Path pomXml = basePath.resolve("pom.xml");
        if (!Files.exists(pomXml)) {
            throw new IllegalStateException("Project pom.xml could not be found." + targetPath.toAbsolutePath().toString());
        }

        // Compile xpath expression ...
        final XPath xpath = xpathFactory.newXPath();

        // Set namespace context resolver...
        final PomNamespaceContext context = new PomNamespaceContext();
        xpath.setNamespaceContext(context);

        final XPathExpression expression = xpath.compile("/project/dependencies/dependency");
        final InputStream is = Files.newInputStream(pomXml);
        final Document xmlDocument = builder.parse(is);
        final NodeList dependencies = (NodeList) expression.evaluate(xmlDocument, XPathConstants.NODESET);

        System.out.println(dependencies.getLength());

        return result.toArray(new URL[result.size()]);
    }

}
