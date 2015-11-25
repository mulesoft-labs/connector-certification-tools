package org.mule.tools.devkit.sonar;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
import org.mule.tools.devkit.sonar.checks.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.NumberOfArgumentsInProcessorCheck;
import org.mule.tools.devkit.sonar.checks.RefOnlyInComplexTypesCheck;
import org.sonar.api.BatchExtension;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannersFactory;

import java.util.List;

public class ConnectorCertificationCheckRegistrar implements BatchExtension, JavaFileScannersFactory {

    private final MavenProject project;

    public ConnectorCertificationCheckRegistrar(MavenProject project) {
        this.project = project;
    }

    @Override
    public Iterable<JavaFileScanner> createJavaFileScanners() {
        List<JavaFileScanner> scanners = Lists.newArrayList();
        scanners.add(new RefOnlyInComplexTypesCheck());
        scanners.add(new NumberOfArgumentsInProcessorCheck());
        scanners.add(new LicenseByCategoryCheck(project));
        return scanners;
    }

}
