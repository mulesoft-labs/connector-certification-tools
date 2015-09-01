package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.rest.RestCall;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;

public class RestCallIsDeprecatedVerifier extends ConnectorClassVerifier {

    public RestCallIsDeprecatedVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    protected void verifyProcessor(@NonNull final MethodTree method, final @NonNull List<? extends VariableTree> parameters) {
        boolean contains = ClassParserUtils.contains(method.getModifiers().getAnnotations(), RestCall.class);
        if (contains) {
            this.addError(method.getName().toString(), "@RestCall should be removed from method");
        }
    }

}
