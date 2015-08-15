package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextImpl implements Context {

    private ClassLoader classLoader;
    private final static ThreadLocal<Context> threadLocal = new ThreadLocal<>();
    private static Map<Path, Context> instancesByPath = new ConcurrentHashMap<>();

    public ContextImpl(@NonNull final Path basePath) {
        try {
            this.classLoader = new ModuleClassLoader(basePath);
        } catch (IOException | XPathExpressionException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override public @NonNull String getDevKitVersion() {
        throw new UnsupportedOperationException();
    }

    @NonNull @Override public ClassLoader getModuleClassLoader() {
        return classLoader;
    }

    @Override public @NonNull ConnectorModel getConnectorModel() {
        throw new UnsupportedOperationException();
    }

    @NonNull public static Context getInstance(@NonNull Path basePath) {
        Context context = instancesByPath.get(basePath);
        if (context == null) {
            context = new ContextImpl(basePath);
            instancesByPath.put(basePath, context);
        }
        return context;
    }

    public void setup() {
        threadLocal.set(this);
    }

    @NonNull public static Context getInstance() {
        return threadLocal.get();
    }

}
