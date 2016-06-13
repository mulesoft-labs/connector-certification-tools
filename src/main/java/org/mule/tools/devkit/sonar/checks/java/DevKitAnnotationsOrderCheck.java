package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.VariableTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;
import java.util.regex.Pattern;

@Rule(key = DevKitAnnotationsOrderCheck.KEY, name = "DevKit annotations should be added in a certain order for easing readability", description = "If there is a @Default or @Optional annotation, it should be the last one (closest to the argument declaration). If there is @RefOnly, it should be prior to @Default or @Optional.", priority = Priority.CRITICAL, tags = { "connector-certification" })
@ActivatedByDefault
public class DevKitAnnotationsOrderCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "devkit-annotations-order";
    private static final Pattern ORDER_PATTERN = Pattern.compile("^((@RefOnly)*(\\s*)(@Default|@Optional)*$)");

    @Override
    protected void verifyProcessor(@NotNull MethodTree tree, @NotNull final IdentifierTree processorAnnotation) {
        for (VariableTree var : tree.parameters()) {
            final List<AnnotationTree> annotations = var.modifiers().annotations();
            String unifiedAnnotations = Joiner.on(" ").join(Iterables.transform(annotations,
                    new Function<AnnotationTree, Object>() {
                        @Override
                        public String apply(AnnotationTree input) {
                            return "@" + input.annotationType().toString();
                        }
                    }));
            if (!ORDER_PATTERN.matcher(unifiedAnnotations).matches()) {
                logAndRaiseIssue(tree.simpleName(),
                        String.format("Annotation(s) '%s' in method '%s' argument '%s' do not have expected order.", unifiedAnnotations, tree.simpleName(), var.simpleName()));
            }
        }
    }
}
