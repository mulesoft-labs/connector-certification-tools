package org.mule.tools.devkit.sonar.checks.java;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import com.google.common.collect.Iterables;

@Rule(key = DevKitAnnotationsOrderCheck.KEY, name = "DevKit annotations should be added in a certain order for easing readability", description = "If there is a @Default annotation, it should be the last one (closest to the argument declaration). If there is @RefOnly, it should be prior to @Default.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class DevKitAnnotationsOrderCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "devkit-annotations-order";

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            final List<AnnotationTree> annotations = var.modifiers().annotations();
            int indexOfDefault = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.Default"));
            int indexOfRefOnly = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate("org.mule.api.annotations.param.RefOnly"));

            if (indexOfDefault != -1 && indexOfDefault != annotations.size() - 1) {
                // if there is a @Default annotation it has to be the last one, otherwise raise an issue
                logAndRaiseIssue(annotations.get(indexOfDefault), String.format("@Default annotation must be the last one in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            } else if (indexOfRefOnly != -1 && indexOfDefault == -1 && indexOfRefOnly != annotations.size() - 1) {
                // if there is a @RefOnly annotation it has to be the last one or be right before a @Default one, otherwise raise an issue
                logAndRaiseIssue(annotations.get(indexOfRefOnly), String.format("@RefOnly annotation must be the last one in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            } else if (indexOfRefOnly != -1 && indexOfDefault != -1 && indexOfRefOnly != annotations.size() - 2)
                logAndRaiseIssue(annotations.get(indexOfRefOnly), String.format("@RefOnly annotation must be right before @Default in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
        }
    }
}
