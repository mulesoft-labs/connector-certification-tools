package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import com.google.common.base.Splitter;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Strings.padStart;
import static com.google.common.collect.Iterables.transform;
import static java.lang.Math.max;

public final class VersionUtils {

    private VersionUtils(){
    }

    public static Integer compareTo(String a, String b) {
        Integer maxSize = max(a.length(), b.length());
        return convert(a, maxSize).compareTo(convert(b, maxSize));
    }

    public static String convert(String a, final Integer size) {
        return on("").join(transform(Splitter.on(".").split(a), new Function<String, String>() {
            @Override
            public String apply(String input) {
                return padStart(input, size, '0');
            }
        }));
    }
}