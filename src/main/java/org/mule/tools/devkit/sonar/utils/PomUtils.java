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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
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

    public static VersionUtils setLatestVersion() {
        VersionUtils latestVersion = new VersionUtils("3.0.0");
        try {
            InputStream xml = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("version");
            VersionUtils currentVersion;
            String currentValue = "";
            for (int i = 0; i < nList.getLength(); i++) {
                Node tag = nList.item(i);
                if (tag.getNodeType() == Node.ELEMENT_NODE) {
                    Element version = (Element) tag;
                    currentValue = version.getFirstChild().getTextContent();
                }
                /* It does not enter when the version has a - because those are test versions,
               and Devkit version 4.x.x is not taken into account for being a migration tool */
                if (currentValue.indexOf('-') < 0 && !currentValue.isEmpty() && currentValue.indexOf('4') != 0) {
                    currentVersion = new VersionUtils(currentValue);
                    currentVersion.replaceIfGreaterThan(latestVersion);
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
