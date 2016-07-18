package org.mule.tools.devkit.sonar.checks.git;

import com.google.common.base.Function;

import javax.annotation.Nullable;

import static com.google.common.base.Strings.nullToEmpty;

public class RemoveRegexFunction implements Function<String, String> {

    @Override
    public String apply(@Nullable String pattern) {
        return nullToEmpty(pattern).replaceAll("(\\\\)|( *\\[/]\\?)|( *\\[\\*]\\?)", "");
    }
}
