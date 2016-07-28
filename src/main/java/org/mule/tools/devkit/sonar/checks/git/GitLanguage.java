package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.api.resources.AbstractLanguage;

public class GitLanguage extends AbstractLanguage {
    public static final String KEY = "git";
    public static final String NAME = "Git";

    public GitLanguage() {
        super(KEY, NAME);
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] { ".gitignore" };
    }
}
