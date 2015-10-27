package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;

public class DocumentationImpl implements Rule.Documentation {

    @NonNull
    private final String id;
    @NonNull
    private final String brief;
    @NonNull
    private final String description;
    @NonNull
    private final String section;
    @NonNull
    private final Severity severity;

    public DocumentationImpl(@NonNull String id, @NonNull String brief, @NonNull String description, @Nullable String section, @NonNull Severity severity) {
        this.id = id;
        this.brief = brief;
        this.description = description;
        this.section = section;
        this.severity = severity;
    }

    public static Rule.@NonNull Documentation create(@NonNull String id, @NonNull final String brief, @NonNull final String description, @NonNull final String section,
            @NonNull Severity severity) {
        return new DocumentationImpl(id, brief, description, section, severity);
    }

    @Override
    @NonNull
    public String getBrief() {
        return brief;
    }

    @Override
    @NonNull
    public String getDescription() {
        return description;
    }

    @Override
    public @NonNull Severity getSeverity() {
        return severity;
    }

    @Override
    @Nullable
    public String getSection() {
        return section;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DocumentationImpl))
            return false;

        DocumentationImpl that = (DocumentationImpl) o;

        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
