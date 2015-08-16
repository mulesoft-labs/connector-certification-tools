package org.mule.tools.devkit.sonar.output;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ValidationError;

import java.nio.file.Path;
import java.util.Set;

import static org.mule.tools.devkit.sonar.output.AnsiEscapeCodesEnum.*;

public class ConsoleReport implements Report {

    public void process(final Path basePath, @NonNull Set<ValidationError> errors) {

        System.out.printf("Performing inspection over '%s'\n", basePath.toAbsolutePath().toString());

        if (!errors.isEmpty()) {
            System.out.println("Review the following violated inspections:");

            for (ValidationError error : errors) {
                System.out.printf("\t" + ANSI_RED.getCode() + "<*>" + ANSI_RESET.getCode() + " %s %s\n", error.getDocumentation().getBrief(), error.getMessage());
            }
        } else {
            System.out.printf(ANSI_GREEN.getCode() + "Congrats. All inspections has been satisfied." + ANSI_RESET.getCode() + "\n");
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
