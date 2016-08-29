package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.mule.tools.devkit.sonar.utils.VersionUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = DevkitLatestVersionCheck.KEY, name = "Devkit version should be the latest stable release", description = "This rule checks whether the current connector is using the latest stable release version of Devkit", priority = Priority.MAJOR, tags = {"connector-certification"})
public class DevkitLatestVersionCheck implements MavenCheck {

    public static final String KEY = "devkit-latest-version";
    private static final int OUTDATED_VERSION = 1;

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        String devkitVersion = mavenProject.getModel().getParent().getVersion();
        VersionUtils latestVersion = PomUtils.getLatestDevkitVersion();
        if (isRevision(devkitVersion) || (!isRevision(devkitVersion) && latestVersion.compareTo(PomUtils.getCurrentDevkitVersion(devkitVersion)) == OUTDATED_VERSION)) {
            issues.add(new ConnectorIssue(KEY, String.format("Current connector Devkit version '%s' is not the latest stable version. If feasible, use version '%s'.", devkitVersion, latestVersion)));
        }
        return issues;
    }

    private boolean isRevision(String devkitVersion) {
        return devkitVersion.indexOf('-') != -1;
    }
}