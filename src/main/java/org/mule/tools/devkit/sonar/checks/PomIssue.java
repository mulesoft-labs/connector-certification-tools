package org.mule.tools.devkit.sonar.checks;

import org.sonar.api.rule.RuleKey;

public class PomIssue {

    private final RuleKey ruleKey;
    private final String message;

    public PomIssue(RuleKey ruleKey, String message) {
        this.ruleKey = ruleKey;
        this.message = message;
    }

    RuleKey ruleKey() {
        return ruleKey;
    }

    String message() {
        return message;
    }
}
