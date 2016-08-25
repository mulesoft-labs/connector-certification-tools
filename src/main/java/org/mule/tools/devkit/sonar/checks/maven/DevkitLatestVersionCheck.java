package org.mule.tools.devkit.sonar.checks.maven;



import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.ConnectorIssue;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DevkitLatestVersionCheck implements MavenCheck {

    public static final String KEY = "devkit-latest-version";

    @Override
    public Iterable<ConnectorIssue> analyze(MavenProject mavenProject) {
        final List<ConnectorIssue> issues = Lists.newArrayList();
        Version currentVersion = new Version(mavenProject.getModel().getParent().getVersion());
        try{
            Version latestVersion = getLatestVersion();
            if(currentVersion.compareTo(latestVersion) == -1){
                logAndRaiseIssue(issues, String.format("Current Devkit version: '%s' is not up to date. Please download the latest version if possible: '%s'", mavenProject.getModel().getParent().getVersion(),latestVersion));
            }
        }catch(IOException|XMLStreamException e){
            logAndRaiseIssue(issues, "Error retrieving the XML");
        }
        return issues;
    }

    public Version getLatestVersion() throws IOException, XMLStreamException {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new URL("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/tools/devkit/mule-devkit-parent/maven-metadata.xml").openStream();
        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        Version latestVersion;
        Version currentVersion;
        String currentTag = "";
        String currentValue;
        streamReader.nextTag(); // Advance to "metadata" element
        do{
            streamReader.next();
            if(streamReader.hasName()){
                currentTag = streamReader.getName().toString();
            }
        }while(!currentTag.equals("versions") && streamReader.hasNext()); // Advance until we reach versions
        streamReader.nextTag(); // Advance into the first element inside versions
        streamReader.next(); // Advance into the value of the first version
        currentValue = streamReader.getText();
        latestVersion = new Version(currentValue);
        do{
            if(streamReader.isStartElement()){
                currentValue = streamReader.getElementText();
            }
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

        public int compareTo(Version latestVersion) {
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

    private final void logAndRaiseIssue(List<ConnectorIssue> issues, String message) {
        issues.add(new ConnectorIssue(KEY, message));
    }

}