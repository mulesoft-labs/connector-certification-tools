package org.mule.tools.devkit.sonar.output;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ValidationError;

import java.nio.file.Path;
import java.util.Set;

public interface Report {

    void process(Path arg, @NonNull Set<ValidationError> errors);

}
