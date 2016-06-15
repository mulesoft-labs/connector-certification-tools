package org.mule.tools.devkit.sonar.checks.structure;

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Rule(key = GitIgnoreExistsCheck.KEY, name = ".gitignore should be present", description = "There should exist a .gitignore file in root folder.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
public class GitIgnoreExistsCheck extends ExistingResourceCheck {

    public static final String KEY = "gitignore-exist";
    private static final String PATH = ".gitignore";
    private static final ImmutableList<String> REQUIRED_GITIGNORE_FIELDS = ImmutableList.of("*.class", "*.jar", "*.war", "target/", ".classpath", ".settings/", ".project",
            ".factorypath", ".idea/", "*.iml", "*.ipr", "*.iws", "bin/", ".DS_Store", "automation-credentials.properties");

    private static FileSystem fileSystem;

    public GitIgnoreExistsCheck(FileSystem fileSystem) {
        super(fileSystem);
        this.fileSystem = fileSystem;
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
                    File gitIgnoreFile = Iterables.getOnlyElement(testFiles);
                    List<String> gitIgnoreElements = FileUtils.readLines(gitIgnoreFile, StandardCharsets.UTF_8);
                    Iterable<String> missingRequiredFields = Iterables.filter(REQUIRED_GITIGNORE_FIELDS, not(in(gitIgnoreElements)));
                    if (!Iterables.isEmpty(missingRequiredFields)) {
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
