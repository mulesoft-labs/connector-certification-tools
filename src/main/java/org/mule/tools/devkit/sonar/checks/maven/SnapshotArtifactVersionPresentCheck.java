package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(key = SnapshotArtifactVersionPresentCheck.KEY, name = "Connector artifact version MUST be SNAPSHOT", description = "Checks that the connector artifact version has a '-SNAPSHOT' declared in pom.xml", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class SnapshotArtifactVersionPresentCheck implements MavenCheck {

    public static final String KEY = "snapshot-artifact-is-present";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        return PomUtils.hasSnapshotVersion(mavenProject.getVersion()) ?
                Lists.<ConnectorIssue>newArrayList() :
                Lists.newArrayList(new ConnectorIssue(KEY, String.format("Project artifact '%s' MUST have a SNAPSHOT version when on develop branch. Current version is '%s' but it should be '%s-SNAPSHOT'.",
                mavenProject.getArtifactId(), mavenProject.getVersion(), mavenProject.getVersion())));
    }

}
