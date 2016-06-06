package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.*;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = NoNullAnnotationsInProcessorCheck.KEY, name = "No @Nullable/@NotNull annotations should be used at processor level", description = "If there is a parameter annotated with @NotNull (or @NonNull), remove the annotation. If there is a parameter annotated with @Nullable, replace it with @Optional annotation or remove it if @Optional is already present.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class NoNullAnnotationsInProcessorCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "nullable-and-notnull-annotations";

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            final List<AnnotationTree> annotations = var.modifiers().annotations();

            int indexOfNullable = Iterables.indexOf(annotations, ClassParserUtils.hasSimpleAnnotationPredicate("Nullable"));
            int indexOfNotNull = Iterables.indexOf(annotations, ClassParserUtils.hasSimpleAnnotationPredicate("NotNull"));
            int indexOfNonNull = Iterables.indexOf(annotations, ClassParserUtils.hasSimpleAnnotationPredicate("NonNull"));
            int indexOfOptional = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.Optional"));

            if (indexOfNullable != -1) {
                if(indexOfOptional != -1){
                    // if there is a @Nullable and a @Optional annotation, raise and issue
                    logAndRaiseIssue(annotations.get(indexOfNullable), String.format("Remove @Nullable annotation in method '%s' argument '%s'. Use the @Optional annotation only.", tree.simpleName(), var.simpleName()));
                }
                else{
                    // if there is a @Nullable only, raise and issue
                    logAndRaiseIssue(annotations.get(indexOfNullable), String.format("Replace @Nullable annotation with @Optional in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
                }
            }

            if (indexOfNotNull != -1) {
                // if there is a @NotNull annotation, raise and issue
                logAndRaiseIssue(annotations.get(indexOfNotNull), String.format("Remove @NotNull annotation in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            }

            if (indexOfNonNull != -1) {
                // if there is a @NonNull / @Nonnull annotation, raise and issue
                logAndRaiseIssue(annotations.get(indexOfNonNull), String.format("Remove @%s annotation in method '%s' argument '%s'.", annotations.get(indexOfNonNull).annotationType().toString(), tree.simpleName(), var.simpleName()));
            }
        }
    }
}
