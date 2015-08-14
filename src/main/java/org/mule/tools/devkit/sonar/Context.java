package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.List;

public interface Context {

    @NonNull static Context getInstance(@NonNull Path path) {
        return ContextImpl.getInstance(path);
    }

    @NonNull ConnectorModel getConnectorModel();

    interface ConnectorModel {

        List<String> getProperty(@NonNull String var);
    }

    static Context create(@NonNull final Path basePath) {
        return new ContextImpl(basePath);
    }
}

