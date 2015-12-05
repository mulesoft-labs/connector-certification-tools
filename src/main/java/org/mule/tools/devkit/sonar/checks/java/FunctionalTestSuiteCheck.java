package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.java.ast.parser.InitializerListTreeImpl;
import org.sonar.java.model.expression.NewArrayTreeImpl;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = FunctionalTestSuiteCheck.KEY, name = "Functional test coverage", description = "Checks that: 1. There is ONE unit test per @Processor. 2. TestCases class names for processors end with 'TestCases'. 3. All test cases in each package (functional, system and unit) are included in their corresponding *TestSuite classes.", tags = { "connector-certification" })
@ActivatedByDefault
public class FunctionalTestSuiteCheck extends BaseTreeVisitor implements JavaFileScanner {

    private static final Logger logger = LoggerFactory.getLogger(FunctionalTestSuiteCheck.class);
    public static final String KEY = "functional-test-suite-coverage";

    JavaFileScannerContext context;

    @Override
    public final void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public final void visitClass(ClassTree tree) {
        if (tree.simpleName().name().endsWith("TestSuite")) {
            final AnnotationTree runWithAnnotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate(SuiteClasses.class), null);
            if (runWithAnnotation == null) {
                logAndRaiseIssue(tree, String.format("Missing @SuiteClasses annotation on Test Suite class '%s'.", tree.simpleName().name()));
            } else {
                final List<ExpressionTree> arguments = runWithAnnotation.arguments();
                final NewArrayTreeImpl arrayTree = (NewArrayTreeImpl) Iterables.getOnlyElement(arguments);

                InitializerListTreeImpl suiteClasses = (InitializerListTreeImpl) arrayTree.initializers();

                if (suiteClasses.isEmpty()) {
                    logAndRaiseIssue(tree, "No tests have been declared under @SuiteClasses.");
                } else {
                    for (ExpressionTree test : suiteClasses) {
                        String testName = ((MemberSelectExpressionTree) test).expression().symbolType().name();
                        if (!testName.endsWith("TestCases")) {
                            logAndRaiseIssue(tree, String.format("Functional tests must end with 'TestCases'. Rename test '%s' accordingly.", testName));
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

    protected void logAndRaiseIssue(@NonNull Tree classTree, String message) {
        logger.info(message);
        context.addIssue(classTree, this, message);
    }

}
