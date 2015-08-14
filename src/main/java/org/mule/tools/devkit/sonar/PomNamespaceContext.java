package org.mule.tools.devkit.sonar;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PomNamespaceContext implements NamespaceContext {

    private static final String POM_XML_NAMESPACE = "http://maven.apache.org/POM/4.0.0";

    private final Map<String, String> namespaces;
    private final String defaultNamespaceURI;

    public PomNamespaceContext() {
        this.defaultNamespaceURI = POM_XML_NAMESPACE;
        this.namespaces = new HashMap<>();
        this.namespaces.put("pom", POM_XML_NAMESPACE);

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
