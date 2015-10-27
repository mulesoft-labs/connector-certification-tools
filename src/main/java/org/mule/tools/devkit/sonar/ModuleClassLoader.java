package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ModuleClassLoader extends URLClassLoader {

    final private static Logger logger = LoggerFactory.getLogger(ModuleClassLoader.class);

    public ModuleClassLoader(final @NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        super(initUrls(basePath), ModuleClassLoader.class.getClassLoader());
    }

    private static URL[] initUrls(@NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        final List<URL> result = new ArrayList<>();

        // Process dependencies and jar path ...
        final NodeList dependencies = (NodeList) XmlUtils.evalXPathOnPom(basePath, "/pom:project/pom:dependencies/pom:dependency", XPathConstants.NODESET);
        for (int i = 0; i < dependencies.getLength(); i++) {
            final NodeList dependency = dependencies.item(i).getChildNodes();
            final Path jarPath = dependencyToJarPath(dependency, basePath);
            if (Files.exists(jarPath)) {
                result.add(jarPath.toUri().toURL());
                logger.debug("Project module jar {}", jarPath);
            } else {
                logger.error("Jar could not be found ->" + jarPath.toAbsolutePath());
            }
        }

        // Add maven module target dir ...
        final Path targetPath = basePath.resolve("target/classes/");
        if (!Files.exists(targetPath)) {
            throw new IllegalStateException("Maven target directory could not be found. Module must be compiled before executing analysis."
                    + targetPath.toAbsolutePath().toString());
        }
        result.add(targetPath.toUri().toURL());

        final String devkitVersion = (String) XmlUtils.evalXPathOnPom(basePath, "/pom:project/pom:parent/pom:version/text()", XPathConstants.STRING);

        // Add DevKit annotations dependency ...
        final Path devkitJar = dependencyToPath("org.mule.tools.devkit", "mule-devkit-annotations", devkitVersion);
        result.add(devkitJar.toUri().toURL());

        // Add Mule Comments ...
        final Path muleCommonJar = dependencyToPath("org.mule.common", "mule-common", devkitVersion);
        result.add(muleCommonJar.toUri().toURL());

        // Add Mule Comments ...
        final Path muleCore = dependencyToPath("org.mule", "mule-core", devkitVersion);
        result.add(muleCore.toUri().toURL());

        return result.toArray(new URL[result.size()]);
    }

    private static Path dependencyToJarPath(@NonNull final NodeList dependency, @NonNull Path basePath) {

        String groupId = "", artifactId = "", version = "";

        for (int j = 0; j < dependency.getLength(); j++) {
            final Node node = dependency.item(j);
            final String localName = node.getLocalName();

            if (localName != null) {
                switch (localName) {
                    case "groupId": {
                        groupId = node.getTextContent();
                        break;
                    }
                    case "artifactId": {
                        artifactId = node.getTextContent();
                        break;
                    }
                    case "version": {
                        // Replace dependency versions with pom properties ...
                        final String rawVersion = node.getTextContent();
                        final NodeList propertiesNodes = (NodeList) XmlUtils.evalXPathOnPom(basePath, "/pom:project/pom:properties", XPathConstants.NODESET);
                        final NodeList childNodes = propertiesNodes.item(0).getChildNodes();
                        version = rawVersion;
                        for (int i = 0; i < childNodes.getLength(); i++) {
                            if (childNodes.item(i).getNodeType() != Node.TEXT_NODE) {
                                final Node property = childNodes.item(i);
                                final String propName = property.getNodeName();
                                final String propValue = property.getTextContent();
                                version = version.replaceAll(Pattern.quote("${" + propName + "}"), propValue);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return dependencyToPath(groupId, artifactId, version);

    }

    @NonNull
    private static Path dependencyToPath(String groupId, String artifactId, String version) {
        // Create maven default layout path ...
        final Path mvnLocalRepo = findMvnLocalRepo();
        final Path jarFolder = mvnLocalRepo.resolve(groupId.replace(".", File.separator)).resolve(artifactId).resolve(version);
        return jarFolder.resolve(artifactId + "-" + version + ".jar");
    }

    @NonNull
    private static Path findMvnLocalRepo() {
        final String userHome = System.getProperty("user.home");
        return Paths.get(userHome).resolve(".m2/repository");
    }

}
