/*
 * SonarQube Java Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.mulesoft.connectivity.sonar.rules;

import com.mulesoft.connectivity.sonar.rules.exception.CardinalityNotSupportedException;
import com.mulesoft.connectivity.sonar.rules.exception.MissingRuleAnnotationException;
import com.mulesoft.connectivity.sonar.rules.exception.MissingRuleFieldException;
import com.mulesoft.connectivity.sonar.rules.exception.RuleNotCreatedException;
import com.mulesoft.connectivity.sonar.rules.model.Bug;
import com.mulesoft.connectivity.sonar.rules.model.CodeSmell;
import com.mulesoft.connectivity.sonar.rules.model.ConstantRemediation;
import com.mulesoft.connectivity.sonar.rules.model.LinearRemediation;
import com.mulesoft.connectivity.sonar.rules.model.Vulnerability;
import com.mulesoft.connectivity.sonar.rules.rule.PackageMarker;
import org.reflections.Reflections;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.java.RspecKey;
import org.sonar.squidbridge.annotations.RuleTemplate;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.collect.Iterables.toArray;
import static java.lang.String.format;
import static java.util.function.Predicate.isEqual;
import static java.util.stream.Collectors.joining;
import static org.sonar.api.internal.apachecommons.lang.StringUtils.splitByCharacterTypeCamelCase;
import static org.sonar.api.utils.AnnotationUtils.getAnnotation;
import static org.sonar.check.Cardinality.MULTIPLE;

/**
 * Declare rule metadata in server repository of rules.
 * That allows to list the rules in the page "Rules".
 */
public class MuleSoftMule4ConnectorRulesDefinition implements RulesDefinition {

    public static final String JAVA_REPOSITORY_KEY = "mulesoft-java";

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(JAVA_REPOSITORY_KEY, "java")
                .setName("MuleSoft Mule 4 Connector Rules Repository");
        String rulesPackage = PackageMarker.class.getPackage().getName();
        Set<Class<?>> rules = new Reflections(rulesPackage).getTypesAnnotatedWith(org.sonar.check.Rule.class);
        new RulesDefinitionAnnotationLoader().load(repository, toArray(rules, Class.class));

        for (Class ruleClass : rules) {
            org.sonar.check.Rule ruleAnnotation = getAnnotation(ruleClass, org.sonar.check.Rule.class);
            String ruleKey = ruleAnnotation.key();
            validateField(ruleKey, "key", ruleClass);
            validateField(ruleAnnotation.name(), "name", ruleClass);
            validateField(ruleAnnotation.description(), "description", ruleClass);
            validateField(ruleAnnotation.priority(), "priority", ruleClass);
            NewRule rule = Optional.ofNullable(repository.rule(ruleKey)).orElseThrow(() -> new RuleNotCreatedException(ruleClass, repository));
            Optional.ofNullable(getAnnotation(ruleClass, RspecKey.class))
                    .map(RspecKey::value)
                    .ifPresent(rule::setInternalKey);
            rule.setHtmlDescription(ruleAnnotation.description());
            rule.setSeverity(ruleAnnotation.priority().name());
            rule.setName(ruleAnnotation.name());
            rule.setHtmlDescription(ruleAnnotation.description());
            rule.setTags(ruleAnnotation.tags());
            rule.setStatus(RuleStatus.valueOf(ruleAnnotation.status()));
            rule.setType(RuleType.valueOf(Stream.of(splitByCharacterTypeCamelCase(Optional.ofNullable(Optional.<Annotation>ofNullable(getAnnotation(ruleClass, Vulnerability.class))
                    .orElse(Optional.<Annotation>ofNullable(getAnnotation(ruleClass, Bug.class))
                            .orElse(Optional.ofNullable(getAnnotation(ruleClass, CodeSmell.class))
                                    .orElse(null))))
                    .orElseThrow(() -> new MissingRuleAnnotationException(ruleClass, Vulnerability.class, Bug.class, CodeSmell.class))
                    .annotationType().getSimpleName().toUpperCase()))
                    .collect(joining("_"))));
            rule.setDebtRemediationFunction(Optional.ofNullable(Optional.ofNullable(getAnnotation(ruleClass, ConstantRemediation.class))
                    .map(ConstantRemediation::value)
                    .map(Object::toString)
                    .map(value -> format("%smin", value))
                    .map(rule.debtRemediationFunctions()::constantPerIssue)
                    .orElse(Optional.ofNullable(getAnnotation(ruleClass, LinearRemediation.class))
                            .map(annotation -> rule.debtRemediationFunctions().linearWithOffset(String.valueOf(annotation.value()), String.valueOf(annotation.offset())))
                            .orElse(null)))
                    .orElseThrow(() -> new MissingRuleAnnotationException(ruleClass, ConstantRemediation.class, LinearRemediation.class)));
            Optional.ofNullable(getAnnotation(ruleClass, LinearRemediation.class)).map(LinearRemediation::gapDefinition).ifPresent(rule::setGapDescription);
            rule.setTemplate(getAnnotation(ruleClass, RuleTemplate.class) != null);
            Optional.of(ruleAnnotation.cardinality())
                    .filter(isEqual(MULTIPLE))
                    .ifPresent(value -> {
                        throw new CardinalityNotSupportedException(ruleClass);
                    });
        }
        repository.done();
    }

    private void validateField(Object value, String fieldName, Class<?> ruleClass) {
        Optional.ofNullable(value)
                .map(Object::toString)
                .filter(isEqual("").negate())
                .orElseThrow(() -> new MissingRuleFieldException(fieldName, ruleClass));
    }
}
