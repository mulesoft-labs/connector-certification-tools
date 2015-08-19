package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@Immutable public class ValidationErrorImpl implements ValidationError {

    private final Rule.@NonNull Documentation doc;
    private final @NonNull String message;
    private final String uuid;

    ValidationErrorImpl(Rule.@NonNull Documentation doc, @NonNull String message) {
        this(doc, message, null);
    }

    public ValidationErrorImpl(Rule.@NonNull Documentation doc, @Nullable String uuid, @NonNull String message) {
        this.doc = doc;
        this.uuid = uuid;
        this.message = message;
    }

    @Override public Rule.@NonNull Documentation getDocumentation() {
        return doc;
    }

    @Override public @NonNull String getMessage() {
        return message;
    }

    @Override public @NonNull String getUUID() {
        return doc.getId() + (uuid != null ? ":" + uuid : "");
    }
}
