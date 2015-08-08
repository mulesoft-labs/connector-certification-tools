package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

abstract public class ConnectorClassVerifier extends SourceTreeVerifier {

    @Override public Object visitMethod(@NonNull final MethodTree methodTree, Trees trees) {
        final List<? extends AnnotationTree> annotations = methodTree.getModifiers().getAnnotations();

        // Map argument ...
        final Optional<Optional<Type>> typeOp = annotations.stream().map(a -> {
            final String processorName = a.getAnnotationType().toString();
            return Type.to(processorName);
        }).filter(Optional::isPresent).findFirst();

        // Process type ...
        if (typeOp.isPresent() && typeOp.get().isPresent()) {
            final Type type = typeOp.get().get();
            switch (type) {
                case PROCESSOR: {
                    this.verifyProcessor(methodTree, methodTree.getParameters());
                    break;
                }
                case SOURCE: {
                    this.verifySource(methodTree, methodTree.getParameters());
                    break;
                }
            }
        }

        return super.visitMethod(methodTree, trees);
    }

    protected void verifySource(@NonNull MethodTree method, @NonNull final List<? extends VariableTree> parameters) {

    }

    protected void verifyProcessor(@NonNull final MethodTree method, @NonNull final List<? extends VariableTree> parameters) {
    }

    private enum Type {
        PROCESSOR("Processor"),
        SOURCE("Source");

        private final String className;

        Type(@NonNull String className) {
            this.className = className;
        }

        public static Optional<Type> to(@NonNull final String name) {
            return Arrays.stream(Type.values()).filter(type -> type.className.equals(name)).findFirst();
        }
    }

}
