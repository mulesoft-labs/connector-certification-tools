package org.mule.tools.devkit.sonar;

import org.mule.tools.devkit.sonar.output.ConsoleReport;
import org.mule.tools.devkit.sonar.output.Report;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

public class Main {

    private static final String CERTIGNORE_FILE_NAME = ".certignore";

    static public void main(String argv[]) throws IOException {

        final ConnectorModuleValidator validator = ConnectorModuleValidator.create();

        final String arg = argv.length == 0 ? "-h" : argv[0];
        switch (arg) {
            case "-v": {
                final Set<Rule.Documentation> docs = validator.rulesDoc();
                System.out.println("Supported certification rules:\n");
                docs.forEach(doc -> System.out.printf("\t-> %s - %s\n", doc.getId(), doc.getBrief()));
                break;
            }
            case "-h": {
                System.out.println("Invalid argument arguments. Use ['-v']");
                break;
            }
            default: {

                // Load ignore properties ...
                final Path modulePath = Paths.get(arg);
                final Path ignorePath = modulePath.resolve(CERTIGNORE_FILE_NAME);
                if (Files.exists(ignorePath)) {
                    final InputStream is = Files.newInputStream(ignorePath);

                    final Properties ignoreProps = new Properties();
                    ignoreProps.load(is);
                    validator.setIgnore(ignoreProps);
                }

                // Execute validator ...
                final Set<ValidationError> errors = validator.execute(modulePath);


                // Print report ....
                final Report report = new ConsoleReport();
                report.process(modulePath, errors);
            }

        }

    }
}
