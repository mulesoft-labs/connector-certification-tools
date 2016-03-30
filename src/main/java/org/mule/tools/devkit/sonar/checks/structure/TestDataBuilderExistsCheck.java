package org.mule.tools.devkit.sonar.checks.structure;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.Nullable;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Rule(key = TestDataBuilderExistsCheck.KEY, name = "TestDataBuilder class should be present", description = "There should exist a class named 'TestDataBuilder' in org.mule.modules.<connector-project>.automation.functional.TestDataBuilder.java", priority = Priority.MAJOR, tags = {
        "connector-certification"
})
public class TestDataBuilderExistsCheck implements StructureCheck {

    public static final String KEY = "test-data-builder-exists";

    private static final String TEST_DATA_BUILDER_JAVA = "TestDataBuilder.java";
    private static final Pattern TEST_PACKAGES_PATTERN = Pattern.compile("^(.*?)(src/test/java/org/mule/module[s]?)+(/\\w+/)+(automation/functional)");
    private static final Predicate<File> IS_IN_AUTOMATION_PACKAGE = new Predicate<File>() {

        @Override
        public boolean apply(@Nullable File input) {
            return input != null && TEST_PACKAGES_PATTERN.matcher(input.getPath())
                    .find();
        }
    };

    private final FileSystem fileSystem;

    public TestDataBuilderExistsCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();

        Collection<File> directories = FileUtils.listFiles(fileSystem.baseDir(), new NameFileFilter(TEST_DATA_BUILDER_JAVA), TrueFileFilter.INSTANCE);
        if (directories.isEmpty()) {
            issues.add(new ConnectorIssue(KEY, "TestDataBuilder.java doesn't exist."));
        } else {
            if (!Iterables.any(directories, IS_IN_AUTOMATION_PACKAGE)) {
                issues.add(new ConnectorIssue(
                        KEY,
                        String.format(
                                "TestDataBuilder.java exists, but it's not in the appropriate folder (%s). It must be located in 'org.mule.modules.<connector-project>.automation.functional.TestDataBuilder.java'.",
                                directories.iterator()
                                        .next())));
            }
        }
        return issues;
    }

}
