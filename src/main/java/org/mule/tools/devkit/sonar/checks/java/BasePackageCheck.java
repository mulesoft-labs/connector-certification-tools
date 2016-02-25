package org.mule.tools.devkit.sonar.checks.java;

import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.PackageDeclarationTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.google.common.collect.Iterables;

@Rule(key = BasePackageCheck.KEY, name = "The connector base package should be org.mule.modules.XXXX, where XXXX is your connector name)", description = "Connector code should be placed under org.mule.modules.XXXX package", priority = Priority.MAJOR, tags = { "connector-certification"
})
public class BasePackageCheck extends BaseLoggingVisitor {

    public static final String KEY = "base-connector-package";

    @Override
    public void visitClass(ClassTree classTree) {
        for (AnnotationTree annotationTree : Iterables.filter(classTree.modifiers().annotations(), ClassParserUtils.ANNOTATION_TREE_PREDICATE)) {
            if (ClassParserUtils.is(annotationTree, "org.mule.api.annotations.Connector")) {
                final Tree parent = classTree.parent();
                if (parent != null && parent.is(Tree.Kind.COMPILATION_UNIT)) {
                    CompilationUnitTree compilationUnitTree = (CompilationUnitTree) parent;
                    final PackageDeclarationTree packageDeclarationTree = compilationUnitTree.packageDeclaration();
                    if (packageDeclarationTree != null) {
                        final String packageName = ClassParserUtils.extractFullyQualifiedPackageName(packageDeclarationTree);
                        if (packageName.startsWith("org.mule.module.")) {
                            logAndRaiseIssue(packageDeclarationTree,
                                    String.format("Connector base package found '%s'. Consider moving it to '%s'.", packageName, packageName.replaceFirst("module", "modules")));
                        } else if (!packageName.startsWith("org.mule.modules.")) {
                            logAndRaiseIssue(packageDeclarationTree,
                                    String.format("Connector base package found '%s'. Connector's base package should be 'org.mule.modules.connectorname'.", packageName));
                        }
                    }
                }
            }
        }
        super.visitClass(classTree);
    }
}
