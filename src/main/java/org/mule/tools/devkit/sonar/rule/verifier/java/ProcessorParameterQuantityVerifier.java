package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;

public class ProcessorParameterQuantityVerifier extends ConnectorClassVerifier {

    public ProcessorParameterQuantityVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> parameters) {

        // Total number of params
        if(parameters.size() >= 7){
            addError(null, "Processor '%s' contains %s parameters. You MAY HAVE to use a separate POJO class to wrap them all", method.getName().toString(), parameters.size());
        }

    }
}
