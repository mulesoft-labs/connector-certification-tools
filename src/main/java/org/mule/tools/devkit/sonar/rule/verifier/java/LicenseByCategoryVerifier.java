package org.mule.tools.devkit.sonar.rule.verifier.java;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.util.Trees;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.licensing.RequiresEntitlement;
import org.mule.tools.devkit.sonar.ClassParserUtils;
import org.mule.tools.devkit.sonar.Context;
import org.mule.tools.devkit.sonar.Rule;

import java.util.List;
import java.util.Optional;

public class LicenseByCategoryVerifier extends ConnectorClassVerifier {

    public LicenseByCategoryVerifier(Rule.@NonNull Documentation doc) {
        super(doc);
    }

    @Override protected void verifyConnector(@NonNull ClassTree classTree, @NonNull Trees trees) {

        final List<? extends AnnotationTree> annotations = classTree.getModifiers().getAnnotations();
        boolean hasEnterpriseAnnotation = annotations.stream().anyMatch(a -> ClassParserUtils.is(a, RequiresEnterpriseLicense.class));
        boolean hasEntitlementAnnotation = annotations.stream().anyMatch(a -> ClassParserUtils.is(a, RequiresEntitlement.class));

        final Context context = Context.getInstance();
        switch (context.getCategory()) {
            case "PREMIUM": {
                if (!hasEnterpriseAnnotation || !hasEntitlementAnnotation) {
                    addError(null, "@RequiresEnterpriseLicense and @RequiresEntitlement need to be defined for Premium category.");
                }

                final Optional<? extends AnnotationTree> annotation = annotations.stream().filter(a -> ClassParserUtils.is(a, RequiresEntitlement.class)).findAny();
                if (annotation.isPresent()) {
                    final List<? extends ExpressionTree> arguments = annotation.get().getArguments();
                    if (!arguments.stream().anyMatch(a -> a.toString().startsWith("name"))) {
                        addError(null, "'name' attribute must be defined for @RequiresEntitlement using connector name.");
                    }
                }

                break;
            }
            case "SELECT":
            case "STANDARD": {
                if (!hasEnterpriseAnnotation || hasEntitlementAnnotation) {
                    addError(null, "@RequiresEnterpriseLicense must be defined and @RequiresEntitlement must not be present for Select and Standard category.");
                }
                break;
            }
            case "COMMUNITY": {
                if (hasEnterpriseAnnotation || hasEntitlementAnnotation) {
                    addError(null, "@RequiresEnterpriseLicense and @RequiresEntitlement must not be present for Community category.");
                }
                break;
            }
        }
    }

}
