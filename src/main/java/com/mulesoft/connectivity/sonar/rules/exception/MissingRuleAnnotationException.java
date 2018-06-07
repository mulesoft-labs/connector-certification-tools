package com.mulesoft.connectivity.sonar.rules.exception;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

public class MissingRuleAnnotationException extends SonarRulesException {
    public MissingRuleAnnotationException(Class<?> ruleClass, Class<? extends Annotation>... missingAnnotations) {
        super(format("Either one of '%s' annotations is missing from rule class: %s", Stream.of(missingAnnotations)
                .map(Class::getSimpleName)
                .map(name -> format("@%s", name))
                .collect(joining(",")), ruleClass));
    }
}
