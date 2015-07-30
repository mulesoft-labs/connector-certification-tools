package org.mule.tools.devkit.sonar.rule;

import org.checkerframework.checker.igj.qual.Immutable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.regex.Pattern;

@Immutable abstract public class AbstractRule implements Rule {

    final private Documentation documentation;
    final private Pattern acceptRegexp;
    final private static Logger logger = LoggerFactory.getLogger(AbstractRule.class);

    protected AbstractRule(@NonNull final Documentation documentation, @NonNull final String acceptRegexp) {
        this.documentation = documentation;
        this.acceptRegexp = Pattern.compile(acceptRegexp);

    }

    @Override
    public boolean accepts(@NonNull Path basePath, @NonNull Path childPath) {
        final String pathStr = childPath.toFile().toString();
        final boolean result = acceptRegexp.matcher(pathStr).matches();
        logger.debug("File {} accepted -> {}", pathStr, result);

        return result;
    }

    @NonNull
    public Documentation getDocumentation() {
        return documentation;
    }

    @Override
    public String errorMessage(@NonNull Path basePath, @NonNull Path childPath) {
        return "-> Rule not satisfied. " + this.getDocumentation().getDescription();
    }


}
