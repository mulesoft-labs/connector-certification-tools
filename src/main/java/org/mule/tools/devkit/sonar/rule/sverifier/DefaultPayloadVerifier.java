package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class DefaultPayloadVerifier extends ConnectorClassVerifier {

    public DefaultPayloadVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override protected void verifyProcessor(@NonNull MethodTree methodTree, @NonNull final List<? extends VariableTree> parameters) {

        // At least one must be default payload ...
        final boolean hasDefaultPayload = parameters.stream().anyMatch(ClassParserUtils::isMarkedAsPayloadDefault);

        final String processorName = methodTree.getName().toString();
        if (!hasDefaultPayload) {
            this.addError("Processor '%s' must define one parameter as @Default(\"#[payload]\").", processorName);
        } else {

            // Is there any InputStream param ?. If this is the case, it should be the default param ...
            final Optional<? extends VariableTree> inputStreamParam = parameters.stream().filter(p -> {
                final Optional<Class<?>> paramClass = ClassParserUtils.classForName(p.getType(), getImports());

                // I don't have warranty that the classes has been loaded in the classpath. Assume that the suffix could help to detect if it's an input stream ..
                return (!paramClass.isPresent() && p.getType().toString().endsWith("InputStream")) || (paramClass.isPresent() && (
                        paramClass.get().isAssignableFrom(InputStream.class) || paramClass.get().isAssignableFrom(Enum.class)));

            }).findAny();

            if (inputStreamParam.isPresent()) {
                if (!ClassParserUtils.isMarkedAsDefault(inputStreamParam.get())) {
                    this.addError("Processor '%s' contains a parameter of type InputStream not masked @Default(\"#[payload]\").", processorName);
                }
            }
        }

    }

}
