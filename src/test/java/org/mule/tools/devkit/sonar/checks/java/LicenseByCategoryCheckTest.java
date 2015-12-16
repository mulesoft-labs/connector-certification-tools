package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import java.io.File;

public class LicenseByCategoryCheckTest {

    private void runForCategory(String category) {
        runForCategory(category, category);
    }

    private void runForCategory(String category, String testClass) {
        // MavenProject mavenProject = new MavenProject();
        // mavenProject.getProperties().setProperty("category", category);
        FileSystem fileSystem = new DefaultFileSystem().setBaseDir(new File(String.format("src/test/files/java/licensechecks/%s", category.toLowerCase())));
        LicenseByCategoryCheck check = new LicenseByCategoryCheck(fileSystem);

        JavaCheckVerifier.verify(String.format("src/test/files/java/licensechecks/%s/LicenseByCategory%sCheck.java", category.toLowerCase(), testClass), check);
    }

    @Test
    public void checkCommunity() {
        runForCategory("Community");
    }

    @Test
    public void checkCertified() {
        runForCategory("Certified");
    }

    @Test
    public void checkSelect() {
        runForCategory("Select");
    }

    @Test
    public void checkPremiumNoRequiresLicense() {
        runForCategory("Premium", "PremiumNoRequiresLicense");
    }

    @Test
    public void checkPremiumNoNameAttribute() {
        runForCategory("Premium", "PremiumNoNameAttribute");
    }

    @Test
    public void checkInvalid() {
        runForCategory("Invalid");
    }

}
