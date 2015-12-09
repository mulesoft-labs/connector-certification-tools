package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.java.model.expression.IdentifierTreeImpl;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = TestSuiteCheck.KEY, name = "Test Suite classes need to be annotated with @RunWith(Suite.class)", tags = { "connector-certification" })
@ActivatedByDefault
public class TestSuiteCheck extends BaseLoggingVisitor {

    private static final Logger logger = LoggerFactory.getLogger(TestSuiteCheck.class);
    public static final String KEY = "test-suite-annotations";

    @Override
    public final void visitClass(ClassTree tree) {
        if (tree.simpleName().name().endsWith("TestSuite")) {
            final AnnotationTree runWithAnnotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate(RunWith.class), null);
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
                        if (!((IdentifierTreeImpl) expressionTree).name().equals(Suite.class.getSimpleName())) {
                            logAndRaiseIssue(tree, String.format(
                                    "Found @RunWith annotation on Test Suite class '%s', but different runner specified (%s.class instead of %s.class).", tree.simpleName().name(),
                                    ((IdentifierTreeImpl) expressionTree).name(), Suite.class.getSimpleName()));
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

}
