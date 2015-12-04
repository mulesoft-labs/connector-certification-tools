package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.codehaus.plexus.util.StringUtils;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Rule;
import org.sonar.java.ast.parser.InitializerListTreeImpl;
import org.sonar.java.model.expression.NewArrayTreeImpl;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Rule(key = FunctionalTestCoverageCheck.KEY, name = "Functional test coverage", description = "Checks that: 1. There is ONE unit test per @Processor. 2. TestCases class names for processors end with 'TestCases'. 3. All test cases in each package (functional, system and unit) are included in their corresponding *TestSuite classes.", tags = { "connector-certification" })
@ActivatedByDefault
public class FunctionalTestCoverageCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "functional-test-coverage";

    public static final String TEST_DIR = "src/test/java";
    public static final String TEST_PARENT_DIR_PATTERN = "^((src\\/test\\/java\\/org\\/mule\\/modules)+(\\/\\w{1,}\\/)+(automation\\/functional)+$)";

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, final @NonNull IdentifierTree processorAnnotation) {

        String processorName = StringUtils.capitalizeFirstLetter(tree.simpleName().name());
        File dir = new File(TEST_DIR);
        List<File> testFiles = (List<File>) FileUtils.listFiles(dir, new WildcardFileFilter(processorName + "*TestCases.java"), TrueFileFilter.INSTANCE);

        if (testFiles.size() != 1) {
            logAndRaiseIssue(tree, String.format("'%s' - There should be one functional test per @Processor.", processorName));
        } else {
            Pattern p = Pattern.compile(TEST_PARENT_DIR_PATTERN);
            Matcher m = p.matcher(testFiles.get(0).getParent());
            if (!m.matches()) {
                logAndRaiseIssue(tree, String.format("Test case '%s' must be placed under directory 'src/test/java/.../automation/functional'.", processorName));
            }
        }

    }

    @Override
    public void visitClass(ClassTree tree) {
        if (tree.simpleName().name().endsWith("TestSuite")) {
            final AnnotationTree runWithAnnotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate(SuiteClasses.class), null);
            if (runWithAnnotation == null) {
                logAndRaiseIssue(tree, String.format("Missing @SuiteClasses annotation on Test Suite class '%s'.", tree.simpleName().name()));
            } else {
                final List<ExpressionTree> arguments = runWithAnnotation.arguments();
                final NewArrayTreeImpl arrayTree = (NewArrayTreeImpl) Iterables.getOnlyElement(arguments);

                InitializerListTreeImpl suiteClasses = (InitializerListTreeImpl) arrayTree.initializers();

                if (suiteClasses.isEmpty()) {
                    logAndRaiseIssue(tree, "No tests have been declared under @SuiteClasses");
                } else {
                    for (ExpressionTree test : suiteClasses) {
                        String testName = ((MemberSelectExpressionTree) test).expression().symbolType().name();
                        if (!testName.endsWith("TestCases")) {
                            logAndRaiseIssue(tree, String.format("Functional tests must end with 'TestCases' suffix. Rename test '%s' accordingly.", testName));
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

}
