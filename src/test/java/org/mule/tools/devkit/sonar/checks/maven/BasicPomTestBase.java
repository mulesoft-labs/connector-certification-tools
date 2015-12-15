package org.mule.tools.devkit.sonar.checks.maven;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.mule.tools.devkit.sonar.utils.PomUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class BasicPomTestBase {

    protected String pomForCurrentClass() {
        return getClass().getSimpleName() + "-pom.xml";
    }

    protected MavenProject createMavenProjectFromPom(String pomResource) throws IOException, XmlPullParserException {
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(pomResource), StandardCharsets.UTF_8)) {
            return PomUtils.createMavenProjectFromInputStream(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't initialize pom.xml", e);
        }
    }
}
