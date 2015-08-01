package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface ValidationError {

    Rule.@NonNull Documentation getDocumentation();

    @NonNull String getMessage();

    static ValidationError create(Rule.@NonNull Documentation doc, @NonNull String message) {
        return new ValidationErrorImpl(doc, message);
    }

}
