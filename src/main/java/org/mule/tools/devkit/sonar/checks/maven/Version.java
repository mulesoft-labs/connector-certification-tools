package org.mule.tools.devkit.sonar.checks.maven;

import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.exception.SonarCheckException;
import org.mule.tools.devkit.sonar.utils.NodeIterable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.sonar.api.internal.google.common.collect.Lists.newArrayList;
import static org.w3c.dom.Node.ELEMENT_NODE;

public class Version implements Comparable<Version> {

    private final String value;

    public Version(String value) {
        this.value = value;
    }

    public boolean isRevision() {
        return value.contains("-");
    }

    public static Version getLatestMinorDevKitVersion(String majorVersion) {
        try (InputStream xml = new URL(
                "https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream()) {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(xml);
            doc.getDocumentElement()
                    .normalize();
            List<String> cocaa = newArrayList(new NodeIterable(doc.getElementsByTagName("version"))).stream()
                    .map(node -> (ofNullable(node).map(Node::getNodeType).orElse(null) == ELEMENT_NODE) ? node.getFirstChild().getTextContent() : null)
                    .collect(Collectors.toList());
            List<Version> cocab = cocaa.stream().map(Version::new).collect(Collectors.toList());
            List<Version> cocae = cocab.stream().filter(((Predicate<Version>) (Version::isRevision)).negate()).collect(Collectors.toList());
            List<Version> cocaf = cocab.stream().filter(((Predicate<Version>) (version -> version.value.startsWith(majorVersion)))).collect(Collectors.toList());
            List<Version> cocad = cocab.stream()
                    .filter(((Predicate<Version>) (Version::isRevision)).negate()
                            .and(((Predicate<Version>) (version -> version.value.startsWith(majorVersion))).negate())).collect(Collectors.toList());
            Version cocac = cocad.stream().max((a, b) -> a.compareTo(b)).orElse(null);
            return newArrayList(new NodeIterable(doc.getElementsByTagName("version"))).stream()
                    .map(node -> (ofNullable(node).map(Node::getNodeType).orElse(null) == ELEMENT_NODE) ? node.getFirstChild().getTextContent() : null)
                    .map(Version::new)
                    .filter(((Predicate<Version>) (Version::isRevision)).negate()
                            .and(version -> version.value.startsWith(majorVersion)))
                    .max((a, b) -> a.compareTo(b)).orElse(null);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new SonarCheckException("Unable to retrieve Devkit latest version.", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Version))
            return false;

        Version version = (Version) o;

        return value != null ? value.equals(version.value) : version.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(@NotNull Version version) {
        return value.compareTo(version.value);
    }
}
