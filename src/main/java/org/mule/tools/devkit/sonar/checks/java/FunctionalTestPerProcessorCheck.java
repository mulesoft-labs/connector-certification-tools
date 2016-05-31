package org.mule.tools.devkit.sonar.checks.java;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = FunctionalTestPerProcessorCheck.KEY, name = "One test per processor", description = "Checks that there is ONE test per @Processor annotation and that its name ends with the suffix 'TestCases' or 'IT'.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
@ActivatedByDefault
public class FunctionalTestPerProcessorCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "functional-test-per-processor";
    public static final String TEST_DIR = "src/test/java";
    public static final Pattern TEST_PARENT_DIR_PATTERN = Pattern.compile("^((src/test/java/org/mule/module[s]?)+(/\\w+/)+(automation/functional)+$)");

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {

        File dir = new File(TEST_DIR);
        final String processorName = StringUtils.capitalizeFirstLetter(tree.simpleName().name());
        List<File> testFiles = (List<File>) FileUtils.listFiles(dir, new OrFileFilter(new WildcardFileFilter(processorName + "TestCases.java"), new WildcardFileFilter(
                processorName + "IT.java")), TrueFileFilter.INSTANCE);

        if (testFiles.size() != 1) {
            logAndRaiseIssue(tree.simpleName(), String.format("There should be one functional test per @Processor. Add proper test for processor '%s'.", tree.simpleName().name()));
        } else {
            Matcher m = TEST_PARENT_DIR_PATTERN.matcher(testFiles.get(0).getParent());
            if (!m.matches()) {
                logAndRaiseIssue(tree.simpleName(),
                        String.format("'%s' must be placed under directory 'src/test/java/org/mule/modules/.../automation/functional'.", testFiles.get(0).getName()));
            }
        }

    }

}
