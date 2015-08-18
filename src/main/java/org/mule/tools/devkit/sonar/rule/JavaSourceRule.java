package org.mule.tools.devkit.sonar.rule;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Context;
import org.mule.tools.devkit.sonar.ContextImpl;
import org.mule.tools.devkit.sonar.ValidationError;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;
import org.mule.tools.devkit.sonar.rule.sverifier.SourceTreeVerifier;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JavaSourceRule extends AbstractRule {

    private static final String EXPRESSION_SEPARATOR = ";";
    private final SourceTreeVerifier sourceVisitor;
    private final Optional<String> acceptAnnotation;

    public JavaSourceRule(@NonNull final Documentation documentation, @NonNull String accept, @NonNull final String assertExp) {
        super(documentation, extractRegPattern(accept));
        this.acceptAnnotation = extractAnnotation(accept);

        try {
            final Class<? extends SourceTreeVerifier> clazz = (Class<? extends SourceTreeVerifier>) Class.forName(assertExp, true, Thread.currentThread().getContextClassLoader());
            this.sourceVisitor = clazz.newInstance();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DevKitSonarRuntimeException("Visitor could not be loaded:" + assertExp, e);
        }
    }

    @Override public boolean accepts(@NonNull Path basePath, @NonNull Path childPath) {

        // Does the class annotation ....
        boolean result = super.accepts(basePath, childPath);
        if (result && acceptAnnotation.isPresent()) {
            try {
                final JavacTask task = this.getJavacTask(basePath, childPath);

                Trees trees;
                final Iterable<? extends CompilationUnitTree> asts = task.parse();
                trees = Trees.instance(task);
                // Fire processing ...
                for (CompilationUnitTree ast : asts) {

                    // Set up in thread local ...
                    final ContextImpl instance = (ContextImpl) Context.getInstance(basePath);
                    instance.setup();

                    // Is valid ?
                    final ClassAnnotatedVerifier verifier = new ClassAnnotatedVerifier(acceptAnnotation.get());
                    verifier.scan(ast, trees);
                    result = verifier.getResult();
                }

            } catch (IOException | IllegalArgumentException e) {
                throw new DevKitSonarRuntimeException("Expression can not supported '" + acceptAnnotation.get() + "' for file '" + childPath + "'", e);
            }
        }
        return result;

    }

    @Override public @NonNull Set<ValidationError> verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException {

        final JavacTask task = getJavacTask(basePath, childPath);

        Trees trees;
        final Iterable<? extends CompilationUnitTree> asts;
        try {
            asts = task.parse();
            trees = Trees.instance(task);
        } catch (IOException e) {
            throw new DevKitSonarRuntimeException(e);
        } finally {
            sourceVisitor.clearErrors();
        }

        // Fire processing ...
        final Set<String> result = new HashSet<>();
        try {
            for (CompilationUnitTree ast : asts) {
                // Set up in thread local ...
                final ContextImpl instance = (ContextImpl) Context.getInstance(basePath);
                instance.setup();

                sourceVisitor.scan(ast, trees);
                result.addAll(sourceVisitor.getErrors());
            }

        } finally {
            sourceVisitor.clearErrors();
        }

        return result.stream().map(msg -> ValidationError.create(this.getDocumentation(), msg)).collect(Collectors.toSet());
    }

    @NonNull private JavacTask getJavacTask(@NonNull Path basePath, @NonNull Path childPath) {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        final File file = basePath.resolve(childPath).toFile();
        final Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(file));

        // Create the compilation task
        return (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnit);
    }

    @NonNull private static String extractRegPattern(@NonNull String accept) {
        String result = accept;
        if (accept.contains(EXPRESSION_SEPARATOR)) {
            result = accept.split(EXPRESSION_SEPARATOR)[0];
        }
        return result;
    }

    private static Optional<String> extractAnnotation(@NonNull String accept) {
        Optional<String> result = Optional.empty();
        if (accept.contains(EXPRESSION_SEPARATOR)) {
            result = Optional.ofNullable(accept.split(EXPRESSION_SEPARATOR)[1]);
        }
        return result;
    }

    private static class ClassAnnotatedVerifier extends TreePathScanner<Object, Trees> {

        final private Set<ImportTree> imports = new HashSet<>();
        private final String annotationExpression;
        private boolean result;

        public ClassAnnotatedVerifier(@NonNull String annotationExpression) {
            this.annotationExpression = annotationExpression;
        }

        @Override public Object visitImport(ImportTree node, Trees trees) {

            this.imports.add(node);
            return super.visitImport(node, trees);
        }

        @Override public Object visitClass(final @NonNull ClassTree classTree, @NonNull final Trees trees) {

            final Optional<Class<?>> annotation = ClassParserUtils.classForName(annotationExpression, imports);
            if (!annotation.isPresent()) {
                throw new DevKitSonarRuntimeException("Class '" + annotationExpression + "' could not be found. Please, review the accept expression.");
            }
            this.result = ClassParserUtils.contains(classTree.getModifiers().getAnnotations(), annotation.get());
            return super.visitClass(classTree, trees);
        }

        @Override public Object visitMethod(MethodTree methodTree, Trees trees) {

            return super.visitMethod(methodTree, trees);
        }

        @NonNull public boolean getResult() {
            return this.result;
        }

    }
}
