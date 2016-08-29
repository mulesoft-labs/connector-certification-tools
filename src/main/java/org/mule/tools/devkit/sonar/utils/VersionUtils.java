package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VersionUtils implements Comparable<VersionUtils> {

    private int major;
    private int minor;
    private int rev;

    public VersionUtils(String version) {
        List<String> tokens = Splitter.on(".").omitEmptyStrings().splitToList(version);
        this.major = Integer.parseInt(tokens.get(0));
        this.minor = Integer.parseInt(tokens.get(1));
        if (tokens.size() == 3) {
            this.rev = Integer.parseInt(tokens.get(2));
        } else {
            this.rev = 0;
        }
    }

    public int compareTo(@NotNull VersionUtils currentVersion) {
        if (this.major != currentVersion.major) {
            return Integer.compare(this.major, currentVersion.major);
        }
        if (this.minor != currentVersion.minor) {
            return Integer.compare(this.minor, currentVersion.minor);
        }
        if (this.rev != currentVersion.rev) {
            return Integer.compare(this.rev, currentVersion.rev);
        }
        return 0;
    }


    public void replaceIfGreaterThan(VersionUtils version) {
        if (this.minor > version.minor || this.minor == version.minor && this.rev > version.rev) {
            version.minor = this.minor;
            version.rev = this.rev;
        }
    }

    @Override
    public String toString() {
        return "" + major + "." + minor + "." + rev;
    }
}
