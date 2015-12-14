package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.api.annotations.param.Payload;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = PayloadDeprecationCheck.KEY, name = "@Payload annotation is deprecated", description = "Support for @Payload has been deprecated. Use @Default(\"#[payload]\") instead.", tags = { "connector-certification" })
public class PayloadDeprecationCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "payload-annotation-deprecated";
    public static final Predicate<VariableTree> HAS_PAYLOAD_ANNOTATION = new Predicate<VariableTree>() {

        @Override
        public boolean apply(@Nullable VariableTree input) {
            return input != null && Iterables.any(input.modifiers().annotations(), ClassParserUtils.hasAnnotationPredicate(Payload.class));
        }
    };

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, final @NonNull IdentifierTree processorAnnotation) {
        long count = Iterables.size(Iterables.filter(tree.parameters(), HAS_PAYLOAD_ANNOTATION));
        if (count > 0) {
            final String message = String.format("@Payload must be removed from processor '%s' as it has been deprecated.", tree.simpleName());
            logAndRaiseIssue(tree, message);
        }

    }

}