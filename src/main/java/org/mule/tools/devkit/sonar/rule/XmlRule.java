package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class XmlRule extends AbstractRule {

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final XPathExpression xpath;
    final private static Logger logger = LoggerFactory.getLogger(AbstractRule.class);

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

    public XmlRule(final Rule.Documentation documentation, @NonNull String acceptRegexp,@NonNull final String verifyExpression) {
        super(documentation, acceptRegexp);
        //@Todo: Load section...

        // Compile xpath expression ...
        final XPath xpath = xpathFactory.newXPath();

        // Set namespace context resolver...
        final Optional<NamespaceContext> context = createXPathNsContext();
        if (context.isPresent()) {
            xpath.setNamespaceContext(context.get());
        }

        try {
            this.xpath = xpath.compile(verifyExpression);
        } catch (XPathExpressionException e) {
            throw new DevKitSonarRuntimeException(e);
        }
    }

    protected Optional<NamespaceContext> createXPathNsContext() {
        return Optional.empty();
    }

    @Override
    public boolean verify(@NonNull Path rootPath, @NonNull final Path childPath) throws DevKitSonarRuntimeException {

        boolean result;
        try {
            final InputStream is = Files.newInputStream(childPath);
            final Document xmlDocument = builder.parse(is);
            result = !(Boolean) xpath.evaluate(xmlDocument, XPathConstants.BOOLEAN);

        } catch (SAXException | XPathExpressionException | IOException e) {
            throw new DevKitSonarRuntimeException(e);
        }
        logger.debug("Rule {} applied to {} -> {}", this.getDocumentation().getId(), childPath.toString(), result);

        return result;
    }

    @Override
    public String toString() {
        return "XPathRule{} " + super.toString();
    }
}

class NamespaceContextImpl implements NamespaceContext {

    private final Map<String, String> namespaces;
    private final String defaultNamespaceURI;

    public NamespaceContextImpl(String defaultNamespaceURI, Map<String, String> namespaces) {
        this.defaultNamespaceURI = defaultNamespaceURI;
        this.namespaces = namespaces;
    }

    public Iterator getPrefixes(String namespaceURI) {
        throw new IllegalStateException("Not Implemented.");
    }

    public String getPrefix(String namespaceURI) {
        throw new IllegalStateException("Not Implemented.");
    }

    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        if (prefix.equals(XMLConstants.XMLNS_ATTRIBUTE)) {
            return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
        }
        if (prefix.equals(XMLConstants.XML_NS_PREFIX)) {
            return XMLConstants.XML_NS_URI;
        }
        if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
            return defaultNamespaceURI;
        }
        String result = namespaces.get(prefix);
        if (result == null) {
            result = XMLConstants.NULL_NS_URI;
        }
        return result;
    }
}
