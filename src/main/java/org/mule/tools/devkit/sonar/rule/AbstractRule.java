package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;

import java.nio.file.Path;

@Immutable abstract public class AbstractRule implements Rule {

    private final Documentation documentation;

    protected AbstractRule(@NonNull final Documentation documentation) {
        this.documentation = documentation;
    }

    static class DocumentationImpl implements Documentation {

        @NonNull private final String id;
        @NonNull private final String brief;
        @NonNull private final String description;
        @Nullable private final String section;

        public DocumentationImpl(@NonNull String id, @NonNull String brief, @NonNull String description, @Nullable String section) {
            this.brief = brief;
            this.description = description;
            this.section = section;
            this.id = id;
        }

        static @NonNull DocumentationImpl create(@NonNull String id, @NonNull final String brief, @NonNull final String description, @Nullable final String section) {
            return new DocumentationImpl(id, brief, description, section);
        }

        @Override @NonNull public String getBrief() {
            return brief;
        }

        @Override @NonNull public String getDescription() {
            return description;
        }

        @Override @Nullable public String getSection() {
            return section;
        }

        @NonNull public String getId() {
            return id;
        }

        @Override public String toString() {
            final StringBuilder sb = new StringBuilder("DocumentationImpl{");
            sb.append("id='").append(id).append('\'');
            sb.append(", brief='").append(brief).append('\'');
            sb.append(", description='").append(description).append('\'');
            sb.append(", section='").append(section).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @NonNull public Documentation getDocumentation() {
        return documentation;
    }

    @Override public @NonNull String errorMessage(@NonNull Path path) {
        return "-> Rule not satisfied. " + this.getDocumentation().getDescription();
    }

}
