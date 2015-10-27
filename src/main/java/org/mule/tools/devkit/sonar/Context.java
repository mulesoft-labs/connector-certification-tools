package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface Context {

    @NonNull
    String getDevKitVersion();

    @NonNull
    String getCategory();

    @NonNull
    ClassLoader getModuleClassLoader();

    @NonNull
    ConnectorModel getConnectorModel();

    interface ConnectorModel {

        @NonNull
        Set<String> getProcessors();

        @NonNull
        Set<String> getSources();

        @NonNull
        String getPackage();

        @NonNull
        List<String> getProperty(ClassProperty property);
    }

    @NonNull
    static Context getInstance(@NonNull final Path basePath) {
        return ContextImpl.getInstance(basePath);
    }

    @NonNull
    static Context getInstance() {
        return ContextImpl.getInstance();
    }
}
