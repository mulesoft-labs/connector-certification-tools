package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorParametersVerifier extends ConnectorClassVerifier {

    @Override protected void verifyProcessor(@NonNull MethodTree method,@NonNull final List<? extends VariableTree> parameters) {


        // Filter not optional params ...
        final Stream<? extends VariableTree> nonOptionalParam = parameters.stream().filter(p -> !ClassUtils.isMarkedAsOptional(p));

        // Filter params with default value ...
        final Set<VariableTree> mandatoryParams = nonOptionalParam.filter(p -> !ClassUtils.isMarkedAsDefault(p)).collect(Collectors.toSet());

        // Just one parameter must not be Optional or contains a default ...
        if (mandatoryParams.size() > 2) {
            addError("Processor '%s' contains more than one mandatory parameter.", method.getName().toString());
        }

    }
}
