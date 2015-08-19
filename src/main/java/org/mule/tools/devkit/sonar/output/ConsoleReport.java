package org.mule.tools.devkit.sonar.output;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mule.tools.devkit.sonar.output.AnsiEscapeCodesEnum.*;

public class ConsoleReport implements Report {

    private final static Map<Rule.Documentation.Severity, String> colorExp = new HashMap<>();

    static {
        colorExp.put(Rule.Documentation.Severity.CRITICAL, ANSI_RED.getCode() + "<*>" + ANSI_RESET.getCode());
        colorExp.put(Rule.Documentation.Severity.MAJOR, ANSI_YELLOW.getCode() + "<*>" + ANSI_RESET.getCode());
        colorExp.put(Rule.Documentation.Severity.MINOR, ANSI_GREEN.getCode() + "<*>" + ANSI_RESET.getCode());
        colorExp.put(Rule.Documentation.Severity.INFO, ANSI_BLUE.getCode() + "<*>" + ANSI_RESET.getCode());
    }

    public void process(final Path basePath, @NonNull Set<ValidationError> errors) {

        System.out.printf("Performing inspection over '%s'\n", basePath.toAbsolutePath().toString());

        if (!errors.isEmpty()) {
            System.out.println("Review the following violated inspections:");

            // Print group by error type ...
            final Map<Rule.Documentation, List<ValidationError>> errorsByType = errors.stream().collect(Collectors.groupingBy(ValidationError::getDocumentation));
            final List<Rule.Documentation> docs = errorsByType.keySet().stream().sorted((a, b) -> a.getSeverity().compareTo(b.getSeverity())).collect(Collectors.toList());
            for (Rule.Documentation doc : docs) {
                final List<ValidationError> verrors = errorsByType.get(doc);
                System.out.printf(" %s %s: \n", colorExp.get(doc.getSeverity()), doc.getBrief());
                for (ValidationError error : verrors) {
                    System.out.printf("\t<*> %s (id: '%s')\n", error.getMessage(), error.getUUID());
                }

            }

        } else {
            System.out.printf(ANSI_GREEN.getCode() + "Congrats. All inspections rules has been satisfied." + ANSI_RESET.getCode() + "\n");
        }

    }

}

enum AnsiEscapeCodesEnum {
    ANSI_RESET("\u001B[0m"),
    ANSI_BLACK("\u001B[30m"),
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\u001B[36m"),
    ANSI_WHITE("\u001B[37m");

    private final String code;

    AnsiEscapeCodesEnum(@NonNull String code) {
        this.code = code;
    }

    @NonNull public String getCode() {
        return code;
    }
}
