package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessParametersVerifier extends ConnectorClassVerifier {

    @Override protected void verifyProcessor(@NonNull MethodTree methodTree) {

        final List<? extends VariableTree> parameters = methodTree.getParameters();
        final Set<AnnotationTree> allAnnotations = parameters.stream().flatMap(p -> p.getModifiers().getAnnotations().stream()).collect(Collectors.toSet());

        final boolean found = allAnnotations.stream().anyMatch(a -> a.toString().equals("@Default(\"#[payload]\")"));
        if (!found) {
            this.addError("Processor '%s' does not define default payload", methodTree.getName().toString());
        }
    }
}
