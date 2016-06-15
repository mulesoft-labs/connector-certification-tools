package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = DevKitRedundantAnnotationsCheck.KEY, name = "DevKit annotations @Default and @Optional should not be used at the same time in the same processor argument.", description = "When using @Default, the @Optional annotation is redundant (@Default implies @Optional). The recommended approach is to discard the @Optional.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class DevKitRedundantAnnotationsCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "devkit-redundant-annotations";

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            List<AnnotationTree> annotations = var.modifiers().annotations();
            if(Iterables.any(annotations, ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.Default"))
                    && Iterables.any(annotations, ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.Optional"))) {
                logAndRaiseIssue(tree.simpleName(), String.format("@Default and @Optional annotations cannot be used at the same time in method '%s' argument '%s'. Discard @Optional.", tree.simpleName(), var.simpleName()));
            }
        }
    }

}
