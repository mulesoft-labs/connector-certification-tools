package org.mule.tools.devkit.sonar.checks.java;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class ConfigFriendlyNameCheckTest {

    @Test
    public void runWithMissingFriendlyName() {
        ConfigFriendlyNameCheck check = new ConfigFriendlyNameCheck();
        JavaCheckVerifier.verify("src/test/files/java/ConfigFriendlyNameCheckMissingFriendlyName.java", check);
    }

    @Test
    public void runWithSingleConfig() {
        ConfigFriendlyNameCheck check = new ConfigFriendlyNameCheck();
        JavaCheckVerifier.verify("src/test/files/java/ConfigFriendlyNameCheckSingleConfig.java", check);
    }

    @Test
    public void runWithMultipleConfig() {
        ConfigFriendlyNameCheck check = new ConfigFriendlyNameCheck();
        JavaCheckVerifier.verify("src/test/files/java/ConfigFriendlyNameCheckMultipleConfig.java", check);
    }

}
