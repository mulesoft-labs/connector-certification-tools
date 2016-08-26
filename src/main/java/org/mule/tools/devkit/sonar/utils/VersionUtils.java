package org.mule.tools.devkit.sonar.utils;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.NotNull;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class VersionUtils implements Comparable<VersionUtils> {

    private int major;
    private int minor;
    private int rev;

    public VersionUtils(String version) {
        List<String> tokens = Splitter.on(".").omitEmptyStrings().splitToList(version);
        this.major = Integer.parseInt(tokens.get(0));
        this.minor = Integer.parseInt(tokens.get(1));
        if (tokens.size() == 3) {
            this.rev = Integer.parseInt(tokens.get(2));
        } else {
            this.rev = 0;
        }
    }

    public int compareTo(@NotNull VersionUtils currentVersion) {
        if (this.major != currentVersion.major) {
            return Integer.compare(this.major, currentVersion.major);
        }
        if (this.minor != currentVersion.minor) {
            return Integer.compare(this.minor, currentVersion.minor);
        }
        if (this.rev != currentVersion.rev) {
            return Integer.compare(this.rev, currentVersion.rev);
        }
        return 0;
    }

    public static VersionUtils setLatestVersion() throws IOException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream();
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        VersionUtils latestVersion;
        VersionUtils currentVersion;
        // If == 1 its the start tag, if ==2 its the end tag
        int versionsTagCount = 0;
        String currentValue = "";
        // Advance to "metadata" element
        streamReader.nextTag();
        // Advance into the first element inside versions
        streamReader.nextTag();
        // Advance into the value of the first version
        streamReader.next();
        latestVersion = new VersionUtils("3.0.0");
        // Advance until we reach the end tag of versions (versionsTagCount == 2)
        while (versionsTagCount != 2 && streamReader.hasNext()) {
            if (streamReader.isStartElement() && versionsTagCount == 1) {
                currentValue = streamReader.getElementText();
            }
            /* It does not enter when the version has a - because those are test versions,
               and Devkit version 4.x.x is not taken into account for being a migration tool */
            if (currentValue.indexOf('-') < 0 && !currentValue.isEmpty() && currentValue.indexOf('4') != 0) {
                currentVersion = new VersionUtils(currentValue);
                currentVersion.replaceIfGreaterThan(latestVersion);
            }
            streamReader.next();
            if (streamReader.hasName() && streamReader.getName().toString().equals("versions")) {
                streamReader.nextTag();
                versionsTagCount++;
            }
            currentValue = "";
        }
        in.close();
        return latestVersion;
    }

    private void replaceIfGreaterThan(VersionUtils version) {
        if (this.minor > version.minor || this.minor == version.minor && this.rev > version.rev) {
            version.minor = this.minor;
            version.rev = this.rev;
        }
    }

    @Override
    public String toString() {
        return "" + major + "." + minor + "." + rev;
    }
}
