package com.mulesoft.connectivity.sonar.rules.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker annotation that identifies a rule as a Code Smell.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface CodeSmell {
}
