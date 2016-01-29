package org.mule.tools.devkit.sonar.checks.structure;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorCategory;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

@Rule(key = LicenseDeclarationFilesCheck.KEY, name = "License files should be present", description = "There should exist 2 files named LICENSE_HEADER.txt and LICENSE.md, and they should have the right content depending on the connector category", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class LicenseDeclarationFilesCheck implements StructureCheck {

    public static final String KEY = "license-declaration-files";

    private static final String LICENSE_FILE = "LICENSE.md";
    private static final String LICENSE_HEADER_FILE = "LICENSE_HEADER.txt";
    private static final DateFormat YEAR_FORMAT = new SimpleDateFormat("YYYY");
    public static final Pattern TOKENIZER_PATTERN = Pattern.compile("[\\w']+");
    private final FileSystem fileSystem;

    public LicenseDeclarationFilesCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        ConnectorCategory category = PomUtils.category(mavenProject);
        checkLicenseFileContent(issues, category);
        return issues;
    }

    private void checkLicenseFileContent(List<ConnectorIssue> issues, ConnectorCategory category) {
        switch (category) {
            case COMMUNITY:
            case SELECT:
            case CERTIFIED:
            case STANDARD:
                checkLicenseFileContent(category, issues, LICENSE_FILE);
                checkLicenseFileContent(category, issues, LICENSE_HEADER_FILE);
                break;

            case PREMIUM:
                // Nothing to validate, Premium license can be totally custom
                break;
        }
    }

    private void checkLicenseFileContent(ConnectorCategory category, List<ConnectorIssue> issues, String fileName) {
        final String masterFileName = String.format("%s.%s", fileName, category.name().toLowerCase());
        Path path = fileSystem.baseDir().toPath().resolve(fileName);
        if (!Files.exists(path)) {
            issues.add(new ConnectorIssue(KEY, String.format("File '%s' is missing. Please add a license file.", fileName)));
        } else {
            try {
                String connectorText = FileUtils.readFileToString(path.toFile(), StandardCharsets.UTF_8);
                String masterText = Resources.toString(getClass().getResource(masterFileName), StandardCharsets.UTF_8).replace("${current_year}", YEAR_FORMAT.format(new Date()));

                // Check that connectorText doesn't have ${current_year} string
                if (connectorText.contains("${current_year}")) {
                    issues.add(new ConnectorIssue(KEY, String.format("License file '%s' contains text '${current_year}'. It should have a resolved date not a variable.", fileName)));
                }

                final List<String> masterTokens = getTokens(masterText);
                final List<String> connectorTokens = getTokens(connectorText);
                if (connectorTokens.size() < masterTokens.size()) {
                    // It doesn't contain the full license text
                    issues.add(new ConnectorIssue(KEY, String.format("Difference in license file '%s'. Content is shorter than expected. Please use the base template: %s.",
                            fileName, masterFileName)));
                } else {
                    int i;
                    for (i = 0; i < masterTokens.size() && masterTokens.get(i).equals(connectorTokens.get(i)); i++) {
                    }
                    if (i < masterTokens.size()) {
                        // It found 2 different words
                        issues.add(new ConnectorIssue(KEY, String.format(
                                "Difference in license file '%s'. Found word '%s' where '%s' was expected. Please use the base template: %s", fileName, connectorTokens.get(i),
                                masterTokens.get(i), masterFileName)));
                    }
                }
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).warn(String.format("Problem reading file: %s", fileName), e);
                issues.add(new ConnectorIssue(KEY, String.format("Problem reading license file: '%s'.", fileName)));
            }
        }
    }

    private List<String> getTokens(String text) {
        Matcher matcher = TOKENIZER_PATTERN.matcher(text);
        List<String> tokens = Lists.newArrayList();
        while (matcher.find()) {
            tokens.add(text.substring(matcher.start(), matcher.end()));
        }
        return tokens;
    }

}
