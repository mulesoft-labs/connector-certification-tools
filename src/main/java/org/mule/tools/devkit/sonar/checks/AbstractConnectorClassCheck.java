package org.mule.tools.devkit.sonar.checks;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

abstract class AbstractConnectorClassCheck extends BaseTreeVisitor implements JavaFileScanner {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnectorClassCheck.class);

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

    protected abstract RuleKey getRuleKey();

    @Override
    public final void scanFile(@NonNull JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public final void visitClass(ClassTree tree) {
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

    protected void verifySource(@NonNull MethodTree methodTree, @NonNull final IdentifierTree sourceAnnotation) {
    }

    protected void verifyProcessor(@NonNull final MethodTree methodTree, @NonNull final IdentifierTree processorAnnotation) {
    }

    protected void verifyConnector(@NonNull ClassTree classTree, @NonNull final IdentifierTree connectorAnnotation) {
    }

    protected void logAndRaiseIssue(@NonNull Tree classTree, String message) {
        logger.info(message);
        context.addIssue(classTree, getRuleKey(), message);
    }

}
