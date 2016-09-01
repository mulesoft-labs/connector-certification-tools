package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import java.util.List;

import static org.mule.tools.devkit.sonar.utils.DevkitUtils.*;

@Rule(key = DevKitLatestVersionCheck.KEY, name = "DevKit version should be the latest stable release", description = "This rule checks whether the current connector is using the latest stable release version of DevKit", priority = Priority.MAJOR, tags = { "connector-certification"
})
public class DevKitLatestVersionCheck implements MavenCheck {

    public static final String KEY = "devkit-latest-version";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        String devKitVersion = mavenProject.getModel()
                .getParent()
                .getVersion();
        String latestVersion = getLatestDevKitVersion();
        if (!isValidVersion.apply(devKitVersion) || compareTo(latestVersion, devKitVersion) > 0) {
            issues.add(new ConnectorIssue(KEY, String.format("Current connector DevKit version '%s' is not the latest stable version. If feasible, use version '%s'.",
                    devKitVersion, latestVersion)));
        }
        return issues;
    }
}
