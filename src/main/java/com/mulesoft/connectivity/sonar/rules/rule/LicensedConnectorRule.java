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
package com.mulesoft.connectivity.sonar.rules.rule;

import com.mulesoft.connectivity.sonar.rules.model.Bug;
import com.mulesoft.connectivity.sonar.rules.model.ConstantRemediation;
import com.mulesoft.connectivity.sonar.rules.model.Vulnerability;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.license.RequiresEnterpriseLicense;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.sonar.check.Priority.BLOCKER;
import static org.sonar.plugins.java.api.tree.Tree.Kind.CLASS;

@Rule(key = "LicensedConnectorRule",
        name = "Licensed Connector Rule",
        description = "<p>All Mule4 connectors should be licensed. As such, they should include the type of connector they are " +
                "(either SELECT or PREMIUM). This connector is missing the annotations required for such licensing.",
        priority = BLOCKER
)
@Bug
@ConstantRemediation(60)
public class LicensedConnectorRule extends IssuableSubscriptionVisitor implements JavaCheck {

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Stream.of(CLASS).collect(toList());
    }

    @Override
    public void visitNode(Tree tree) {
        Optional.of(ClassTree.class.cast(tree).modifiers().annotations().stream()
                .map(AnnotationTree::annotationType)
                .map(Object::toString)
                .collect(toList()))
                .filter(list -> list.contains(Extension.class.getSimpleName()))
                .filter(list -> !list.contains(RequiresEnterpriseLicense.class.getSimpleName()))
                .ifPresent(list -> reportIssue(tree, "Unlicensed Mule 4 Connector."));
    }
}
