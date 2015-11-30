//package org.mule.tools.devkit.sonar.checks;
//
//import org.apache.maven.model.Dependency;
//import org.apache.maven.project.MavenProject;
//import org.junit.Rule;
//import org.junit.Test;
//import org.sonar.java.JavaAstScanner;
//import org.sonar.java.model.VisitorsBridge;
//import org.sonar.squidbridge.api.SourceFile;
//import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ScopeProvidedInMuleDependenciesCheckTest {
//
//    @Rule
//    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();
//
//    @Test
//    public void checkScope() {
//
//        Dependency dependency = new Dependency();
//        dependency.setGroupId("org.mule.transports");
//        dependency.setArtifactId("mule-transport-vm");
//
//        MavenProject mavenProject = new MavenProject();
//        List<Dependency> dependencies = new ArrayList<>();
//        dependencies.add(dependency);
//        mavenProject.setDependencies(dependencies);
//
//        ScopeProvidedInMuleDependenciesCheck check = new ScopeProvidedInMuleDependenciesCheck(mavenProject);
//
//        SourceFile file = JavaAstScanner
//                .scanSingleFile(new File(String.format("src/test/files/ScopeProvidedInMuleDependenciesCheck.java")), new VisitorsBridge(check));
//
//        checkMessagesVerifier.verify(file.getCheckMessages())
//                .next().atLine(10).withMessage("Artifact 'mule-transport-vm' is a Mule dependency and should be declared with <scope>provided</scope>.");
//    }
//
//
//}
