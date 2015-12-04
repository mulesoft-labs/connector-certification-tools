package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.api.annotations.param.RefOnly;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = RefOnlyInComplexTypesCheck.KEY, name = "Complex-type arguments must be marked with @RefOnly", description = "Checks that all complex-type arguments of a processor are annotated with @RefOnly.", tags = { "connector-certification" })
@ActivatedByDefault
public class RefOnlyInComplexTypesCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "refonly-annotation-in-complex-types";

    public static final Predicate<AnnotationTree> HAS_REF_ONLY_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RefOnly.class);
        }
    };

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, @NonNull final IdentifierTree processorAnnotation) {

        Iterable<? extends VariableTree> complexTypes = Iterables.filter(tree.parameters(), ClassParserUtils.complexTypePredicate());
        for (VariableTree variable : complexTypes) {

            List<? extends AnnotationTree> annotations = variable.modifiers().annotations();

            final long count = Iterables.size(Iterables.filter(annotations, HAS_REF_ONLY_ANNOTATION));
            if (count == 0) {
                final String message = String.format("Processor '%s' contains variable '%s' of type '%s' (complex type) not annotated with @RefOnly.", tree.simpleName(),
                        variable.simpleName(), ClassParserUtils.getStringForType(variable.type()));
                logAndRaiseIssue(variable, message);
            }
        }
    }

}
