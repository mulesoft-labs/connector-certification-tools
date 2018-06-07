package com.mulesoft.connectivity.sonar.rules.model;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation that marks a rule as having a linear remediation function. This function can also have an offset.
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface LinearRemediation {

    /**
     * The incremental value.
     *
     * @return int the value.
     */
    int value();

    /**
     * The offset value.
     *
     * @return int the value.
     */
    int offset() default 0;

    /**
     * This description explains what 1 point of "gap" represents for the rule.
     * <br>
     * Example: for the "Insufficient condition coverage", this description for the
     * remediation function gap multiplier/base effort would be something like
     * "Effort to test one uncovered condition".
     *
     * @return String the definition of 1 point of "gap".
     */
    String gapDefinition();
}
