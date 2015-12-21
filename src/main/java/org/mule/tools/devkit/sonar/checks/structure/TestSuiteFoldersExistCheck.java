package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Rule(key = TestSuiteFoldersExistCheck.KEY, name = "Test Suite directories must be present", description = "There must exist 3 test suite directories: one for Functional ('automation/functional'), one for System ('automation/system') and one for Unit ('automation/unit'). Also, there must be a Runner package ('automation/runner').", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class TestSuiteFoldersExistCheck implements StructureCheck {

    public static final String KEY = "test-suite-folders-exists";
    public static ImmutableList<String> defaultPackages = ImmutableList.of("functional", "system", "unit", "runner");
    public static final Pattern TEST_PACKAGES_PATTERN = Pattern.compile("^((.*?)(org/mule/modules)+(/\\w+/)+(automation/)+(functional|system|unit|runner)$)");
    public static final Predicate<File> HAS_VALID_TEST_PACKAGE = new Predicate<File>() {

        @Override
        public boolean apply(@Nullable File input) {
            return input != null && TEST_PACKAGES_PATTERN.matcher(input.getPath()).find();
        }
    };

    private final FileSystem fileSystem;

    public TestSuiteFoldersExistCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        final List<String> packagesCopy = Lists.newArrayList(defaultPackages);
        final File dir = fileSystem.baseDir();

        Collection<File> directories = FileUtils.listFilesAndDirs(dir, new NotFileFilter(TrueFileFilter.INSTANCE), DirectoryFileFilter.DIRECTORY);
        Iterable<File> suites = Iterables.filter(directories, HAS_VALID_TEST_PACKAGE);

        for (File suite : suites) {
            String suiteName = StringUtils.substringAfterLast(suite.getPath(), "/");
            if (defaultPackages.contains(suiteName)) {
                packagesCopy.remove(suiteName);
            }
        }

        if (packagesCopy.size() > 0) {
            for (String noSuite : packagesCopy) {
                issues.add(new ConnectorIssue(KEY, String.format("%s test suite directory doesn't exist.", WordUtils.capitalize(noSuite))));
            }
        }

        return issues;
    }

}
