package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.base.Joiner;
import static com.google.common.base.Predicates.*;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.apache.commons.io.FileUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        Collection<File> testFiles = FileUtils.listFiles(fileSystem.baseDir(), new WildcardFileFilter(PATH), TrueFileFilter.INSTANCE);
        if (testFiles.size() > 1) {
            issues.add(new ConnectorIssue(KEY, "More than one .gitignore file in project."));
        }
        if (issues.isEmpty()) {
            try {
                File gitIgnoreFile = Iterables.find(testFiles, equalTo(new File(fileSystem.baseDir(), PATH)));
                String gitIgnoreText = FileUtils.readFileToString(gitIgnoreFile, StandardCharsets.UTF_8);
                List<String> gitIgnoreElements = ImmutableList.copyOf(StringUtils.split(gitIgnoreText, '\n'));
                Iterable<String> missingRequiredFields = Iterables.filter(REQUIRED_GITIGNORE_FIELDS,
                        not(in(gitIgnoreElements)));
                if (!Iterables.isEmpty(missingRequiredFields)) {
                    issues.add(new ConnectorIssue(KEY, String.format(".gitignore file in project is missing the following exclusions:  '%s'.", Joiner.on(", ")
                            .join(missingRequiredFields))));
                }
            } catch (IOException e) {
                Throwables.propagate(e);
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
