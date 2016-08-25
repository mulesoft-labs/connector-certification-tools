package org.mule.tools.devkit.sonar.checks.maven;


import com.google.common.base.Splitter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mule.tools.devkit.sonar.utils.PomUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
public class DevkitLatestVersionCheckTest {

    @Test
    public void checkDevkitVersionIsTrue() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-is-latest-version"));
        Version currentVersion = new Version(mavenProject.getModel().getParent().getVersion());
        Version latestVersion = getLatestVersion();
        assertThat(currentVersion.compareTo(latestVersion), is(0));
    }

    @Test
    public void checkDevkitVersionIsFalse() throws IOException, XmlPullParserException, XMLStreamException {
        final MavenProject mavenProject = PomUtils.createMavenProjectFromPomFile(new File("src/test/files/maven/devkit-latest-version/devkit-is-not-latest-version"));
        Version currentVersion = new Version(mavenProject.getModel().getParent().getVersion());
        Version latestVersion = getLatestVersion();
        assertThat(currentVersion.compareTo(latestVersion), is(-1));
    }

    public Version getLatestVersion() throws IOException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream();
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        Version latestVersion;
        Version currentVersion;
        String currentTag = "";
        String currentValue;
        // Advance to "metadata" element
        streamReader.nextTag();
        do{
            streamReader.next();
            if(streamReader.hasName()){
                currentTag = streamReader.getName().toString();
            }
        }
        // Advance until we reach versions
        while(!currentTag.equals("versions") && streamReader.hasNext());
        // Advance into the first element inside versions
        streamReader.nextTag();
        // Advance into the value of the first version
        streamReader.next();
        currentValue = streamReader.getText();
        latestVersion = new Version(currentValue);
        do{
            if(streamReader.isStartElement()){
                currentValue = streamReader.getElementText();
            }
            /* It does not enter when the version has a - because those are test versions,
             and Devkit version 4.x.x is not taken into account for being a migration tool */
            if(currentValue.indexOf('-') < 0 && !currentValue.isEmpty() && currentValue.indexOf('4') != 0){
                currentVersion = new Version(currentValue);
                currentVersion.replaceIfGreaterThan(latestVersion);
            }
            streamReader.next();
            if(streamReader.hasName()){
                currentTag = streamReader.getName().toString();
            }
            currentValue = "";
        }while(!currentTag.equals("versions") && streamReader.hasNext());
        in.close();
        return latestVersion;
    }

    class Version implements Comparable<Version> {
        int major;
        int minor;
        int rev;

        public int compareTo(@NotNull Version latestVersion) {
            if (this.major != latestVersion.major) {
                return Integer.compare(this.major, latestVersion.major);
            }
            if (this.minor != latestVersion.minor) {
                return Integer.compare(this.minor, latestVersion.minor);
            }
            if (this.rev != latestVersion.rev) {
                return Integer.compare(this.rev, latestVersion.rev);
            }
            return 0;
        }

        public void replaceIfGreaterThan(Version version){
            if(this.minor > version.minor || this.minor == version.minor && this.rev > version.rev){
                version.minor = this.minor;
                version.rev = this.rev;
            }
        }

        public Version (String version) {
            List<String> tokens = Splitter.on(".").omitEmptyStrings().splitToList(version);
            this.major = Integer.parseInt(tokens.get(0));
            this.minor = Integer.parseInt(tokens.get(1));
            if(tokens.size() == 3) {
                this.rev = Integer.parseInt(tokens.get(2));
            }
            else {
                this.rev = 0;
            }
        }

        @Override
        public String toString() {
            return "" + major + "." + minor + "." + rev;
        }
    }
}