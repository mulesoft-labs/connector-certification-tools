package org.mule.tools.devkit.sonar.checks;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.JavaRuleRepository;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;

@Rule(key = NumberOfArgumentsInProcessorCheck.KEY,
        name = "Number of complex-type arguments in a processor method",
        description = "This rule checks that the number of complex-type arguments for a method annotated with @Processor is less than max allowed",
        tags = { "connector-certification" })
public class NumberOfArgumentsInProcessorCheck extends AbstractConnectorClassCheck {

    private static final Logger logger = LoggerFactory.getLogger(NumberOfArgumentsInProcessorCheck.class);
    public static final String KEY = "NumberOfArgumentsInProcessor";
    private static final RuleKey RULE_KEY = RuleKey.of(JavaRuleRepository.REPOSITORY_KEY, KEY);

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    private static final String DEFAULT_MAX_ALLOWED = "6";

    /**
     * The maximum number of complex-type arguments allowed in a processor.
     */
    @RuleProperty(
            key = "maxArgumentsAllowed",
            defaultValue = DEFAULT_MAX_ALLOWED,
            description = "The maximum number of complex-type arguments allowed in a method annotated with @Processor")
    protected int maxArgumentsAllowed;

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        final long count = Iterables.size(Iterables.filter(tree.parameters(), ClassParserUtils.COMPLEX_TYPE_PREDICATE));
        if (count > maxArgumentsAllowed) {
            final String message = String.format("Processor %s has %d complex-type parameters (more than %d which is max allowed)", tree.simpleName(), count, maxArgumentsAllowed);
            logger.info(message);
            logAndRaiseIssue(processorAnnotation, message);

        }
    }
}
