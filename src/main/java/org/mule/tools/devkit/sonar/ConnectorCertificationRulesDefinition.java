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
import org.reflections.Reflections;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;

public class ConnectorCertificationRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_NAME = "Connector Certification";
    private static final String JAVA_REPOSITORY_KEY = "connector-certification-java";
    private static final String POM_REPOSITORY_KEY = "connector-certification-mvn";
    private static final String STRUCT_REPOSITORY_KEY = "connector-certification-struct";
    private static final String GIT_REPOSITORY_KEY = "connector-certification-git";

    public static String getJavaRepositoryKey() {
        return JAVA_REPOSITORY_KEY;
    }

    public static String getMvnRepositoryKey() {
        return POM_REPOSITORY_KEY;
    }

    public static String getStructRepositoryKey() {
        return STRUCT_REPOSITORY_KEY;
    }

    @Override
    public void define(Context context) {
        addJavaRules(context);
        addMavenRules(context);
        addStructureRules(context);
        addGitRules(context);
    }

    private void addJavaRules(Context context) {
        NewRepository repo = context.createRepository(JAVA_REPOSITORY_KEY, "java");
        repo.setName(REPOSITORY_NAME);

        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        annotationLoader.load(repo, Iterables.toArray(ConnectorsChecks.javaChecks(), Class.class));
        annotationLoader.load(repo, Iterables.toArray(ConnectorsChecks.javaTestChecks(), Class.class));
        repo.done();
    }

    private void addMavenRules(Context context) {
        NewRepository repo = context.createRepository(POM_REPOSITORY_KEY, "mvn");
        repo.setName(REPOSITORY_NAME);

        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        annotationLoader.load(repo, Iterables.toArray(ConnectorsChecks.mavenChecks(), Class.class));
        repo.done();
    }

    private void addStructureRules(Context context) {
        NewRepository repo = context.createRepository(STRUCT_REPOSITORY_KEY, "struct");
        repo.setName(REPOSITORY_NAME);

        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        annotationLoader.load(repo, Iterables.toArray(ConnectorsChecks.structureChecks(), Class.class));
        repo.done();
    }

    private void addGitRules(Context context) {
        NewRepository repo = context.createRepository(GIT_REPOSITORY_KEY, "git");
        repo.setName(REPOSITORY_NAME);

        RulesDefinitionAnnotationLoader annotationLoader = new RulesDefinitionAnnotationLoader();
        annotationLoader.load(repo, Iterables.toArray(new Reflections(getClass().getPackage().getName()).getTypesAnnotatedWith(org.sonar.check.Rule.class), Class.class));
        repo.done();
    }
}
