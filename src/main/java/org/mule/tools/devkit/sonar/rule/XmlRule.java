package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class XmlRule extends AbstractRule {

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final XPathExpression xpathExpression;
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

    private final String assertExp;

    public XmlRule(final Rule.Documentation documentation, @NonNull String accept, @NonNull final String assertExp) {
        super(documentation, accept);
        this.assertExp = assertExp;

        // Compile xpathExpression expression ...
        final XPath xpath = xpathFactory.newXPath();

        // Set namespace context resolver...
        final Optional<NamespaceContext> context = createXPathNsContext();
        if (context.isPresent()) {
            xpath.setNamespaceContext(context.get());
        }

        try {
            this.xpathExpression = xpath.compile(assertExp);
        } catch (XPathExpressionException e) {
            throw new DevKitSonarRuntimeException(e);
        }
    }

    protected Optional<NamespaceContext> createXPathNsContext() {
        return Optional.empty();
    }

    @Override @NonNull public Set<ValidationError> verify(@NonNull Path basePath, @NonNull final Path childPath) throws DevKitSonarRuntimeException {

        boolean success;
        try {
            final InputStream is = Files.newInputStream(basePath.resolve(childPath));
            final Document xmlDocument = builder.parse(is);
            success = (Boolean) xpathExpression.evaluate(xmlDocument, XPathConstants.BOOLEAN);

        } catch (SAXException | XPathExpressionException | IOException e) {
            throw new DevKitSonarRuntimeException(e);
        }
        logger.debug("Rule {} applied to {} -> {}", this.getDocumentation().getId(), childPath.toString(), success);

        return !success ? buildError("Assertion failed '" + assertExp + "'.") : Collections.<ValidationError>emptySet();
    }

    @Override public String toString() {
        return "XPathRule{} " + super.toString();
    }
}

