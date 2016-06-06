package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.api.internal.google.common.collect.Lists;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = AvoidNullChecksOnProcessorCheck.KEY, name = "No @Nullable/@NotNull annotations should be used at processor level", description = "If there is a parameter annotated with @NotNull (or @NonNull), remove the annotation. If there is a parameter annotated with @Nullable, replace it with @Optional annotation or remove it if @Optional is already present.", priority = Priority.CRITICAL, tags = {
        "connector-certification"
})
@ActivatedByDefault
public class AvoidNullChecksOnProcessorCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "nullable-and-notnull-annotations";
    private static final List<String> ANNOTATION_CLASSES = Lists.newArrayList("org.jetbrains.annotations.Nullable",
            "org.jetbrains.annotations.NotNull",
            "com.sun.javafx.beans.annotations.NonNull",
            "javax.annotation.Nonnull");

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            List<AnnotationTree> annotations = var.modifiers().annotations();
            for (String annotationClass : ANNOTATION_CLASSES) {
                for(AnnotationTree annotation : Iterables.filter(annotations, ClassParserUtils.hasAnnotationPredicate(annotationClass))){
                    logAndRaiseIssue(annotation, String.format("Remove @%s annotation in method '%s' argument '%s'.", annotationClass, tree.simpleName(), var.simpleName()));
                }
            }
        }
    }

}
