package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.model.expression.AssignmentExpressionTreeImpl;
import org.sonar.java.model.expression.IdentifierTreeImpl;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = MinMuleVersionCheck.KEY, name = "Connectors should specify a minMuleVersion", description = "The minMuleVersion should be specified on the @Connector annotation.", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class MinMuleVersionCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "min-mule-version";

    @Override
    protected void verifyConnector(ClassTree classTree, IdentifierTree connectorAnnotation) {

        for (AnnotationTree annotationTree : Iterables.filter(classTree.modifiers().annotations(), ClassParserUtils.ANNOTATION_TREE_PREDICATE)) {
            if (ClassParserUtils.is(annotationTree, "org.mule.api.annotations.Connector")) {
                for(ExpressionTree expression : annotationTree.arguments()){
                    if(expression.kind().name().equals("ASSIGNMENT")){
                        if(((IdentifierTreeImpl) ((AssignmentExpressionTreeImpl) expression).variable()).name().equals("minMuleVersion")){
                            return;
                        }
                    }
                }

                logAndRaiseIssue(annotationTree, "The connector should specify a minMuleVersion");
                return;
            }
        }
    }
}
