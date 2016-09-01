package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Predicates.and;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.base.Strings.padStart;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static java.lang.Math.max;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.mule.tools.devkit.sonar.utils.NodeIterable.getVersion;

public final class DevkitUtils {

    private DevkitUtils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(PomUtils.class);

    public static Predicate<String> isValidVersion = new Predicate<String>() {

        @Override
        public boolean apply(@Nullable String version) {
            return !isRevision(version)
                    && !version.startsWith("4");
        }
    };

    public static String getLatestDevKitVersion() {
        String latestVersion = null;
        try (InputStream xml = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream()) {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(xml);
            doc.getDocumentElement()
                    .normalize();
            latestVersion = getMajorVersion(filter(transform(new NodeIterable(doc.getElementsByTagName("version")), getVersion), and(notNull(), isValidVersion)));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            logger.error("Unable to retrieve the XML for DevKit metadata from Nexus repository. The rule 'DevKitLatestVersionCheck' won't be executed at this point", e);
        }
        return latestVersion;
    }

    public static String getMajorVersion(Iterable<String> versions) {
        String mayor = EMPTY;
        for (String version : versions) {
            if (compareTo(version, mayor) > 0) {
                mayor = version;
            }
        }
        return mayor;
    }

    public static boolean isRevision(String devKitVersion) {
        return devKitVersion.contains("-");
    }

    public static Integer compareTo(String a, String b) {
        Integer maxSize = max(a.length(), b.length());
        return convert(a, maxSize).compareTo(convert(b, maxSize));
    }

    public static String convert(String a, final Integer size) {
        return on(EMPTY).join(transform(Splitter.on(".")
                .split(a), new Function<String, String>() {

            @Override
            public String apply(String input) {
                return padStart(input, size, '0');
            }
        }));
    }
}