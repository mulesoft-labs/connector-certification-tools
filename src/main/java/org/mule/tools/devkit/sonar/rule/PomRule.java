package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;

import javax.xml.namespace.NamespaceContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PomRule extends XmlRule {

    private static final String POM_XML_NAMESPACE = "http://maven.apache.org/POM/4.0.0";

    public PomRule(final Rule.Documentation documentation, @NonNull String acceptRegexp, @Nullable final String verifyExpression) {
        super(documentation, "pom.xml$", verifyExpression);
    }

    @Override protected Optional<NamespaceContext> createXPathNsContext() {
        final Map<String, String> namespaces = new HashMap<>();
        final NamespaceContext namespace = new NamespaceContextImpl(POM_XML_NAMESPACE, namespaces);
        return Optional.of(namespace);
    }
}

