package org.mule.tools.devkit.sonar.checks;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.param.RefOnly;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import javax.annotation.Nullable;
import java.util.List;

@Rule(key = "RefOnlyInComplexTypes",
        name = "Check complex-types arguments are marked with @RefOnly",
        description = "This rule checks that all complex-type arguments for a method are annotated with @RefOnly",
        tags = { "connector-certification" })
@ActivatedByDefault
public class RefOnlyInComplexTypesCheck extends AbstractConnectorClassCheck {

    private static final Logger logger = LoggerFactory.getLogger(NumberOfArgumentsInProcessorCheck.class);

    public static final Predicate<AnnotationTree> HAS_REFONLY_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return ClassParserUtils.is(input, RefOnly.class);
        }
    };

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {

        Iterable<? extends VariableTree> complexTypes = Iterables.filter(tree.parameters(), ClassParserUtils.COMPLEX_TYPE_PREDICATE);
        for (VariableTree variable: complexTypes) {

            List<? extends AnnotationTree> annotations = variable.modifiers().annotations();

            final long count = Iterables.size(Iterables.filter(annotations, HAS_REFONLY_ANNOTATION));
            if (count == 0) {
                final String message = String.format("Processor '%s' contains complex types without @RefOnly.", tree.simpleName(), count);
                logger.info(message);
                context.addIssue(processorAnnotation, this, message);
            }
        }
    }

}
