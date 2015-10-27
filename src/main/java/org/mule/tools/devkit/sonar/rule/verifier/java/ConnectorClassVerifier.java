package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;

abstract class ConnectorClassVerifier extends SourceTreeVerifier {

    public ConnectorClassVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override
    @NonNull
    final public Object visitClass(@NonNull ClassTree classTree, @NonNull Trees trees) {
        Object result = null;

        // Process if it's a connector class ...
        if (ClassParserUtils.contains(classTree.getModifiers().getAnnotations(), Connector.class)) {
            this.verifyConnector(classTree, trees);
            result = super.visitClass(classTree, trees);
        }
        return result;
    }

    @Override
    @NonNull
    final public Object visitMethod(@NonNull final MethodTree methodTree, Trees trees) {
        final List<? extends AnnotationTree> annotations = methodTree.getModifiers().getAnnotations();

        if (ClassParserUtils.contains(annotations, Processor.class)) {
            this.verifyProcessor(methodTree, methodTree.getParameters());
        } else if (ClassParserUtils.contains(annotations, Source.class)) {
            this.verifySource(methodTree, methodTree.getParameters());
        }
        return super.visitMethod(methodTree, trees);
    }

    protected void verifySource(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> parameters) {
    }

    protected void verifyProcessor(@NonNull final MethodTree method, @NonNull final List<? extends VariableTree> parameters) {
    }

    protected void verifyConnector(@NonNull ClassTree classTree, @NonNull Trees trees) {
    }

}
