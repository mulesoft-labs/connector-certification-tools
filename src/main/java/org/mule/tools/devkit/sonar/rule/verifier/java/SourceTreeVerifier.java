package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.Rule;
import org.mule.tools.devkit.sonar.ValidationError;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

abstract public class SourceTreeVerifier extends TreePathScanner<Object, Trees> {

    final private Set<ValidationError> errors = new HashSet<>();
    final private Set<ImportTree> imports = new HashSet<>();
    private final Rule.Documentation doc;

    SourceTreeVerifier(final Rule.@NonNull Documentation doc) {
        this.doc = doc;
    }

    @Override public Object visitImport(ImportTree node, Trees trees) {

        imports.add(node);
        return super.visitImport(node, trees);
    }

    @Override public Object visitClass(@NonNull final ClassTree classTree, @NonNull final Trees trees) {

        return super.visitClass(classTree, trees);
    }

    @Override public Object visitMethod(MethodTree methodTree, Trees trees) {

        return super.visitMethod(methodTree, trees);
    }

    public Set<ValidationError> getErrors() {
        return errors;
    }

    void addError(@Nullable String uuid, @NonNull final String msg, @NonNull Object... argv) {
        final Formatter formatter = new Formatter();
        final String formatedMsg = formatter.format(msg, argv).toString();
        final ValidationError validationError = ValidationError.create(doc, uuid, formatedMsg);
        errors.add(validationError);
    }

    public void clearErrors() {
        errors.clear();
        imports.clear();
    }

    @NonNull protected Set<ImportTree> getImports() {
        return imports;
    }
}