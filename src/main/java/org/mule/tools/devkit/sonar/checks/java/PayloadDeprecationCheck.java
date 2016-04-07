package org.mule.tools.devkit.sonar.checks.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Rule(key = PayloadDeprecationCheck.KEY, name = "@Payload annotation is deprecated", description = "Support for @Payload has been deprecated. Use @Default(\"#[payload]\") instead.", priority = Priority.CRITICAL, tags = { "connector-certification"
})
public class PayloadDeprecationCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "payload-annotation-deprecated";
    public static final Predicate<VariableTree> HAS_PAYLOAD_ANNOTATION = new Predicate<VariableTree>() {

        @Override
        public boolean apply(@Nullable VariableTree input) {
            return input != null && Iterables.any(input.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.Payload"));
        }
    };

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree param: Iterables.filter(tree.parameters(), HAS_PAYLOAD_ANNOTATION)) {
            final String message = String.format("@Payload must be removed from processor '%s' as it has been deprecated.", tree.simpleName());
            logAndRaiseIssue(param, message);
        }
    }

}
