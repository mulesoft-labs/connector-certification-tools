package org.mule.tools.devkit.sonar.test;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.loader.JsonRulesLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

interface TestData {

    @NonNull static Path noCompliantTestPath() {
        return Paths.get("src/test/resources/wrong_connector").toAbsolutePath();
    }

    @NonNull
    static Path perfectTestPath() {
        return Paths.get("src/test/resources/perfect_connector").toAbsolutePath();
    }

    @NonNull
    static Rule findRule(@NonNull final String id) throws IOException {
        final Set<Rule> rules = JsonRulesLoader.build();
        final Optional<Rule> result = rules.stream().filter(rule -> rule.getDocumentation().getId().endsWith(id)).findFirst();
        return result.get();

    }

}
