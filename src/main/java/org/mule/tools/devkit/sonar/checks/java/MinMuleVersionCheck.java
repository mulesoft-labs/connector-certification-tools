package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.model.expression.AssignmentExpressionTreeImpl;
import org.sonar.java.model.expression.IdentifierTreeImpl;
import org.sonar.java.model.expression.LiteralTreeImpl;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;

import javax.annotation.Nullable;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.isEmpty;
import static org.apache.commons.lang.StringUtils.replace;
import static org.mule.tools.devkit.sonar.checks.maven.Version.hasValidFormat;
import static org.mule.tools.devkit.sonar.utils.ClassParserUtils.ANNOTATION_TREE_PREDICATE;
import static org.mule.tools.devkit.sonar.utils.ClassParserUtils.is;

@Rule(key = MinMuleVersionCheck.KEY, name = "Attribute 'minMuleVersion' should be declared in @Connector", description = "Connectors should explicitly declare a 'minMuleVersion' inside @Connector, which is the minimum Mule version required. It must also follow the semantic versioning MAJOR.MINOR or MAJOR.MINOR.PATCH. For example, minMuleVersion = \"3.7\".", priority = Priority.CRITICAL, tags = { "connector-certification" })
public class MinMuleVersionCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "min-mule-version";

    @Override
    protected void verifyConnector(ClassTree classTree, IdentifierTree connectorAnnotation) {
        for (AnnotationTree annotationTree : filter(classTree.modifiers().annotations(), ANNOTATION_TREE_PREDICATE)) {
            if (is(annotationTree, "org.mule.api.annotations.Connector")) {
                if(isEmpty(annotationTree.arguments()) || isEmpty(filter(annotationTree.arguments(), new Predicate<ExpressionTree>() {

                    @Override
                    public boolean apply(@Nullable ExpressionTree expression) {
                        return expression.kind().name().equals("ASSIGNMENT")
                                && ((IdentifierTreeImpl) ((AssignmentExpressionTreeImpl) expression).variable()).name().equals("minMuleVersion")
                                && hasValidFormat(replace((((LiteralTreeImpl) ((AssignmentExpressionTreeImpl) expression).expression()).token().text()), "\"", ""));
                    }
                }))) {
                    logAndRaiseIssue(annotationTree, "@Connector should declare a 'minMuleVersion'.");
                }
            }
        }
    }
}
