package org.mule.tools.devkit.sonar.checks;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.sonar.api.config.Settings;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class LicenseByCategoryCheckTest {

    @NotNull
    private void runForCategory(String category) {
        runForCategory(category, category);
    }

    @NotNull
    private void runForCategory(String category, String testClass) {
        Settings settings = new Settings();
        settings.setProperty("category", category);
        LicenseByCategoryCheck check = new LicenseByCategoryCheck();
        check.settings = settings;
        JavaCheckVerifier.verify(String.format("src/test/files/licensechecks/LicenseByCategory%sCheck.java", testClass), check);
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
