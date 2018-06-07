package com.mulesoft.connectivity.sonar.rules.exception;

import org.sonar.api.server.rule.RulesDefinition;

import static java.lang.String.format;

public class RuleNotCreatedException extends SonarRulesException {
    public RuleNotCreatedException(Class<?> ruleClass, RulesDefinition.NewRepository repository) {
        super(format("No rule was created for %s in %s.", ruleClass.getName(), repository.key()));
    }
}
