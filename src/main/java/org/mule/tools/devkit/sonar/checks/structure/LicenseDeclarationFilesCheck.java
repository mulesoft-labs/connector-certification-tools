package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.resources.Project;
import org.sonar.check.Rule;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Rule(key = LicenseDeclarationFilesCheck.KEY, name = "License files should be present", description = "There should exist 2 files named LICENSE_HEADER.txt and LICENSE.md, and they should have the right content depending on the connector category", tags = { "connector-certification" })
public class LicenseDeclarationFilesCheck implements StructureCheck {

    public static final String KEY = "license-declaration-files";

    private static final String LICENSE_FILE = "LICENSE.md";
    private static final String LICENSE_HEADER_FILE = "LICENSE_HEADER.txt";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject, Project project) {
        System.err.println("path::::::: " + project.path());
        // List<File> testFiles = (List<File>) FileUtils.listFiles(dir, new WildcardFileFilter(processorTestName), TrueFileFilter.INSTANCE);

        // final InputFile licenseMd = fs.inputFile(fs.predicates().matchesPathPattern(LICENSE_FILE));
        // final InputFile licenseHeader = fs.inputFile(fs.predicates().matchesPathPattern(LICENSE_HEADER_FILE));
        final List<ConnectorIssue> issues = Lists.newArrayList();
        //
        if (!Files.exists(Paths.get(LICENSE_FILE))) {
            issues.add(new ConnectorIssue(KEY, String.format("File %s is missing. Please add a license file", LICENSE_FILE)));
        }
        if (!Files.exists(Paths.get(LICENSE_HEADER_FILE))) {
            issues.add(new ConnectorIssue(KEY, String.format("File %s is missing. Please add a license header file", LICENSE_HEADER_FILE)));
        }
        return issues;
    }

}
