package com.mulesoft.connectivity.sonar.rules.exception;

import static java.lang.String.format;

public class MissingRuleFieldException extends SonarRulesException {
    public MissingRuleFieldException(String fieldType, Class<?> ruleClass) {
        super(format("Missing field '%s' for Rule %s", fieldType, ruleClass.getName()));
    }
}
