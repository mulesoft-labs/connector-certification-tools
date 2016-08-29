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
    private VersionUtils latestVersion;
    private VersionUtils currentVersion;

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        String devkitVersion = mavenProject.getModel().getParent().getVersion();
        String issueMessage;
        latestVersion = PomUtils.setLatestVersion();
        if (devkitVersion.indexOf('-') != -1) {
            issueMessage = String.format("Current connector Devkit version '%s' is not the last stable version. If feasible, use the latest stable one '%s'.", devkitVersion, latestVersion);
            issues.add(new ConnectorIssue(KEY, issueMessage));
        } else {
            currentVersion = new VersionUtils(devkitVersion);
            if (latestVersion.compareTo(currentVersion) == OUTDATED_VERSION) {
                issueMessage = String.format("Current connector Devkit version '%s' is not up to date. If feasible, use the latest DevKit stable version '%s'.", currentVersion, latestVersion);
                issues.add(new ConnectorIssue(KEY, issueMessage));
            }
        }
        return issues;
    }

    public VersionUtils getLatestVersion() {
        return latestVersion;
    }

    public VersionUtils getCurrentVersion() {
        return currentVersion;
    }
}