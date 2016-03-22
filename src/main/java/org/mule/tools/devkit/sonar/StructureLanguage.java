package org.mule.tools.devkit.sonar;

import org.sonar.api.resources.AbstractLanguage;

public class StructureLanguage extends AbstractLanguage {

    public static final String NAME = "Structure";
    public static final String KEY = "struct";

    public StructureLanguage() {
        super(KEY, NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getFileSuffixes() {
        return new String[] { "*.txt" };
    }

}
