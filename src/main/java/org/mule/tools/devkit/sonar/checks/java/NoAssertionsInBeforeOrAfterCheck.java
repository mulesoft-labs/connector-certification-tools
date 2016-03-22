package org.mule.tools.devkit.sonar.checks.java;

import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

@Rule(key = NoAssertionsInBeforeOrAfterCheck.KEY, name = "No assertions allowed in methods annotated with @After or @Before.", description = "Assertions are meant to be used in tests, not during setup of tear down of the test objects.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
@ActivatedByDefault
public class NoAssertionsInBeforeOrAfterCheck extends BaseLoggingVisitor {

    public static final String KEY = "no-assertions-in-before-or-after";

    public final boolean isAnnotatedWithBeforeOrAfter(MethodTree tree) {
        return Iterables.any(tree.modifiers().annotations(),
                Predicates.or(ClassParserUtils.hasAnnotationPredicate("org.junit.After"), ClassParserUtils.hasAnnotationPredicate("org.junit.Before")));
    }

    @Override
    public void visitMethod(MethodTree methodTree) {
        BlockTree block = methodTree.block();
        if (block != null && isAnnotatedWithBeforeOrAfter(methodTree)) {
            block.accept(new AssertionsVisitor());
        }
    }

    private class AssertionsVisitor extends BaseTreeVisitor {

        @Override
        public void visitMethodInvocation(MethodInvocationTree tree) {
            final IdentifierTree identifier;
            if (tree.methodSelect().is(Tree.Kind.MEMBER_SELECT)) {
                identifier = ((MemberSelectExpressionTree) tree.methodSelect()).identifier();
            } else {
                identifier = (IdentifierTree) tree.methodSelect();
            }
            if (identifier.name().startsWith("assert")) {
                logAndRaiseIssue(tree, "No assertions allowed in methods annotated with @After or @Before.");
            }

            super.visitMethodInvocation(tree);
        }
    }
}
