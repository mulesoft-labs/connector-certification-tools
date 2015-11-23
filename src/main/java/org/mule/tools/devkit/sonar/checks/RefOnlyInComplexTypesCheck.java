package org.mule.tools.devkit.sonar.checks;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.param.RefOnly;
import org.mule.tools.devkit.sonar.JavaRuleRepository;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;

import javax.annotation.Nullable;
import java.util.List;

@Rule(key = RefOnlyInComplexTypesCheck.KEY,
        name = "Check complex-types arguments are marked with @RefOnly",
        description = "This rule checks that all complex-type arguments for a method are annotated with @RefOnly",
        tags = { "connector-certification" })
public class RefOnlyInComplexTypesCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "refonly-annotation-in-complex-types";
    private static final RuleKey RULE_KEY = RuleKey.of(JavaRuleRepository.REPOSITORY_KEY, KEY);

    public static final Predicate<AnnotationTree> HAS_REF_ONLY_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RefOnly.class);
        }
    };

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {

        Iterable<? extends VariableTree> complexTypes = Iterables.filter(tree.parameters(), ClassParserUtils.COMPLEX_TYPE_PREDICATE);
        for (VariableTree variable: complexTypes) {

            List<? extends AnnotationTree> annotations = variable.modifiers().annotations();

            final long count = Iterables.size(Iterables.filter(annotations, HAS_REF_ONLY_ANNOTATION));
            if (count == 0) {
                final String message = String.format("Processor '%s' contains variable '%s' of type '%s' (complex type) not annotated with @RefOnly.", tree.simpleName(), variable.simpleName(), variable.type().toString());
                logAndRaiseIssue(variable, message);
            }
        }
    }

}
