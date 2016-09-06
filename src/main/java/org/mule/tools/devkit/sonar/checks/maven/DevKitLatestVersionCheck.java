package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

import static java.lang.String.format;
import static org.mule.tools.devkit.sonar.checks.maven.Version.getLatestMinorDevKitVersion;

@Rule(key = DevKitLatestVersionCheck.KEY, name = "DevKit version should be the latest stable release", description = "This rule checks whether the current connector is using the latest stable release version of DevKit", priority = Priority.MAJOR, tags = { "connector-certification"
})
public class DevKitLatestVersionCheck implements MavenCheck {

    public static final String KEY = "devkit-latest-version";
    public static final String DEVKIT_MAJOR_VERSION_3_X = "3";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        List<ConnectorIssue> issues = Lists.newArrayList();
        Version devKitVersion = new Version(mavenProject.getModel().getParent().getVersion());
        Version latestVersion = getLatestMinorDevKitVersion(DEVKIT_MAJOR_VERSION_3_X);
        if (!devKitVersion.equals(latestVersion)) {
            issues.add(new ConnectorIssue(KEY, format("Current connector DevKit version '%s' is not the latest stable version. If feasible, use version '%s'.",
                    devKitVersion, latestVersion)));
        }
        return issues;
    }
}
