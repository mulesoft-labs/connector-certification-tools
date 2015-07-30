package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;

public  class DocumentationImpl implements Rule.Documentation {

    @NonNull
    private final String id;
    @NonNull
    private final String brief;
    @NonNull
    private final String description;
    @Nullable
    private final String section;

    public DocumentationImpl(@NonNull String id, @NonNull String brief, @NonNull String description, @Nullable String section) {
        this.brief = brief;
        this.description = description;
        this.section = section;
        this.id = id;
    }

    public static Rule.@NonNull Documentation create(@NonNull String id, @NonNull final String brief, @NonNull final String description, @Nullable final String section) {
        return new DocumentationImpl(id, brief, description, section);
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
    @Nullable
    public String getSection() {
        return section;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocumentationImpl{");
        sb.append("id='").append(id).append('\'');
        sb.append(", brief='").append(brief).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", section='").append(section).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
