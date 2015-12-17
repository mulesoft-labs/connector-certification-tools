package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = NumberOfComplexArgumentsCheck.KEY, name = "Too many complex-type arguments in a processor", description = "Checks, for every @Processor, that the number of complex-type arguments is less than max allowed.", priority = Priority.MAJOR, tags = { "connector-certification" })
@ActivatedByDefault
public class NumberOfComplexArgumentsCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "number-of-complex-arguments";
    private static final int DEFAULT_MAX_ALLOWED = 6;

    /**
     * The maximum number of complex-type arguments allowed in a processor.
     */
    @RuleProperty(key = "maxArgumentsAllowed", defaultValue = "" + DEFAULT_MAX_ALLOWED, description = "The maximum number of complex-type arguments allowed in a method annotated with @Processor")
    protected int maxArgumentsAllowed = DEFAULT_MAX_ALLOWED;

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, @NonNull final IdentifierTree processorAnnotation) {
        final long count = Iterables.size(Iterables.filter(tree.parameters(), ClassParserUtils.complexTypePredicate()));
        if (count > maxArgumentsAllowed) {
            final String message = String.format("Processor '%s' has %d complex-type parameters (more than %d, which is the maximum allowed).", tree.simpleName(), count,
                    maxArgumentsAllowed);
            logAndRaiseIssue(processorAnnotation, message);
        }
    }
}
