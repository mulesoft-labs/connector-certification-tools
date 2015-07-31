package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;

public class ContextImpl implements Context {

    private static ContextImpl instance;

    @NonNull public static Context getInstance(final @NonNull Path basePath) {
        return new ContextImpl();
    }

}
