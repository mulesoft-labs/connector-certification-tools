package org.mule.tools.devkit.sonar.checks;

public class ConnectorIssue {

    private final String ruleKey;
    private final String message;

    public ConnectorIssue(String ruleKey, String message) {
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
