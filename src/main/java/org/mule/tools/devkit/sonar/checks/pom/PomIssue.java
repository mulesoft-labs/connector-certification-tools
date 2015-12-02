package org.mule.tools.devkit.sonar.checks.pom;

import org.sonar.api.rule.RuleKey;

public class PomIssue {

    private final RuleKey ruleKey;
    private final String message;

    public PomIssue(RuleKey ruleKey, String message) {
        this.ruleKey = ruleKey;
        this.message = message;
    }

    public RuleKey ruleKey() {
        return ruleKey;
    }

    public String message() {
        return message;
    }
}
