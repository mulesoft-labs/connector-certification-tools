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

@Rule(key = DevKitAnnotationsOrderCheck.KEY, name = "DevKit annotations should be added in a certain order for easing readability", description = "If there is a @Default or @Optional annotation, it should be the last one (closest to the argument declaration). If there is @RefOnly, it should be prior to @Default or @Optional.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class DevKitAnnotationsOrderCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "devkit-annotations-order";

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            final List<AnnotationTree> annotations = var.modifiers().annotations();
            int indexOfDefault = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate(ClassParserUtils.FQN_DEFAULT));
            int indexOfOptional = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate(ClassParserUtils.FQN_OPTIONAL));
            int indexOfRefOnly = Iterables.indexOf(annotations, ClassParserUtils.hasAnnotationPredicate(ClassParserUtils.FQN_REFONLY));

            if (hasAnnotation(indexOfDefault)
                    && isNotLastAnnotation(indexOfDefault, annotations)) {
                // if there is a @Default annotation it has to be the last one, otherwise raise an issue
                logAndRaiseIssue(annotations.get(indexOfDefault),
                        String.format("@Default annotation must be the last one in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            }
            else if (hasAnnotation(indexOfOptional)
                    && isNotLastAnnotation(indexOfOptional, annotations)) {
                // if there is a @Optional annotation it has to be the last one, otherwise raise an issue
                logAndRaiseIssue(annotations.get(indexOfOptional),
                        String.format("@Optional annotation must be the last one in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            }
            else if (hasAnnotation(indexOfRefOnly)
                    && !hasDefaultOrOptional(indexOfDefault, indexOfOptional)
                    && isNotLastAnnotation(indexOfRefOnly, annotations)) {
                // if there is a @RefOnly annotation it has to be the last one or be right before a @Default one, otherwise raise an issue
                logAndRaiseIssue(annotations.get(indexOfRefOnly),
                        String.format("@RefOnly annotation must be the last one in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            }
            else if (hasAnnotation(indexOfRefOnly)
                    && hasDefaultOrOptional(indexOfDefault, indexOfOptional)
                    && isNotNextToLastAnnotation(indexOfRefOnly, annotations)) {
                logAndRaiseIssue(annotations.get(indexOfRefOnly),
                        String.format("@RefOnly annotation must be placed just before @Default in method '%s' argument '%s'.", tree.simpleName(), var.simpleName()));
            }
        }
    }

    private boolean hasDefaultOrOptional(int indexOfDefault, int indexOfOptional) {
        return hasAnnotation(indexOfDefault) || hasAnnotation(indexOfOptional);
    }

    private boolean hasAnnotation(int index) {
        return index != -1;
    }

    private boolean isNotLastAnnotation(int index, List<AnnotationTree> annotations){
        return index != annotations.size() - 1;
    }

    private boolean isNotNextToLastAnnotation(int index, List<AnnotationTree> annotations){
        return index != annotations.size() - 2;
    }

}
