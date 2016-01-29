package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = TestCasesExtendAbstractTestCasesCheck.KEY, name = "TestCases classes should extend project's AbstractTestCase class", description = "Test Cases should extend project's AbstractTestCase class", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class TestCasesExtendAbstractTestCasesCheck extends BaseLoggingVisitor {

    public static final String KEY = "test-cases-extend-abstract-test-cases";

    @Override
    public final void visitClass(ClassTree classTree) {
        super.visitClass(classTree);
        final Symbol owner = classTree.symbol().owner();
        boolean isFunctional = owner.isPackageSymbol() && owner.name().endsWith("functional");
        boolean isTestClass = Iterables.any(classTree.members(), new Predicate<Tree>() {

            @Override
            public boolean apply(@Nullable Tree input) {
                if (input == null || !input.is(Tree.Kind.METHOD)) {
                    return false;
                }
                final MethodTree methodTree = (MethodTree) input;
                return Iterables.any(methodTree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate(Test.class));
            }
        });

        if (isTestClass && isFunctional && !classTree.symbol().type().isSubtypeOf("org.mule.tools.devkit.ctf.junit.AbstractTestCase")) {
            logAndRaiseIssue(classTree, String.format("Test case '%s' should inherit from AbstractTestCase.", classTree.simpleName().name()));
        }
    }

}
