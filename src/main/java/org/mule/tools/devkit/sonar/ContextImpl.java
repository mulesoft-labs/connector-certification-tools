package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;

class ContextImpl implements Context {

    private final Path basePath;
    private ProjectClassLoader classLoader;

    public ContextImpl(@NonNull final Path basePath) {
        this.basePath = basePath;
    }

    void init() {
        try {
            this.classLoader = new ProjectClassLoader(basePath);
        } catch (IOException | XPathExpressionException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    @NonNull public static Context getInstance(final @NonNull Path basePath) {
        return new ContextImpl(basePath);
    }

    @Override public @NonNull String getDevKitVersion() {
        throw new UnsupportedOperationException();
    }

    @NonNull @Override public ClassLoader getProjectClassLoader() {
        return classLoader;
    }

    @Override public @NonNull ConnectorModel getConnectorModel() {
        throw new UnsupportedOperationException();
    }

}
