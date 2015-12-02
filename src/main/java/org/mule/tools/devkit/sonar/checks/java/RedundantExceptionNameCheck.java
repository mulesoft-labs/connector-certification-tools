package org.mule.tools.devkit.sonar.checks.java;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = RedundantExceptionNameCheck.KEY, name = "Redundant exception names thrown by a processor", description = "Don't use the word 'Connector' to define the name of custom exceptions, e.g: <MyAwesomeConnectorException> should be <MyAwesomeException>.", tags = { "connector-certification" })
@ActivatedByDefault
public class RedundantExceptionNameCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "redundant-exception-name";
    private static final RuleKey RULE_KEY = RuleKey.of(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, KEY);

    @Override
    protected RuleKey getRuleKey() {
        return RULE_KEY;
    }

    @Override
    protected void verifyProcessor(@NonNull MethodTree tree, final @NonNull IdentifierTree processorAnnotation) {

        final List<? extends ExpressionTree> thrownClauses = tree.throwsClauses();

        if (!thrownClauses.isEmpty()) {
            for (ExpressionTree clause : thrownClauses) {
                String clauseName = ((IdentifierTree) clause).name();
                if (clauseName.contains("Connector")) {
                    String newClauseName = clauseName.replace("Connector", "");
                    final String message = String.format("Exception '%s' in processor '%s' should be renamed to '%s'.", clauseName, tree.simpleName(), newClauseName);
                    logAndRaiseIssue(tree, message);
                }
            }
        }

    }

}
