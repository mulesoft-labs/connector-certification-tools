package org.mule.tools.devkit.sonar.checks;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import javax.annotation.Nullable;
import java.util.List;

abstract class AbstractConnectorClassCheck extends BaseTreeVisitor implements JavaFileScanner {

    private static final String CONNECTOR_ANNOTATION = "Connector";
    private static final String PROCESSOR_ANNOTATION = "Processor";
    private static final String SOURCE_ANNOTATION = "Source";

    public static final Predicate<AnnotationTree> ANNOTATION_TREE_PREDICATE = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && input.annotationType().is(Tree.Kind.IDENTIFIER);
        }
    };

    JavaFileScannerContext context;

    @Override
    public void scanFile(@NotNull JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitClass(ClassTree tree) {
        for (AnnotationTree annotationTree : Iterables.filter(tree.modifiers().annotations(), ANNOTATION_TREE_PREDICATE)) {
            final IdentifierTree idf = (IdentifierTree) annotationTree.annotationType();
            if (idf.name().equals(CONNECTOR_ANNOTATION)) {
                verifyConnector(tree, idf);
            }
        }

        // The call to the super implementation allows to continue the visit of the AST.
        // Be careful to always call this method to visit every node of the tree.
        super.visitClass(tree);
    }

    @Override
    public final void visitMethod(MethodTree tree) {
        for (AnnotationTree annotationTree : Iterables.filter(tree.modifiers().annotations(), ANNOTATION_TREE_PREDICATE)) {
            final IdentifierTree idf = (IdentifierTree) annotationTree.annotationType();
            if (idf.name().equals(PROCESSOR_ANNOTATION)) {
                verifyProcessor(tree, idf);
            } else if (idf.name().equals(SOURCE_ANNOTATION)) {
                verifySource(tree, idf);
            }
        }

        // The call to the super implementation allows to continue the visit of the AST.
        // Be careful to always call this method to visit every node of the tree.
        super.visitMethod(tree);
    }

    protected void verifySource(@NotNull MethodTree methodTree, @NotNull final IdentifierTree sourceAnnotation) {
    }

    protected void verifyProcessor(@NotNull final MethodTree methodTree, @NotNull final IdentifierTree processorAnnotation) {
    }

    protected void verifyConnector(@NotNull ClassTree classTree, @NotNull final IdentifierTree connectorAnnotation) {
    }

}
