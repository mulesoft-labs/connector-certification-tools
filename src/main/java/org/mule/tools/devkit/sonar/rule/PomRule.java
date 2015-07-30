package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Map;

public class PomRule extends XmlRule {

    public static final String POM_XML_NAMESPACE = "http://maven.apache.org/POM/4.0.0";

    public PomRule(@NonNull final String id, @NonNull final String description, @NonNull final String documentation, @NonNull final String xpath, @NonNull String acceptRegexp) {
        super(id, description, documentation, xpath, acceptRegexp);
    }

    @Override protected @Nullable NamespaceContext createXPathNsContext() {
        final Map<String, String> namespaces = new HashMap<>();
        return new NamespaceContextImpl(POM_XML_NAMESPACE, namespaces);
    }
}

