package org.mule.tools.devkit.sonar.checks.structure;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Rule;

import java.math.BigInteger;
import java.util.List;

import static org.mule.tools.devkit.sonar.checks.structure.GitIgnoreExistsCheck.KEY;
import static org.sonar.check.Priority.CRITICAL;

@Rule(key = KEY,
        name = ".gitignore should be present",
        description = "There should exist a .gitignore file in root folder.",
        priority = CRITICAL,
        tags = "connector-certification")
public class GitIgnoreExistsCheck extends ExistingResourceCheck {

    public static final String KEY = "gitignore-exist";
    public static final String PATH = ".gitignore";

    public GitIgnoreExistsCheck(FileSystem fileSystem) {
        super(fileSystem);
    }

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        List<ConnectorIssue> issues = Lists.newArrayList(super.analyze(mavenProject));
        System.out.println(getFileSystem().baseDir());
        if (FileUtils.listFiles(getFileSystem().baseDir(), new WildcardFileFilter(PATH), TrueFileFilter.INSTANCE).size() > BigInteger.ONE.intValue()) {
            issues.add(new ConnectorIssue(KEY, "More than one .gitignore file in project."));
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
