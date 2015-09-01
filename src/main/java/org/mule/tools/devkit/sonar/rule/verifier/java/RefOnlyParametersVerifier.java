package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;
import java.util.stream.Stream;

public class RefOnlyParametersVerifier extends ConnectorClassVerifier {

    public RefOnlyParametersVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> params) {

        // Filter complex types ...
        final Stream<? extends VariableTree> nonSimpleTypes = params.stream().filter(param -> !ClassParserUtils.isSimpleType(param, getImports()));

        // Filter the types with without @RefOnly ...
        final Stream<? extends VariableTree> complexParams = nonSimpleTypes
                .filter(param -> !ClassParserUtils.isMarkedAsRefOnly(param) && !ClassParserUtils.isMarkedAsPayloadDefault(param));

        if (complexParams.findAny().isPresent()) {
            this.addError(method.getName().toString(), "Processor '%s' contains complex types without @RefOnly.", method.getName());
        }

    }
}
