package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.TransformerResolver;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

public class TransformerResolverVerifier extends SourceTreeVerifier {

    public TransformerResolverVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    public Object visitMethod(MethodTree methodTree, Trees trees) {

        boolean contains = ClassParserUtils.contains(methodTree.getModifiers().getAnnotations(), TransformerResolver.class);
        if (contains) {
            this.addError(null, "Could not automatically verify transformations times. Please, confirm that only connector domain model object are transformed");
        }

        return super.visitMethod(methodTree, trees);
    }

}
