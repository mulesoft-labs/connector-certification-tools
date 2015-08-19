package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;

public interface ValidationError {

    Rule.@NonNull Documentation getDocumentation();

    @NonNull String getMessage();

    @NonNull String getUUID();

    @NonNull static ValidationError create(Rule.@NonNull Documentation doc, @NonNull String message) {
        return new ValidationErrorImpl(doc, message);
    }

    @NonNull static ValidationError create(Rule.@NonNull Documentation doc, @NonNull String uuid, @NonNull String message) {
        return new ValidationErrorImpl(doc, uuid, message);
    }

    //
    //    @NonNull
    //    static Set<ValidationError> create(Rule.@NonNull Documentation doc, @NonNull Set<String> messages) {
    //        return messages.stream().map(m -> new ValidationErrorImpl(doc, m, false)).collect(Collectors.toSet());
    //    }

}
