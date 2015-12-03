package org.mule.tools.devkit.sonar.checks.pom;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public abstract class BasicPomTestBase {

    protected String pomForCurrentClass() {
        return getClass().getSimpleName() + "-pom.xml";
    }

    protected MavenProject createMavenProjectFromPom(String pomResource) throws IOException, XmlPullParserException {
        MavenProject mavenProject;
        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(pomResource), StandardCharsets.UTF_8)) {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            Model model = pomReader.read(reader);
            mavenProject = new MavenProject(model);
        }
        return mavenProject;
    }
}
