package com.mulesoft.connectivity.sonar.rules.exception;

import static java.lang.String.format;

public class CardinalityNotSupportedException extends SonarRulesException {
    public CardinalityNotSupportedException(Class<?> ruleClass) {
        super(format("Cardinality is not supported, use the RuleTemplate annotation instead for %s", ruleClass.getName()));
    }
}
