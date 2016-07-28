package org.mule.tools.devkit.sonar.checks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define the language of a check.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface Check {
    String language();
    String repository();
}
