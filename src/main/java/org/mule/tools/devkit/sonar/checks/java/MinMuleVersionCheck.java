package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.model.expression.AssignmentExpressionTreeImpl;
import org.sonar.java.model.expression.IdentifierTreeImpl;
import org.sonar.java.model.expression.LiteralTreeImpl;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = MinMuleVersionCheck.KEY, name = "Connectors should specify a minMuleVersion", description = "The 'minMuleVersion' should be specified in the @Connector annotation.", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class MinMuleVersionCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "min-mule-version";

    @Override
    protected void verifyConnector(ClassTree classTree, IdentifierTree connectorAnnotation) {

        //Iterate through the annotations until finding @Connector
        for (AnnotationTree annotationTree : Iterables.filter(classTree.modifiers().annotations(), ClassParserUtils.ANNOTATION_TREE_PREDICATE)) {
            if (ClassParserUtils.is(annotationTree, "org.mule.api.annotations.Connector")) {

                //Iterate through the arguments until finding minMuleVersion
                for(ExpressionTree expression : annotationTree.arguments()){
                    if(expression.kind().name().equals("ASSIGNMENT") && ((IdentifierTreeImpl) ((AssignmentExpressionTreeImpl) expression).variable()).name().equals("minMuleVersion")){

                        //Make sure the minMuleVersion argument is set as an string value
                        if(!((AssignmentExpressionTreeImpl) expression).expression().symbolType().isSubtypeOf(String.class.getTypeName())){
                            logAndRaiseIssue(annotationTree, "The value of minMuleVersion must be a string");
                            return;
                        }

                        //`length == 2` means it only opens and closes the string ("")
                        if(((String)((LiteralTreeImpl) ((AssignmentExpressionTreeImpl) expression).expression()).value()).toCharArray().length == 2){
                            logAndRaiseIssue(annotationTree, "The value of minMuleVersion can not be empty");
                            return;
                        }

                        //At this point, the argument exists and is valid. Stop checking.
                        return;
                    }
                }

                //If not valid argument has been found, raise an issue.
                logAndRaiseIssue(annotationTree, "The connector should specify a minMuleVersion");
            }
        }
    }
}
