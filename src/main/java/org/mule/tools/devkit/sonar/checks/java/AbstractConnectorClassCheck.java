package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.*;

import java.util.Set;

public abstract class AbstractConnectorClassCheck extends BaseTreeVisitor implements JavaFileScanner {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnectorClassCheck.class);

    protected final Set<ImportTree> imports = Sets.newLinkedHashSet();

    public static final Predicate<AnnotationTree> ANNOTATION_TREE_PREDICATE = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && input.annotationType().is(Tree.Kind.IDENTIFIER);
        }
    };

    JavaFileScannerContext context;

    protected abstract RuleKey getRuleKey();

    @Override
    public void visitImport(ImportTree node) {
        imports.add(node);
        super.visitImport(node);
    }

    @Override
    public final void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

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
