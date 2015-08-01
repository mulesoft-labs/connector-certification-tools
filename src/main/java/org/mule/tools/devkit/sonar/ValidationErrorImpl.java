package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;

@Immutable
public class ValidationErrorImpl implements ValidationError {

    private final Rule.Documentation doc;
    private final String message;

    ValidationErrorImpl(Rule.@NonNull Documentation doc, @NonNull String message) {

        this.doc = doc;
        this.message = message;
    }

    @Override public Rule.@NonNull Documentation getDocumentation() {
        return doc;
    }

    @Override public @NonNull String getMessage() {
        return message;
    }
}
