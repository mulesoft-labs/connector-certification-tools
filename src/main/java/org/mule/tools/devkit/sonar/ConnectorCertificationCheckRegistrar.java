package org.mule.tools.devkit.sonar;

import com.google.common.collect.Lists;
import org.apache.maven.project.MavenProject;
<<<<<<< HEAD
import org.mule.tools.devkit.sonar.checks.*;
=======
import org.mule.tools.devkit.sonar.checks.LicenseByCategoryCheck;
import org.mule.tools.devkit.sonar.checks.NumberOfArgumentsInProcessorCheck;
import org.mule.tools.devkit.sonar.checks.RefOnlyInComplexTypesCheck;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
>>>>>>> develop
import org.sonar.api.BatchExtension;
import org.sonar.api.batch.ProjectClasspath;
import org.sonar.api.resources.Project;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannersFactory;

import java.util.List;

public class ConnectorCertificationCheckRegistrar implements BatchExtension, JavaFileScannersFactory {

    private final MavenProject mavenProject;

    public ConnectorCertificationCheckRegistrar(MavenProject mavenProject, ProjectClasspath projectClasspath, Project project) {
        this.mavenProject = mavenProject;
        ClassParserUtils.PROJECT_CLASSPATH_THREAD_LOCAL.set(projectClasspath);
    }

    @Override
    public Iterable<JavaFileScanner> createJavaFileScanners() {
        List<JavaFileScanner> scanners = Lists.newArrayList();
        scanners.add(new RefOnlyInComplexTypesCheck());
        scanners.add(new NumberOfArgumentsInProcessorCheck());
        scanners.add(new LicenseByCategoryCheck(mavenProject));
//        scanners.add(new ScopeProvidedInMuleDependenciesCheck(project));
//        scanners.add(new TestingFrameworkNotOverwrittenCheck(project));
        return scanners;
    }

}
