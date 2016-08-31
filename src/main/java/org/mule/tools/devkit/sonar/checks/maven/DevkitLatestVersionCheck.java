package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import static org.mule.tools.devkit.sonar.utils.PomUtils.getLatestDevkitVersion;
import static org.mule.tools.devkit.sonar.utils.PomUtils.isRevision;
import static org.mule.tools.devkit.sonar.utils.VersionUtils.compareTo;

import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

@Rule(key = DevkitLatestVersionCheck.KEY, name = "Devkit version should be the latest stable release", description = "This rule checks whether the current connector is using the latest stable release version of Devkit", priority = Priority.MAJOR, tags = {"connector-certification"})
public class DevkitLatestVersionCheck implements MavenCheck {

    public static final String KEY = "devkit-latest-version";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        String devkitVersion = mavenProject.getModel().getParent().getVersion();
        String latestVersion = getLatestDevkitVersion();
        if (isRevision(devkitVersion) || compareTo(latestVersion,devkitVersion) > 0) {
            issues.add(new ConnectorIssue(KEY, String.format("Current connector Devkit version '%s' is not the latest stable version. If feasible, use version '%s'.", devkitVersion, latestVersion)));
        }
        return issues;
    }
}
