package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorModuleValidator {

    final private static Logger logger = LoggerFactory.getLogger(ConnectorModuleValidator.class);
    private Set<Rule> rules;
    private Properties ignore;
    private static final Set<String> exclusions = new HashSet<>();

    static {
        exclusions.add(".");
        exclusions.add("target");
    }

    private ConnectorModuleValidator() {

    }

    public static @NonNull ConnectorModuleValidator create() throws IOException {
        return create(new Properties());
    }

    public static @NonNull ConnectorModuleValidator create(@NonNull Properties ignore) throws IOException {
        final ConnectorModuleValidator result = new ConnectorModuleValidator();
        result.init();
        result.setIgnore(ignore);
        return result;
    }

    private void init() throws IOException {
        // Load rules ...
        this.rules = RulesFactory.load();
    }

    @NonNull
    public Set<ValidationError> execute(@NonNull final Path basePath) throws IOException {

        // Init Context ...
        Context.getInstance(basePath);

        // Process rules ...
        final Stream<Path> filesToProgress = Files.walk(basePath, FileVisitOption.FOLLOW_LINKS).map(path -> basePath.relativize(path))
                .filter(childPath -> !exclusions.stream().anyMatch(exc -> childPath.toString().startsWith(exc)));

        final Set<Set<ValidationError>> result = filesToProgress.map(relativePath -> {
            logger.debug("Processing file -> '{}' '{}'", basePath, relativePath);

            // Filter rules ...
                final Stream<Rule> filteredRules = rules.stream().filter(rule -> rule.accepts(basePath, relativePath));

                // Apply rules ..
                return filteredRules.map(rule -> rule.verify(basePath, relativePath)).collect(Collectors.toSet());
            }).filter(set -> !set.isEmpty()).flatMap(Collection::stream).collect(Collectors.toSet());

        final Stream<ValidationError> errors = result.stream().flatMap(Collection::stream);

        // Filter ignored errors ....
        return errors.filter(e -> ignore.get(e.getUUID()) == null).collect(Collectors.toSet());
    }

    @NonNull
    public Set<Rule.Documentation> rulesDoc() throws IOException {
        return rules.stream().map(Rule::getDocumentation).collect(Collectors.toSet());
    }

    public void setIgnore(@NonNull Properties ignore) {
        this.ignore = ignore;
    }
}
