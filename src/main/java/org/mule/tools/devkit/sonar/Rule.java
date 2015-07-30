package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.nio.file.Path;

public interface Rule {

    boolean verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException;

    String errorMessage(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException;

    boolean accepts(@NonNull Path basePath, @NonNull Path childPath);

    interface Documentation {

        @NonNull String getBrief();

        @NonNull String getDescription();

        @Nullable String getSection();

        @Nullable String getId();
    }
}
