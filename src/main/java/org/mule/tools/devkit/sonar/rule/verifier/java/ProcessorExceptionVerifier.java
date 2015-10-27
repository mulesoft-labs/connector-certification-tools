package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.tools.javac.tree.JCTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorExceptionVerifier extends ConnectorClassVerifier {

    public ProcessorExceptionVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> parameters) {

        // Get list of thrown exceptions ...
        final List<? extends ExpressionTree> exceptions = method.getThrows();

        if(exceptions.size() > 0){
            for(ExpressionTree exp : exceptions){
                String name =  ((JCTree.JCIdent)exp).getName().toString();
                if(name.contains("Connector")){
                    String newName = name.replace("Connector", "");
                    addError(null, "Exception '%s' in processor '%s' should be renamed to %s", name, method.getName().toString(), newName);
                }
            }
        }
    }
}
