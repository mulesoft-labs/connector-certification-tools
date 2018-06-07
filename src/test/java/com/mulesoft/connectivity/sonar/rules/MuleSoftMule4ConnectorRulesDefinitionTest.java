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

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static com.mulesoft.connectivity.sonar.rules.MuleSoftMule4ConnectorRulesDefinition.JAVA_REPOSITORY_KEY;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.sonar.api.rules.RuleType.BUG;
import static org.sonar.api.server.debt.DebtRemediationFunction.Type.CONSTANT_ISSUE;

public class MuleSoftMule4ConnectorRulesDefinitionTest {

    @Test
    public void test() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        new MuleSoftMule4ConnectorRulesDefinition().define(context);
        RulesDefinition.Repository repository = context.repository(JAVA_REPOSITORY_KEY);

        assertThat(repository.name(), equalTo("MuleSoft Mule 4 Connector Rules Repository"));
        assertThat(repository.language(), equalTo("java"));

        Rule avoidAnnotationRule = repository.rule("LicensedConnectorRule");
        assertThat(avoidAnnotationRule, notNullValue());
        assertThat(avoidAnnotationRule.name(), equalTo("Licensed Connector Rule"));
        assertThat(avoidAnnotationRule.debtRemediationFunction().type(), equalTo(CONSTANT_ISSUE));
        assertThat(avoidAnnotationRule.type(), equalTo(BUG));
    }
}
