package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.VersionUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;

@Rule(key = DevkitLatestVersionCheck.KEY, name = "Devkit version should be the latest", description = "This rule checks whether the current Connector's Devkit version is the latest", priority = Priority.MAJOR, tags = {"connector-certification"})
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
        try {
            latestVersion = VersionUtils.setLatestVersion();
            if (devkitVersion.indexOf('-') != -1) {
                issueMessage = String.format("Current connector Devkit version '%s' is not the last stable version. If feasible, use the latest sta stable one '%s'.", devkitVersion, latestVersion);
                logAndRaiseIssue(issues, issueMessage);
            } else {
                currentVersion = new VersionUtils(devkitVersion);
                if (latestVersion.compareTo(currentVersion) == OUTDATED_VERSION) {
                    issueMessage = String.format("Current connector Devkit version '%s' is not up to date. If feasible, use the latest DevKit stable version '%s'.", currentVersion, latestVersion);
                    logAndRaiseIssue(issues, issueMessage);
                }
            }
        } catch (IOException | XMLStreamException e) {
            if (e instanceof XMLStreamException && ((XMLStreamException) e).getNestedException() != null) {
                ((XMLStreamException) e).getNestedException().printStackTrace();
            }
            throw new RuntimeException(e.getMessage());
        }
        return issues;
    }

    private final void logAndRaiseIssue(List<ConnectorIssue> issues, String message) {
        issues.add(new ConnectorIssue(KEY, message));
    }

    public VersionUtils getLatestVersion() {
        return latestVersion;
    }

    public VersionUtils getCurrentVersion() {
        return currentVersion;
    }
}