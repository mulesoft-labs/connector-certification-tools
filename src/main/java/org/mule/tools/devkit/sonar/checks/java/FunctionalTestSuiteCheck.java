package org.mule.tools.devkit.sonar.checks.java;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.ListTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.NewArrayTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Rule(key = FunctionalTestSuiteCheck.KEY, name = "Functional test coverage", description = "Checks that: 1. There exists a @SuiteClasses annotation on Test Suite classes. 2. TestCases class names for processors end with 'TestCases'. 3. All test cases in each package (functional, system and unit) are included in their corresponding *TestSuite classes.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
@ActivatedByDefault
public class FunctionalTestSuiteCheck extends BaseLoggingVisitor {

    public static final String KEY = "functional-test-suite-coverage";
    public static final String SUFFIX = "TestCases";
    public static final String TEST_DIR = "src/test/java";
    public static final Pattern FILE_PATH_PATTERN = Pattern.compile("^((src/test/java/org/mule/module[s]?)+(/\\w+/)+(automation/functional/)+(\\w*.java)$)");

    @Override
    public final void visitClass(ClassTree tree) {
        IdentifierTree treeName = tree.simpleName();
        if (treeName != null && treeName.name().equals("FunctionalTestSuite")) {
            final AnnotationTree runWithAnnotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate("org.junit.runners.SuiteClasses"), null);
            if (runWithAnnotation == null) {
                logAndRaiseIssue(tree.simpleName(), String.format("Missing @SuiteClasses annotation on Test Suite class '%s'.", tree.simpleName().name()));
            } else {
                final List<ExpressionTree> arguments = runWithAnnotation.arguments();
                final ExpressionTree expressionTree = Iterables.getOnlyElement(arguments);

                if (expressionTree.is(Tree.Kind.NEW_ARRAY)) {
                    ListTree<ExpressionTree> suiteClasses = ((NewArrayTree) expressionTree).initializers();
                    if (suiteClasses.isEmpty()) {
                        logAndRaiseIssue(runWithAnnotation, "No tests have been declared under @SuiteClasses.");
                    } else {
                        final List<File> tests = Lists.newArrayList(FileUtils.listFiles(new File(TEST_DIR), new WildcardFileFilter("*TestCases.java"), TrueFileFilter.INSTANCE));
                        for (ExpressionTree test : suiteClasses) {
                            final String testName = ((IdentifierTree) ((MemberSelectExpressionTree) test).expression()).name();
                            Iterable<? extends File> matchingTests = Iterables.filter(tests, new Predicate<File>() {

                                @Override
                                public boolean apply(@Nullable File input) {
                                    return input != null && testName.equals(FilenameUtils.removeExtension(input.getName())) && FILE_PATH_PATTERN.matcher(input.getPath()).find();
                                }
                            });
                            if (testName.endsWith(SUFFIX)) {
                                if (Iterables.isEmpty(matchingTests)) {
                                    logAndRaiseIssue(test, String.format("A file named '%s.java' must exist in directory 'src/test/java/.../automation/functional'.", testName));
                                }
                            } else {
                                logAndRaiseIssue(test, String.format("Functional test class name must end with 'TestCases'. Rename '%s.java' accordingly.", testName));
                            }
                        }
                    }
                }
            }
        }
        super.visitClass(tree);
    }

}
