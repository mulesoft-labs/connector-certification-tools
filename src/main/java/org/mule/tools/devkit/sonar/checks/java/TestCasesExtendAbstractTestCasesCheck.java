package org.mule.tools.devkit.sonar.checks.java;

import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = TestCasesExtendAbstractTestCasesCheck.KEY, name = "TestCases classes should extend project's AbstractTestCase class", description = "Test Cases should extend project's AbstractTestCase class", priority = Priority.CRITICAL, tags = { "connector-certification"
})
@ActivatedByDefault
public class TestCasesExtendAbstractTestCasesCheck extends BaseLoggingVisitor {

    public static final String KEY = "test-cases-extend-abstract-test-cases";

    @Override
    public final void visitClass(ClassTree classTree) {
        super.visitClass(classTree);
        final Symbol owner = classTree.symbol().owner();
        boolean isFunctional = owner.isPackageSymbol() && owner.name().endsWith("functional");
        boolean isTestClass = ClassParserUtils.isTestClass(classTree);

        if (isTestClass && isFunctional && !classTree.symbol().type().isSubtypeOf("org.mule.tools.devkit.ctf.junit.AbstractTestCase")) {
            logAndRaiseIssue(classTree, String.format("Test case '%s' should inherit from AbstractTestCase.", classTree.simpleName().name()));
        }
    }

}
