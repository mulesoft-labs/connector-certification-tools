package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface Context {

    @NonNull static Context getInstance(@NonNull Path path) {
        return ContextImpl.getInstance(path);
    }

    @NonNull String getDevKitVersion();

    @NonNull ClassLoader getProjectClassLoader();

    @NonNull ConnectorModel getConnectorModel();

    interface ConnectorModel {

        List<String> getProcessors();

        List<String> getSources();

        List<String> getPackage();

        List<String> getProperty(String var);
    }

    static Context create(@NonNull final Path basePath) {
        final ContextImpl result = new ContextImpl(basePath);
        result.init();
        return result;
    }
}

