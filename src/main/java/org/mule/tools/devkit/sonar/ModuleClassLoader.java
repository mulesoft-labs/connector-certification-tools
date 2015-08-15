package org.mule.tools.devkit.sonar;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModuleClassLoader extends URLClassLoader {

    final private static Logger logger = LoggerFactory.getLogger(ModuleClassLoader.class);

    private final static XPathFactory xpathFactory = XPathFactory.newInstance();
    private final static DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    private static DocumentBuilder builder = null;

    static {
        try {
            domFactory.setNamespaceAware(true);
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public ModuleClassLoader(final @NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        super(initUrls(basePath), null);
    }

    private static URL[] initUrls(@NonNull Path basePath) throws IOException, XPathExpressionException, SAXException {
        final List<URL> result = new ArrayList<>();

        // Add maven module target dir ...
        final Path targetPath = basePath.resolve("target/classes/");
        if (!Files.exists(targetPath)) {
            throw new IllegalStateException("Maven target directory could not be found." + targetPath.toAbsolutePath().toString());
        }
        result.add(targetPath.toUri().toURL());

        // Add maven
        final Path pomXml = basePath.resolve("pom.xml");
        if (!Files.exists(pomXml)) {
            throw new IllegalStateException("Project pom.xml could not be found." + targetPath.toAbsolutePath().toString());
        }

        // Compile xpath expression ...
        final XPath xpath = xpathFactory.newXPath();

        // Set namespace context resolver...
        final PomNamespaceContext context = new PomNamespaceContext();
        xpath.setNamespaceContext(context);

        final XPathExpression expression = xpath.compile("/pom:project/pom:dependencies/pom:dependency");
        final InputStream is = Files.newInputStream(pomXml);
        final Document xmlDocument = builder.parse(is);
        final NodeList dependencies = (NodeList) expression.evaluate(xmlDocument, XPathConstants.NODESET);

        // Process dependencies and jar path ...
        for (int i = 0; i < dependencies.getLength(); i++) {
            final NodeList dependency = dependencies.item(i).getChildNodes();
            final Path jarPath = dependencyToJarPath(dependency);
            result.add(jarPath.toUri().toURL());
        }

        return result.toArray(new URL[result.size()]);
    }

    @NonNull private static Path dependencyToJarPath(@NonNull final NodeList dependency) {

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
                        version = node.getTextContent();
                        break;
                    }
                }
            }
        }

        // Create maven default layout path ...
        final Path mvnLocalRepo = findMvnLocalRepo();
        final Path jarFolder = mvnLocalRepo.resolve(groupId.replace(".", File.separator)).resolve(artifactId).resolve(version);

        return jarFolder.resolve(artifactId + "-" + version + ".jar");
    }

    @NonNull private static Path findMvnLocalRepo() {
        final String userHome = System.getProperty("user.home");
        return Paths.get(userHome).resolve(".m2/repository");
    }

}
