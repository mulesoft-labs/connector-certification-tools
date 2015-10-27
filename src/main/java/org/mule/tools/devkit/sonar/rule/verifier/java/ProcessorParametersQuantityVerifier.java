package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;

public class ProcessorParametersQuantityVerifier extends ConnectorClassVerifier {

    public ProcessorParametersQuantityVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> parameters) {

        if(parameters.size() >= 5){
            addError(null, "Processor '%s' contains %s parameters. Consider wrapping them in a separate POJO class", method.getName().toString(), parameters.size());
        }

    }
}
