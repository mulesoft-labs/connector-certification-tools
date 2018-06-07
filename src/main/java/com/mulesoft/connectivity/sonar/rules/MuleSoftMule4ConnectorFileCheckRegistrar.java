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

import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

import java.util.ArrayList;

import static com.mulesoft.connectivity.sonar.rules.MuleSoftMule4ConnectorRulesDefinition.JAVA_REPOSITORY_KEY;
import static org.reflections.Reflections.collect;

/**
 * Provide the "rule" (implementations of rules) classes that are going be executed during source code analysis.
 * <p>
 * This class is a batch extension by implementing the {@link CheckRegistrar} interface.
 */
@SonarLintSide
public class MuleSoftMule4ConnectorFileCheckRegistrar implements CheckRegistrar {

    /**
     * Register the classes that will be used to instantiate rule during analysis.
     */
    @Override
    public void register(RegistrarContext registrarContext) {

        // Call to registerClassesForRepository to associate the classes with the correct repository key
        registrarContext.registerClassesForRepository(JAVA_REPOSITORY_KEY, collect(getClass().getPackage().getName(), value -> true).getSubTypesOf(JavaCheck.class), new ArrayList<>());
    }
}
