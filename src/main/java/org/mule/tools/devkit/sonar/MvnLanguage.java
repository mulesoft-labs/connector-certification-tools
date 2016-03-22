package org.mule.tools.devkit.sonar;

import org.sonar.api.resources.AbstractLanguage;

public final class MvnLanguage extends AbstractLanguage {

    public static final String NAME = "Maven";
    public static final String KEY = "mvn";

    public MvnLanguage() {
        super(KEY, NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getFileSuffixes() {
        return new String[] { ".xml" };
    }

}
