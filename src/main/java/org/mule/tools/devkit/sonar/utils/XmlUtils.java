package org.mule.tools.devkit.sonar.utils;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class XmlUtils {

    final private static Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final static DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder = null;

    static {
        try {
            domFactory.setNamespaceAware(true);
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

    @NotNull
    public static Object evalXPathOnPom(@NotNull final Path basePath, @NotNull final String xpathExp, @NotNull QName constant) {
        final Path pomXml = basePath.resolve("pom.xml");
        if (!Files.exists(pomXml)) {
            throw new IllegalStateException("Project pom.xml could not be found." + basePath.toAbsolutePath().toString());
        }

        try {
            // Compile xpath expression ...
            final XPath xpath = xpathFactory.newXPath();

            // Set namespace context resolver...
            final PomNamespaceContext context = new PomNamespaceContext();
            xpath.setNamespaceContext(context);

            final XPathExpression expression = xpath.compile(xpathExp);
            final InputStream is = Files.newInputStream(pomXml);
            final Document xmlDocument = builder.parse(is);
            return expression.evaluate(xmlDocument, constant);
        } catch (Exception e) {
            throw new IllegalStateException("Pom could not parsed ->" + basePath.toAbsolutePath().toString(), e);
        }
    }
}
