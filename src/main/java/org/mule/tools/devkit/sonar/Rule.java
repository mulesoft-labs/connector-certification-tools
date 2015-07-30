package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.nio.file.Path;

public interface Rule {

    boolean accepts(@NonNull Path basePath, @NonNull Path childPath);

    boolean verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException;

    @NonNull
    String errorMessage(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException;


    interface Documentation {

        @Nullable String getId();

        @Nullable String getSection();

        @NonNull String getBrief();

        @NonNull String getDescription();


    }
}
