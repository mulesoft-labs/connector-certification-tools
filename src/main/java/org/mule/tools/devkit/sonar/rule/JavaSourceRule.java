package org.mule.tools.devkit.sonar.rule;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
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
import java.util.Set;
import java.util.stream.Collectors;

public class JavaSourceRule extends AbstractRule {

    private final SourceTreeVerifier sourceVisitor;

    public JavaSourceRule(@NonNull final Documentation documentation, @NonNull String accept, @NonNull final String assertExp) {
        super(documentation, accept);
        try {
            final Class<? extends SourceTreeVerifier> clazz = (Class<? extends SourceTreeVerifier>) Class.forName(assertExp, true, Thread.currentThread().getContextClassLoader());
            this.sourceVisitor = clazz.newInstance();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new DevKitSonarRuntimeException("Visitor could not be loaded:" + assertExp, e);
        }
    }

    @Override public @NonNull Set<ValidationError> verify(@NonNull Path basePath, @NonNull Path childPath) throws DevKitSonarRuntimeException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        final File file = basePath.resolve(childPath).toFile();
        final Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(file));

        // Create the compilation task
        final JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnit);
        final Iterable<? extends CompilationUnitTree> asts;

        final Set<String> result = new HashSet<>();
        try {
            asts = task.parse();
            Trees trees = Trees.instance(task);

            for (CompilationUnitTree ast : asts) {
                // Set up in thread local ...
                final ContextImpl instance = (ContextImpl) Context.getInstance(basePath);
                instance.setup();

                sourceVisitor.scan(ast, trees);
                result.addAll(sourceVisitor.getErrors());
            }

        } catch (IOException e) {
            throw new DevKitSonarRuntimeException(e);
        } finally {
            sourceVisitor.clearErrors();
        }

        return result.stream().map(msg -> ValidationError.create(this.getDocumentation(), msg)).collect(Collectors.toSet());
    }

}
