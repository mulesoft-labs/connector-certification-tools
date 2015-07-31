package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.Set;

public interface Context {

    @NonNull static Context getInstance(final @NonNull Path path) {
        return ContextImpl.getInstance(path);
    }

    @NonNull ConnectorModel getConnectorModel();
}

interface ConnectorModel {

    @NonNull Set<String> getProcessors();
}