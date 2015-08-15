package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorModuleValidator {

    final private static Logger logger = LoggerFactory.getLogger(ConnectorModuleValidator.class);
    private final Path basePath;

    private Set<Rule> rules;

    private ConnectorModuleValidator(@NonNull final Path rootPath) {
        this.basePath = rootPath;
    }

    public static @NonNull ConnectorModuleValidator create(@NonNull final Path rootPath) throws IOException {
        final ConnectorModuleValidator result = new ConnectorModuleValidator(rootPath);
        result.init();
        return result;
    }

    private void init() throws IOException {
        // Load rules ...
        this.rules = RulesFactory.load();

        // Init Context ...
        Context.getInstance(basePath);
    }

    public void validator() throws IOException {

        // Process rules ...
        final Stream<Path> filteredHiddenDirs = Files.walk(basePath, FileVisitOption.FOLLOW_LINKS).filter(childPath -> !childPath.toString().contains("/."));
        final Set<Set<ValidationError>> collect = filteredHiddenDirs.map(childPath -> {

            final Path relativePath = childPath.relativize(childPath);
            logger.debug("Processing file -> {} {}", basePath, relativePath);

            // Filter rules ...
            final Stream<Rule> filteredRules = rules.stream().filter(rule -> rule.accepts(basePath, childPath.relativize(childPath)));

            // Apply rules ..
            return filteredRules.map(rule -> {
                // Fire verification ...
                return rule.verify(basePath, childPath);
            }).collect(Collectors.toSet());
        }).filter(set -> !set.isEmpty()).flatMap(Set::stream).collect(Collectors.toSet());

        // Generate report ...
        collect.forEach(System.err::println);
    }

    @NonNull public Set<Rule.Documentation> rulesDoc() throws IOException {
        return rules.stream().map(Rule::getDocumentation).collect(Collectors.toSet());
    }
}
