package org.mule.tools.devkit.sonar.checks.java;

import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck.KEY, name = "MetaDataTestCases classes should extend CTF's AbstractMetaDataTestCase class", description = "MetaData Test Cases should extend CTFs AbstractMetaDataTestCase class", priority = Priority.CRITICAL, tags = {
        "connector-certification"
})
@ActivatedByDefault
public class MetaDataTestCasesExtendAbstractMetaDataTestCaseCheck extends BaseLoggingVisitor {

    public static final String KEY = "metadata-test-cases-extend-abstract-metadata-test-case";

    @Override
    public final void visitClass(ClassTree classTree) {
        super.visitClass(classTree);
        final Symbol owner = classTree.symbol().owner();
        boolean isFunctional = owner.isPackageSymbol() && owner.name().endsWith("functional");
        boolean isTestClass = ClassParserUtils.isTestClass(classTree);

        if (isTestClass && isFunctional && classTree.simpleName() != null && (classTree.simpleName().name().endsWith("MetaDataTestCases") || classTree.simpleName()
                .name()
                .endsWith("MetaDataTestCases")) && !classTree.symbol().type().isSubtypeOf("org.mule.tools.devkit.ctf.junit.AbstractMetaDataTestCase")) {
            logAndRaiseIssue(classTree, String.format("MetaData Test Case '%s' should inherit from AbstractMetaDataTestCase.", classTree.simpleName().name()));
        }
    }

}
