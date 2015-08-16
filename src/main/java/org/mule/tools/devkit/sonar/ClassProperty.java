package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public enum ClassProperty {
    CONNECTOR_PACKAGE(model -> Collections.singleton(model.getPackage())),
    CONNECTOR_PROCESSOR(model -> model.getProcessors());

    private final @NonNull Function<Context.ConnectorModel, Set<String>> function;

    ClassProperty(@NonNull final Function<Context.ConnectorModel, Set<String>> function) {
        this.function = function;
    }

    @NonNull public String toKey() {
        return this.name().toLowerCase().replace("_", ".");
    }

    public static @NonNull ClassProperty to(@NonNull final String key) {
        final String id = key.toUpperCase().replace(".", "_");
        return ClassProperty.valueOf(id);
    }

    @NonNull Set<String> values(final Context.ConnectorModel model) {
        return function.apply(model);
    }
}
