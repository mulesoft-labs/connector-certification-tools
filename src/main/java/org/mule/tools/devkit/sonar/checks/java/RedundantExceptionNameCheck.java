package org.mule.tools.devkit.sonar.checks.java;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.TypeTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = RedundantExceptionNameCheck.KEY, name = "Redundant exception names thrown by a processor", description = "Don't use the word 'Connector' to define the name of custom exceptions, e.g: <MyAwesomeConnectorException> should be <MyAwesomeException>.", tags = {
        "connector-certification" })
@ActivatedByDefault
public class RedundantExceptionNameCheck extends BaseLoggingVisitor {

    public static final String KEY = "redundant-exception-name";

    @Override
    public void visitClass(ClassTree tree) {
        final TypeTree superClassTree = tree.superClass();
        if (superClassTree != null) {
            final Type superClassType = superClassTree.symbolType();
            if (superClassType.isSubtypeOf("java.lang.Exception")) {
                IdentifierTree className = tree.simpleName();
                if (className != null && className.name().contains("Connector")) {
                    logAndRaiseIssue(tree, String.format("Exception class '%s' should be renamed to '%s'.", className.name(), className.name().replace("Connector", "")));
                }
            }
        }
        super.visitClass(tree);

    }
}
