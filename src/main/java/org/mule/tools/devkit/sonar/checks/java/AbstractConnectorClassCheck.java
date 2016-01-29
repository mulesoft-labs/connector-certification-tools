package org.mule.tools.devkit.sonar.checks.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class AbstractConnectorClassCheck extends BaseLoggingVisitor {

    public static final Predicate<AnnotationTree> ANNOTATION_TREE_PREDICATE = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && input.annotationType().is(Tree.Kind.IDENTIFIER);
        }
    };

    @Override
    public final void visitClass(ClassTree tree) {
        for (AnnotationTree annotationTree : Iterables.filter(tree.modifiers().annotations(), ANNOTATION_TREE_PREDICATE)) {
            if (ClassParserUtils.is(annotationTree, Connector.class)) {
                verifyConnector(tree, (IdentifierTree) annotationTree.annotationType());
            }
        }
        super.visitClass(tree);
    }

    @Override
    public final void visitMethod(MethodTree tree) {
        for (AnnotationTree annotationTree : Iterables.filter(tree.modifiers().annotations(), ANNOTATION_TREE_PREDICATE)) {
            final IdentifierTree idf = (IdentifierTree) annotationTree.annotationType();
            if (ClassParserUtils.is(annotationTree, Processor.class)) {
                verifyProcessor(tree, idf);
            } else if (ClassParserUtils.is(annotationTree, Source.class)) {
                verifySource(tree, idf);
            }
        }
        super.visitMethod(tree);
    }

    protected void verifySource(@NotNull MethodTree methodTree, @NotNull final IdentifierTree sourceAnnotation) {
    }

    protected void verifyProcessor(@NotNull final MethodTree methodTree, @NotNull final IdentifierTree processorAnnotation) {
    }

    protected void verifyConnector(@NotNull ClassTree classTree, @NotNull final IdentifierTree connectorAnnotation) {
    }

}
