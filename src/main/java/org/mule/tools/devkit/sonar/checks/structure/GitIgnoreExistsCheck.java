package org.mule.tools.devkit.sonar.checks.structure;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Rule(key = GitIgnoreExistsCheck.KEY, name = ".gitignore should be present", description = "There should exist a .gitignore file in root folder.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
public class GitIgnoreExistsCheck extends ExistingResourceCheck {

    public static final String KEY = "gitignore-exist";
    private static final String PATH = ".gitignore";
    private static FileSystem fileSystem;

    private static final ImmutableList<String> REQUIRED_GITIGNORE_FIELDS = ImmutableList.of("\\*\\.class", "\\*\\.jar", "\\*\\.war", "[/]?target[/]?", "[*]?[/]?\\.classpath",
            "[*]?[/]?\\.settings[/]?", "[*]?[/]?\\.project",
            "[*]?[/]?\\.factorypath", "[/]?\\.idea[/]?", "\\*\\.iml", "\\*\\.ipr", "\\*\\.iws", "[/]?bin[/]?", "[/]?\\.DS_Store", "automation-credentials\\.properties",
            "muleLicenseKey.lic");

    public GitIgnoreExistsCheck(FileSystem fileSystem) {
        super(fileSystem);
        this.fileSystem = fileSystem;
    }

    private static class matchesPredicate<T extends String> implements Predicate<T> {

        private final Iterable<String> target;

        private matchesPredicate(Collection<String> target) {
            this.target = target;
        }

        @Override
        public boolean apply(T t) {
            for (String s : target) {
                if (Pattern.compile(t)
                        .matcher(s)
                        .matches()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        List<ConnectorIssue> issues = Lists.newArrayList(super.analyze(mavenProject));
        if (issues.isEmpty()) {
            Collection<File> testFiles = FileUtils.listFiles(fileSystem.baseDir(), new WildcardFileFilter(PATH), TrueFileFilter.INSTANCE);
            if (testFiles.size() > 1) {
                issues.add(new ConnectorIssue(KEY, "More than one .gitignore file in project."));
            } else {
                try {
                    List<String> gitIgnoreElements = FileUtils.readLines(Iterables.getOnlyElement(testFiles), StandardCharsets.UTF_8);
                    Iterable<String> missingRequiredFields = Iterables.filter(REQUIRED_GITIGNORE_FIELDS, new matchesPredicate(gitIgnoreElements));
                    if (!Iterables.isEmpty(missingRequiredFields)) {
                        missingRequiredFields = Iterables.transform(missingRequiredFields, new Function<String, String>() {

                            @Override
                            public String apply(String input) {
                                return input.replaceAll("(\\\\)|( *\\[/]\\?)|( *\\[\\*]\\?)", "");
                            }
                        });
                        issues.add(new ConnectorIssue(KEY, String.format(".gitignore file in project is missing the following exclusions: '%s'.", Joiner.on(", ")
                                .join(missingRequiredFields))));
                    }
                } catch (IOException e) {
                    Throwables.propagate(e);
                }
            }
        }
        return issues;
    }

    @Override
    protected String resourcePath() {
        return PATH;
    }

    @Override
    protected String ruleKey() {
        return KEY;
    }

}
