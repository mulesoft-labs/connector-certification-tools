package org.mule.tools.devkit.sonar;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.exception.DevKitSonarRuntimeException;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ConnectorModelIml implements Context.ConnectorModel {

    private final Path connectorPath;
    private final Set<String> processors;
    private String packageName;

    public ConnectorModelIml(@NonNull Path connectorPath) {
        this.connectorPath = connectorPath;
        this.processors = new HashSet<>();

        parseClass(connectorPath);
    }

    private void parseClass(@NonNull Path connectorPath) {

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        final Iterable<? extends JavaFileObject> compilationUnit = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(connectorPath.toFile()));

        // Create the compilation task
        final JavacTask task = (JavacTask) compiler.getTask(null, fileManager, null, null, null, compilationUnit);

        try {
            final Iterable<? extends CompilationUnitTree> asts = task.parse();
            final Trees trees = Trees.instance(task);

            final ModelScanner modelScanner = new ModelScanner();
            modelScanner.scan(asts.iterator().next(), trees);

        } catch (IOException e) {
            throw new DevKitSonarRuntimeException("Connector class can not be parsed. Class file name " + connectorPath, e);
        }
    }

    @Override public Set<String> getProcessors() {
        return processors;
    }

    @Override public Set<String> getSources() {
        throw new UnsupportedOperationException();
    }

    @Override public List<String> getProperty(@NonNull final ClassProperty property) {
        return new ArrayList<>(property.values(this));
    }

    @Override @NonNull public String getPackage() {
        return packageName;
    }

    private class ModelScanner extends TreePathScanner<Object, Trees> {

        boolean mainClassParsed;

        @Override public Object visitCompilationUnit(CompilationUnitTree node, Trees trees) {
            ConnectorModelIml.this.packageName = node.getPackageName().toString();
            return super.visitCompilationUnit(node, trees);
        }

        @Override public Object visitMethod(MethodTree method, Trees trees) {

            final List<? extends AnnotationTree> annotations = method.getModifiers().getAnnotations();
            if (annotations.stream().anyMatch(ClassParserUtils::isProcessorAnnotation)) {
                ConnectorModelIml.this.addProcessor(method.getName().toString());
            }

            return super.visitMethod(method, trees);
        }

        @Override public Object visitClass(ClassTree node, Trees trees) {
            Object result;
            if (!mainClassParsed) {
                final List<? extends AnnotationTree> annotations = node.getModifiers().getAnnotations();
                if (!annotations.stream().anyMatch(ClassParserUtils::isConnectorAnnotation)) {
                    throw new DevKitSonarRuntimeException("Class is not marked with @Connector. File:" + ConnectorModelIml.this.connectorPath);
                }
                mainClassParsed = true;
                result = super.visitClass(node, trees);
            } else {
                result = null;
            }
            return result;

        }
    }

    private void addProcessor(@NonNull final String methodName) {
        this.processors.add(methodName);
    }
}


