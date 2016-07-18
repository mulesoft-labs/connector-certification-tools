package org.mule.tools.devkit.sonar.checks.git;

import org.sonar.api.resources.AbstractLanguage;

public class GitLanguage extends AbstractLanguage {

    public GitLanguage() {
        super("git", "Git");
    }

    @Override
    public String[] getFileSuffixes() {
        return new String[] { ".gitignore" };
    }
}
