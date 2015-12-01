package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = NumberOfSimpleAndOptionalArgumentsCheck.KEY, name = "Too many @Optional arguments in a processor", description = "Checks, for every @Processor, that the number of optional arguments (each of which must be of simple-type) doesn't exceed the maximum allowed. If it does, the suggested approach is to wrap them all in a separate POJO class. ", tags = { "connector-certification" })
@ActivatedByDefault
public class NumberOfSimpleAndOptionalArgumentsCheck extends AbstractConnectorClassCheck {

    private static final Logger logger = LoggerFactory.getLogger(NumberOfSimpleAndOptionalArgumentsCheck.class);
    public static final String KEY = "number-of-simple-and-optional-arguments";
    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    private static final int DEFAULT_MAX_ALLOWED = 6;

    /**
     * The maximum number of simple-type arguments allowed in a processor.
     */
    @RuleProperty(key = "maxArgumentsAllowed", defaultValue = "" + DEFAULT_MAX_ALLOWED, description = "The maximum number of complex-type arguments allowed in a method annotated with @Processor")
    protected int maxArgumentsAllowed = DEFAULT_MAX_ALLOWED;

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, final @NonNull IdentifierTree processorAnnotation) {
        final long count = Iterables.size(Iterables.filter(tree.parameters(), ClassParserUtils.getSimpleTypePredicate(imports)));
        if (count > maxArgumentsAllowed) {
            final String message = String
                    .format("Processor '%s' has %d simple-type parameters marked as @Optional (more than %d, which is the maximum allowed). It's strongly recommended that all optional parameters are grouped inside a separate POJO class.",
                            tree.simpleName(), count, maxArgumentsAllowed);
            logAndRaiseIssue(processorAnnotation, message);
        }
    }
}