package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.RulesFactory;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class RulesLoaderTest {

    @Test
    public void loadDefault() throws IOException {
        final Set<Rule> rules = RulesFactory.load();
        assertTrue("Rules could not be loaded", !rules.isEmpty());
    }
}
