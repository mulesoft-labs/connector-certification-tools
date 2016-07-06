package org.mule.tools.devkit.sonar.checks.maven;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

@Rule(key = SnapshotConnectorArtifactCheck.KEY, name = "Connector artifact version MUST be SNAPSHOT", description = "Checks that the connector artifact version has a '-SNAPSHOT' declared in pom.xml", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class SnapshotConnectorArtifactCheck implements MavenCheck {

    public static final String KEY = "snapshot-artifact-mandatory";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        return PomUtils.hasSnapshot(mavenProject.getVersion()) ?
                Lists.<ConnectorIssue>newArrayList() :
                Lists.newArrayList(new ConnectorIssue(KEY, String.format("Project artifact (%s) MUST have a SNAPSHOT. Current version is (%s) but it should be (%s-SNAPSHOT).",
                mavenProject.getArtifactId(), mavenProject.getVersion(), mavenProject.getVersion())));
    }

}
