package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;

public class DefaultPayloadVerifier extends ConnectorClassVerifier {

    private static final String INPUT_STREAM_CLASS = "InputStream";

    @Override protected void verifyProcessor(@NonNull MethodTree methodTree, @NonNull final List<? extends VariableTree> parameters) {

        // At least one must be default payload ...
        final boolean hasDefaultPayload = parameters.stream().anyMatch(ClassUtils::isMarkedAsPayloadDefault);

        if (!hasDefaultPayload) {
            this.addError("Processor '%s' does not define default payload", methodTree.getName().toString());
        } else {

            // Is there any InputStream param ?. If this is the case, it should be the default param ...
            final Optional<? extends VariableTree> inputStreamParam = parameters.stream().filter(p -> p.getType().toString().endsWith(INPUT_STREAM_CLASS)).findAny();
            if (inputStreamParam.isPresent()) {
                if (!ClassUtils.isMarkedAsDefault(inputStreamParam.get())) {
                    this.addError("Processor '%s' contains a parameter of type InputStream not masked @Default(\"#[payload]\")", methodTree.getName().toString());
                }
            }
        }

    }

}
