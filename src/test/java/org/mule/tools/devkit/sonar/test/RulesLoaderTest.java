package org.mule.tools.devkit.sonar.test;

import org.junit.Test;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.RulesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class RulesLoaderTest {

    final static Logger logger = LoggerFactory.getLogger(RulesLoaderTest.class);

    @Test public void loadDefault() throws IOException {
        final Set<Rule> rules = RulesFactory.load();


    }



}
