package org.mule.tools.devkit.sonar.checks.pom;

public class PomIssue {

    private final String ruleKey;
    private final String message;

    public PomIssue(String ruleKey, String message) {
        this.ruleKey = ruleKey;
        this.message = message;
    }

    public String ruleKey() {
        return ruleKey;
    }

    public String message() {
        return message;
    }
}
