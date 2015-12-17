package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.util.List;

@Rule(key = ConfigFriendlyNameCheck.KEY, name = "@Config 'friendlyName' must follow a convention.", description = "Checks that @MetadataCategory follows a naming convention.", priority = Priority.MAJOR, tags = { "connector-certification" })
public class ConfigFriendlyNameCheck extends BaseLoggingVisitor {

    public static final String KEY = "config-friendly-name";
    public static final String DEFAULT_CONFIG_NAME = "Configuration";
    public static final String OAUTH_CONFIG_NAME = "OAuth 1.0";
    public static final String OAUTH_2_CONFIG_NAME = "OAuth 2.0";

    @Override
    public final void visitClass(ClassTree tree) {
        final AnnotationTree annotation = Iterables.find(tree.modifiers().annotations(), ClassParserUtils.hasConfigAnnotationPredicate(), null);

        if (annotation != null) {
            String annotationName = ((IdentifierTree) annotation.annotationType()).name();
            final List<ExpressionTree> arguments = annotation.arguments();

            if (arguments.isEmpty()) {
                logAndRaiseIssue(annotation,
                        String.format("@%s must define a friendlyName. If there is a single configuration, 'Configuration' must be used as friendlyName.", annotationName));
            } else {
                AssignmentExpressionTree argument;
                if (arguments.size() > 0) {
                    argument = (AssignmentExpressionTree) Iterables.find(arguments, new Predicate<ExpressionTree>() {

                        @Override
                        public boolean apply(@Nullable ExpressionTree input) {
                            return input != null && input.is(Tree.Kind.ASSIGNMENT) && "friendlyName".equals(((AssignmentExpressionTree) input).variable().toString());
                        }
                    }, null);
                } else {
                    argument = (AssignmentExpressionTree) Iterables.getOnlyElement(arguments);
                }

                final LiteralTree expression = ((LiteralTree) argument.expression());
                String friendlyName = expression.token().text().replaceAll("^\"|\"$", "");

                switch (annotationName) {
                    case "Configuration":
                    case "ConnectionManagement":
                        if (!friendlyName.equals(DEFAULT_CONFIG_NAME)) {
                            logAndRaiseIssue(
                                    tree,
                                    String.format(
                                            "Single connector configurations annotated with @ConnectionManagement or @Configuration must use 'Configuration' as friendly name (instead of '%s'). If there are multiple configurations, none of which is of type @OAuth or @OAuth2, use a brief description to specify the configuration name (e.g, 'SAML 2.0').",
                                            friendlyName));
                        }
                        break;

                    case "OAuth":
                        if (!friendlyName.equals(OAUTH_CONFIG_NAME)) {
                            logAndRaiseIssue(
                                    tree,
                                    String.format(
                                            "For multiple connector configurations, if one or more are annotated with @OAuth, the default friendly name must be 'OAuth 1.0' (instead of '%s').",
                                            friendlyName));
                        }
                        break;

                    case "OAuth2":
                        if (!friendlyName.equals(OAUTH_2_CONFIG_NAME)) {
                            logAndRaiseIssue(
                                    tree,
                                    String.format(
                                            "For multiple connector configurations, if one or more are annotated with @OAuth2, the default friendly name must be 'OAuth 2.0' (instead of '%s').",
                                            friendlyName));
                        }
                        break;

                    default:
                        logAndRaiseIssue(tree, "Invalid configuration type specified in @Config class.");
                        break;
                }
            }
        }

    }

}
