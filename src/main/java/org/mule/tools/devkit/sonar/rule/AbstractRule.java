package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract public @Immutable class AbstractRule implements Rule {

    final private Documentation documentation;
    final private Pattern acceptRegexp;
    final private static Logger logger = LoggerFactory.getLogger(AbstractRule.class);

    protected AbstractRule(@NonNull final Documentation documentation, @NonNull final String acceptRegexp) {
        this.documentation = documentation;
        this.acceptRegexp = Pattern.compile(acceptRegexp);
    }

    @Override public boolean accepts(@NonNull Path basePath, @NonNull Path childPath) {
        final String pathStr = childPath.toFile().toString();
        final boolean result = acceptRegexp.matcher(pathStr).matches();
        logger.debug("File {} accepted -> {}", pathStr, result);

        return result;
    }

    @NonNull public Documentation getDocumentation() {
        return documentation;
    }

    @NonNull Set<ValidationError> buildError(@NonNull List<String> msgs) {
        return msgs.stream().map(msg -> ValidationError.create(this.getDocumentation(), msg)).collect(Collectors.toSet());
    }

    @NonNull Set<ValidationError> buildError(@NonNull String... msgs) {
        return buildError(Arrays.asList(msgs));
    }
}
