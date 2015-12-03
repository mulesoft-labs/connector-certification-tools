package org.mule.tools.devkit.sonar.checks.java;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.ListTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.TypeTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

@Rule(key = RedundantExceptionNameCheck.KEY, name = "Redundant exception names thrown by a processor", description = "Don't use the word 'Connector' to define the name of custom exceptions, e.g: <MyAwesomeConnectorException> should be <MyAwesomeException>.", tags = { "connector-certification" })
@ActivatedByDefault
public class RedundantExceptionNameCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "redundant-exception-name";

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, @NonNull final IdentifierTree processorAnnotation) {

        final ListTree<TypeTree> thrownClauses = tree.throwsClauses();

        if (!thrownClauses.isEmpty()) {
            for (TypeTree clause : thrownClauses) {
                String clauseName = ((IdentifierTree) clause).name();
                if (clauseName.contains("Connector")) {
                    final String message = String.format("Exception '%s' in processor '%s' should be renamed to '%s'.", clauseName, tree.simpleName(),
                            clauseName.replace("Connector", ""));
                    logAndRaiseIssue(tree, message);
                }
            }
        }

    }

}
