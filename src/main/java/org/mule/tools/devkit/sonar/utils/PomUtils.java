package org.mule.tools.devkit.sonar.utils;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PomUtils {

    private PomUtils() {
    }

    public static MavenProject createMavenProjectFromPom() {
        MavenProject mavenProject;
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream("pom.xml"), StandardCharsets.UTF_8)) {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            Model model = pomReader.read(reader);
            mavenProject = new MavenProject(model);
        } catch (IOException | XmlPullParserException e) {
            throw new IllegalStateException("Couldn't initalize pom", e);
        }
        return mavenProject;
    }
}
