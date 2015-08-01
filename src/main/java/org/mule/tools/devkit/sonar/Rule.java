package org.mule.tools.devkit.sonar;

import com.sun.istack.internal.NotNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import java.nio.file.Path;
import java.util.Set;

public interface Rule {

    @NonNull boolean accepts(@NonNull Path basePath, @NonNull Path childPath);

    @NonNull Set<ValidationError> verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException;

    @NotNull Documentation getDocumentation();

    interface Documentation {

        @Nullable String getId();

        @Nullable String getSection();

        @NonNull String getBrief();

        @NonNull String getDescription();

    }
}
