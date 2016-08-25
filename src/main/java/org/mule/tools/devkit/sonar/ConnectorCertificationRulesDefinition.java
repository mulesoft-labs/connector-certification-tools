/*
 * SonarQube Java
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.mule.tools.devkit.sonar;

import com.google.common.collect.Iterables;
import org.mule.tools.devkit.sonar.checks.Check;
import org.reflections.Reflections;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

import java.util.Set;

import static com.google.common.base.Predicates.assignableFrom;
import static com.google.common.collect.Iterables.toArray;
import static java.util.stream.Collectors.toList;

public class ConnectorCertificationRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_NAME = "Connector Certification";
    public static final String JAVA_REPOSITORY_KEY = "connector-certification-java";

    @Override
    public void define(Context context) {
        Reflections reflections = new Reflections(getClass().getPackage().getName());
        Set<Class<?>> rules = reflections.getTypesAnnotatedWith(org.sonar.check.Rule.class);

        // Adding Java Rules.
        addRules(context, JAVA_REPOSITORY_KEY, "java", Iterables.concat(ConnectorsChecks.javaChecks(), ConnectorsChecks.javaTestChecks()));

        // Adding other Rules.
        reflections.getTypesAnnotatedWith(Class.class.cast(Check.class))
                .stream()
                .filter(checkType -> Class.class.cast(checkType).isAnnotationPresent(Check.class))
                .forEach(checkType -> {
                    Class<?> type = Class.class.cast(checkType);
                    Check annotation = type.getDeclaredAnnotation(Check.class);
                    addRules(context, annotation.repository(), annotation.language(), rules.stream().filter(assignableFrom(type)::apply).collect(toList()));
                });
    }

    private void addRules(Context context, String key, String language, Iterable<Class<?>> rules) {
        NewRepository repo = context.createRepository(key, language);
        repo.setName(REPOSITORY_NAME);
        new RulesDefinitionAnnotationLoader().load(repo, toArray(rules, Class.class));
        repo.done();
    }
}
