package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
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
    private static final List<String> REDUNDANT_ANNOTATIONS = Lists.newArrayList("Default", "Optional");

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            if(hasRedundantAnnotations(var.modifiers().annotations())){
                logAndRaiseIssue(tree.simpleName(), String.format("@Default and @Optional annotations cannot be used at the same time in method '%s' argument '%s'. Discard @Optional.", tree.simpleName(), var.simpleName()));
            }
        }
    }

    private boolean hasRedundantAnnotations(List<AnnotationTree> annotations){
        return Iterables.size(Iterables.filter(annotations, new Predicate<AnnotationTree>() {
            public boolean apply(AnnotationTree input) {
                return input != null && REDUNDANT_ANNOTATIONS.contains(input.annotationType().toString());
            }
        })) == 2;

    }
}
