package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Strings.padStart;
import static com.google.common.collect.Iterables.transform;
import static java.lang.Math.max;
import static org.apache.commons.lang.StringUtils.EMPTY;

public final class DevkitUtils {

    private DevkitUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(PomUtils.class);

    public static Predicate<String> isValidVersion(String majorVersion) {
        return version -> !isRevision(version) && !version.startsWith(majorVersion);
    }

    public static boolean isRevision(String devKitVersion) {
        return devKitVersion.contains("-");
    }

    public static Integer compareTo(String a, String b) {
        Integer maxSize = max(a.length(), b.length());
        return convert(a, maxSize).compareTo(convert(b, maxSize));
    }

    public static String convert(String a, final Integer size) {
        return on(EMPTY).join(transform(Splitter.on(".")
                .split(a), new Function<String, String>() {

            @Override
            public String apply(String input) {
                return padStart(input, size, '0');
            }
        }));
    }
}