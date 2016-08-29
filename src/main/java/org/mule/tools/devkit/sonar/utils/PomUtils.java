package org.mule.tools.devkit.sonar.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.checks.ConnectorCategory;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PomUtils {

    public static final String DEVKIT_GROUP_ID = "org.mule.tools.devkit";
    public static final String DEVKIT_ARTIFACT_ID = "mule-devkit-parent";
    public static final String CERTIFIED_DEVKIT_ARTIFACT_ID = "certified-mule-connector-parent";
    public static final String SNAPSHOT = "SNAPSHOT";

    private PomUtils() {
    }

    @NotNull
    public static MavenProject createMavenProjectFromPomFile(File baseDir) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(baseDir, "pom.xml")), StandardCharsets.UTF_8)) {
            return createMavenProjectFromInputStream(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't initialize pom", e);
        }
    }

    @NotNull
    public static MavenProject createMavenProjectFromInputStream(InputStreamReader reader) {
        MavenProject mavenProject;
        try {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            Model model = pomReader.read(reader);
            mavenProject = new MavenProject(model);
        } catch (IOException | XmlPullParserException e) {
            throw new IllegalStateException("Couldn't initialize pom", e);
        }
        return mavenProject;
    }

    public static boolean isDevKitConnector(MavenProject mavenProject) {
        final Parent parent = mavenProject.getModel().getParent();
        return parent != null && parent.getGroupId().equals(DEVKIT_GROUP_ID) && (parent.getArtifactId().equals(DEVKIT_ARTIFACT_ID) || parent.getArtifactId().equals(CERTIFIED_DEVKIT_ARTIFACT_ID));
    }

    public static VersionUtils getCurrentDevkitVersion(String devkitVersion){
        return new VersionUtils(devkitVersion);
    }

    public static VersionUtils getLatestDevkitVersion() {
        VersionUtils latestVersion = getCurrentDevkitVersion("0.0.0");
        try (InputStream xml = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream()) {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("version");
            latestVersion = getCurrentDevkitVersion(nodes.item(0).getFirstChild().getTextContent());
            for (int i = 1; i < nodes.getLength(); i++) {
                Node tag = nodes.item(i);
                String currentValue = tag.getNodeType() == Node.ELEMENT_NODE ? tag.getFirstChild().getTextContent() : StringUtils.EMPTY;
                // Ignore revisions (e.g. 3.7.0-M1) and Mule 4.x.x versions that refer to the new SDK
                if (StringUtils.isNotEmpty(currentValue) && currentValue.indexOf('-') < 0  && currentValue.indexOf('4') != 0) {
                    getCurrentDevkitVersion(currentValue).replaceIfGreaterThan(latestVersion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latestVersion;
    }

    public static boolean hasSnapshot(String version) {
        return StringUtils.endsWith(version, SNAPSHOT);
    }

    @NotNull
    public static ConnectorCategory category(MavenProject mavenProject) {
        final Properties properties = mavenProject.getProperties();
        ConnectorCategory category = ConnectorCategory.UNKNOWN;
        if (properties != null) {
            try {
                category = ConnectorCategory.valueOf(properties.getProperty("category").toUpperCase());
            } catch (IllegalArgumentException e) {
                LoggerFactory.getLogger(PomUtils.class).warn(String.format("Cannot parse Connector Category: %s", properties.getProperty("category")), e);
                category = ConnectorCategory.UNKNOWN;
            }
        }
        return category;
    }
}
