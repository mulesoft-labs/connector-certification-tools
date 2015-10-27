package org.mule.tools.devkit.sonar;

import org.apache.commons.lang3.text.WordUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ClassProperty {
    CONNECTOR_PACKAGE(model -> Collections.singleton(model.getPackage().replace(".", "/"))), CONNECTOR_PROCESSOR(model -> model.getProcessors().stream().map(WordUtils::capitalize)
            .collect(Collectors.toSet()));

    private final @NonNull Function<Context.ConnectorModel, Set<String>> function;

    ClassProperty(@NonNull final Function<Context.ConnectorModel, Set<String>> function) {
        this.function = function;
    }

    @NonNull
    public String toKey() {
        return this.name().toLowerCase();
    }

    public static @NonNull ClassProperty to(@NonNull final String key) {
        final String id = key.toUpperCase();
        return ClassProperty.valueOf(id);
    }

    @NonNull
    Set<String> values(final Context.ConnectorModel model) {
        return function.apply(model);
    }
}
