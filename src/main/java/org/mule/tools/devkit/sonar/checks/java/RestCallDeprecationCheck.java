package org.mule.tools.devkit.sonar.checks.java;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mule.api.annotations.rest.RestCall;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Rule(key = RestCallDeprecationCheck.KEY, name = "@RestCall annotation is deprecated", description = "Support for @RestCall processors has been deprecated in favor of RAML. Consider migrating your connector to REST Connect.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class RestCallDeprecationCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "restcall-annotation-deprecated";

    public static final Predicate<AnnotationTree> HAS_REST_CALL_ANNOTATION = new Predicate<AnnotationTree>() {

        @SuppressWarnings("deprecation")
        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RestCall.class);
        }
    };

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        List<? extends AnnotationTree> annotations = tree.modifiers().annotations();
        final long count = Iterables.size(Iterables.filter(annotations, HAS_REST_CALL_ANNOTATION));
        if (count > 0) {
            final String message = String.format("@RestCall should be removed from processor '%s' as it is deprecated.", tree.simpleName());
            logAndRaiseIssue(tree, message);
        }

    }

}
