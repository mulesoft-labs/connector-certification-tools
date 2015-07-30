package org.mule.tools.devkit.sonar;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.loader.JsonRulesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

public class RulesFactory
{
    final private static Logger logger = LoggerFactory.getLogger(RulesFactory.class);


    @NonNull
    public static Set<Rule> load() throws IOException {

        // Load JSON declared rules ...
        final Set<Rule> result = JsonRulesLoader.build();
        logger.info(result.toString());

        return result;
    }
}
