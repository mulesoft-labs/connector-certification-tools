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

public class ConnectorValidator {

    final private static Logger logger = LoggerFactory.getLogger(ConnectorValidator.class);
    private final Path rootPath;

    private Set<Rule> rules;

    private ConnectorValidator(@NonNull final Path rootPath) {
        this.rootPath = rootPath;
    }

    public static @NonNull ConnectorValidator create(@NonNull final Path rootPath) throws IOException {
        final ConnectorValidator result = new ConnectorValidator(rootPath);
        result.init();
        return result;
    }

    private void init() throws IOException {
        this.rules = RulesFactory.load();
    }

    public void validator() throws IOException {

        // Process rules ...
        final Stream<Path> filteredHiddenDirs = Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS).filter(childPath -> !childPath.toString().contains("/."));
        final Set<Set<ValidationError>> collect = filteredHiddenDirs.map(childPath -> {

            final Path relativePath = childPath.relativize(childPath);
            logger.debug("Processing file -> {} {}", rootPath, relativePath);

            // Filter rules ...
            final Stream<Rule> filteredRules = rules.stream().filter(rule -> rule.accepts(rootPath, childPath.relativize(childPath)));

            // Apply rules ..
            return filteredRules.map(rule -> rule.verify(rootPath, childPath)).collect(Collectors.toSet());
        }).filter(set -> !set.isEmpty()).flatMap(Set::stream).collect(Collectors.toSet());

        // Generate report ...
        collect.forEach(System.err::println);
    }

    @NonNull
    public Set<Rule.Documentation> rulesDoc() throws IOException {
        return rules.stream().map(Rule::getDocumentation).collect(Collectors.toSet());
    }
}
