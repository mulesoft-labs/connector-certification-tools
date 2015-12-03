package org.mule.tools.devkit.sonar.checks.java;

import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class LicenseByCategoryCheckTest {

    private void runForCategory(String category) {
        runForCategory(category, category);
    }

    private void runForCategory(String category, String testClass) {
        MavenProject mavenProject = new MavenProject();
        mavenProject.getProperties().setProperty("category", category);
        LicenseByCategoryCheck check = new LicenseByCategoryCheck(mavenProject);

        JavaCheckVerifier.verify(String.format("src/test/files/java/licensechecks/LicenseByCategory%sCheck.java", testClass), check);
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
