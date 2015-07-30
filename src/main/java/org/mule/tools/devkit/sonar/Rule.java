package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.nio.file.Path;

public interface Rule {

    boolean verify(@NonNull Path path) throws DevKitSonarRuntimeException;

    @NonNull String errorMessage(@NonNull Path path) throws DevKitSonarRuntimeException;

    boolean accepts(@NonNull Path path);

    interface Documentation {

        @NonNull String getBrief();

        @NonNull String getDescription();

        @Nullable String getSection();

        @Nullable String getId();
    }

}
