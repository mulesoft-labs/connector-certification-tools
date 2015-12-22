package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import difflib.DiffUtils;
import difflib.Patch;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorCategory;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Rule(key = LicenseDeclarationFilesCheck.KEY, name = "License files should be present", description = "There should exist 2 files named LICENSE_HEADER.txt and LICENSE.md, and they should have the right content depending on the connector category", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class LicenseDeclarationFilesCheck implements StructureCheck {

    public static final String KEY = "license-declaration-files";

    private static final String LICENSE_FILE = "LICENSE.md";
    private static final String LICENSE_HEADER_FILE = "LICENSE_HEADER.txt";
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
                List<String> originalContent = Files.readAllLines(path, StandardCharsets.UTF_8);
                List<String> masterContent = Resources.readLines(getClass().getResource(masterFileName), StandardCharsets.UTF_8);

                Patch patch = DiffUtils.diff(masterContent, originalContent);
                if (!patch.getDeltas().isEmpty()) {
                    issues.add(new ConnectorIssue(KEY, String.format("Difference in license file '%s'. Please check the diff between expected and actual:%n%s", fileName, Joiner
                            .on("\n").join(DiffUtils.generateUnifiedDiff(masterFileName, fileName, originalContent, patch, 5)))));
                }
            } catch (IOException e) {
                LoggerFactory.getLogger(getClass()).warn(String.format("Problem reading file: %s", fileName), e);
                issues.add(new ConnectorIssue(KEY, String.format("Problem reading license file: '%s'.", fileName)));
            }
        }
    }

}
