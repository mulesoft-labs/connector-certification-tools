package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.regex.qual.Regex;
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
import java.util.regex.Pattern;

public class XmlRule extends AbstractRule {

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final XPathExpression xpath;
    @Regex private final Pattern acceptRegexp;
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

    public XmlRule(@NonNull final String id, @NonNull final String description, @NonNull final String documentation, @NonNull final String xpathExp, @NonNull String acceptRegexp) {
        super(DocumentationImpl.create(id, description, documentation, null));

        // Compile xpath expression ...
        final XPath xpath = xpathFactory.newXPath();

        // Set namespace context ...
        final NamespaceContext context = createXPathNsContext();
        if (context != null) {
            xpath.setNamespaceContext(context);
        }

        try {
            this.xpath = xpath.compile(xpathExp);
        } catch (XPathExpressionException e) {
            throw new DevKitSonarRuntimeException(e);
        }

        this.acceptRegexp = Pattern.compile(acceptRegexp);
    }

    @Nullable protected NamespaceContext createXPathNsContext() {
        return null;
    }

    @Override public boolean verify(@NonNull Path path) throws DevKitSonarRuntimeException {

        boolean result;
        try {
            final InputStream is = Files.newInputStream(path);
            final Document xmlDocument = builder.parse(is);
            result = !(Boolean) xpath.evaluate(xmlDocument, XPathConstants.BOOLEAN);

        } catch (SAXException | XPathExpressionException | IOException e) {
            throw new DevKitSonarRuntimeException(e);
        }
        logger.debug("Rule {} applied to {} -> {}", this.getDocumentation().getId(), path.toString(), result);

        return result;
    }

    @Override public boolean accepts(@NonNull Path path) {
        final boolean result = acceptRegexp.matcher(path.toAbsolutePath().toString()).matches();
        //logger.debug("File {} accepted -> {}", path.toString(), result);

        return result;
    }

    @Override public String toString() {
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
