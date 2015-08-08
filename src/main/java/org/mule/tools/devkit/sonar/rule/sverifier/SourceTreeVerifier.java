package org.mule.tools.devkit.sonar.rule.sverifier;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Formatter;
import java.util.HashSet;
import java.util.Set;

public abstract class SourceTreeVerifier extends TreePathScanner<Object, Trees> {

    final private Set<String> errors = new HashSet<>();
    final private Set<ImportTree> imports = new HashSet<>();

    SourceTreeVerifier() {

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

    @NonNull public Set<String> getErrors() {
        return errors;
    }

    void addError(@NonNull final String msg, @NonNull Object... argv) {
        final Formatter formatter = new Formatter();
        errors.add(formatter.format(msg, argv).toString());
    }

    public void clearErrors() {
        errors.clear();
        imports.clear();
    }

    @NonNull
    protected Set<ImportTree> getImports() {
        return imports;
    }
}