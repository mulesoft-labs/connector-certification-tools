package org.mule.tools.devkit.sonar.checks;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.rest.RestCall;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import javax.annotation.Nullable;
import java.util.List;

@Rule(key = RestCallDeprecatedCheck.KEY, name = "@RestCall annotation is deprecated", description = "Support for @RestCall processors has been deprecated in favor of RAML. Consider migrating your connector to REST Connect.", tags = { "connector-certification" })
@ActivatedByDefault
public class RestCallDeprecatedCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "restcall-annotation-deprecated";
    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);

    public static final Predicate<AnnotationTree> HAS_REST_CALL_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RestCall.class);
        }
    };

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, @NonNull final IdentifierTree processorAnnotation) {

        List<? extends AnnotationTree> annotations = tree.modifiers().annotations();

        final long count = Iterables.size(Iterables.filter(annotations, HAS_REST_CALL_ANNOTATION));
        if (count > 0) {
            final String message = String.format("@RestCall should be removed from processor '%s' as it has been deprecated.", tree.simpleName());
            logAndRaiseIssue(tree, message);
        }

    }

}
