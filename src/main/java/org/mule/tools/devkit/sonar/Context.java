package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.List;

public interface Context {

    @NonNull String getDevKitVersion();

    @NonNull ClassLoader getModuleClassLoader();

    @NonNull ConnectorModel getConnectorModel();

    interface ConnectorModel {

        List<String> getProcessors();

        List<String> getSources();

        List<String> getPackage();

        List<String> getProperty(String var);
    }

    @NonNull static Context getInstance(@NonNull final Path basePath) {
        return ContextImpl.getInstance(basePath);
    }

    @NonNull static Context getInstance() {
        return ContextImpl.getInstance();
    }
}

