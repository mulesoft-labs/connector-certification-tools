package com.mulesoft.connectivity.sonar.rules.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that marks a rule as having a fixed value function for remediation purposes.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ConstantRemediation {

    /**
     * The fixed value.
     *
     * @return int the value.
     */
    int value();
}
