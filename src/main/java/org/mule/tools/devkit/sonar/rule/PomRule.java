package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.PomNamespaceContext;
import org.mule.tools.devkit.sonar.Rule;

import javax.xml.namespace.NamespaceContext;
import java.util.Optional;

public class PomRule extends XmlRule {

    public PomRule(final Rule.Documentation documentation, @NonNull String acceptRegexp, @Nullable final String verifyExpression) {
        super(documentation, "pom.xml$", verifyExpression);
    }

    @Override protected Optional<NamespaceContext> createXPathNsContext() {
        final NamespaceContext namespace = new PomNamespaceContext();
        return Optional.of(namespace);
    }
}

