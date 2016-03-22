package org.mule.tools.devkit.sonar.checks.java;

import java.util.List;

import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.checks.helpers.ExpressionsHelper;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import com.google.common.collect.Iterables;

@Rule(key = TestSuiteCheck.KEY, name = "Test Suite classes need to be annotated with @RunWith(Suite.class)", description = "Test Suite classes need to be annotated with @RunWith(Suite.class)", priority = Priority.CRITICAL, tags = { "connector-certification"
})
@ActivatedByDefault
public class TestSuiteCheck extends BaseLoggingVisitor {

    public static final String KEY = "test-suite-annotations";

    @Override
    public final void visitClass(ClassTree tree) {
        IdentifierTree treeName = tree.simpleName();
        if (treeName != null && treeName.name().endsWith("TestSuite")) {
            final AnnotationTree runWithAnnotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate("org.junit.runner.RunWith"), null);
            if (runWithAnnotation == null) {
                logAndRaiseIssue(tree, String.format("Missing @RunWith annotation on Test Suite class '%s'.", tree.simpleName().name()));
            } else {
                final List<ExpressionTree> arguments = runWithAnnotation.arguments();
                if (arguments.isEmpty()) {
                    logAndRaiseIssue(tree,
                            String.format("Found @RunWith annotation on Test Suite class '%s', but no runner specified. It should be Suite.class.", tree.simpleName().name()));
                } else {
                    final ExpressionTree argument = Iterables.getOnlyElement(arguments);
                    if (argument.is(Tree.Kind.MEMBER_SELECT)) {
                        final ExpressionTree expressionTree = ((MemberSelectExpressionTree) argument).expression();
                        if (expressionTree.is(Tree.Kind.IDENTIFIER)) {
                            final IdentifierTree identifierTree = (IdentifierTree) expressionTree;
                            if (!identifierTree.name().equals("Suite")) {
                                logAndRaiseIssue(tree, String.format(
                                        "Found @RunWith annotation on Test Suite class '%s', but different runner specified (%s.class instead of %s.class).", tree.simpleName()
                                                .name(), identifierTree.name(), "Suite"));
                            }
                        } else if (expressionTree.is(Tree.Kind.MEMBER_SELECT)) {
                            final MemberSelectExpressionTree memberSelectExpressionTree = (MemberSelectExpressionTree) expressionTree;
                            final String fullyQualifiedClassName = ExpressionsHelper.concatenate(memberSelectExpressionTree);
                            if (!fullyQualifiedClassName.equals("org.junit.runners.Suite")) {
                                logAndRaiseIssue(tree, String.format(
                                        "Found @RunWith annotation on Test Suite class '%s', but different runner specified (%s.class instead of %s.class).", tree.simpleName()
                                                .name(), fullyQualifiedClassName, "Suite"));
                            }
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

}
