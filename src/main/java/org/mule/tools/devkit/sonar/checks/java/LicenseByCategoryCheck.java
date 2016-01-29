package org.mule.tools.devkit.sonar.checks.java;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.maven.project.MavenProject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.licensing.RequiresEntitlement;
import org.mule.tools.devkit.sonar.checks.ConnectorCategory;
import org.mule.tools.devkit.sonar.utils.ClassParserUtils;
import org.mule.tools.devkit.sonar.utils.PomUtils;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.tree.AnnotationTree;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.Tree;

import java.io.File;
import java.util.List;

@Rule(key = LicenseByCategoryCheck.KEY, name = "Licensing annotations must match the category declared in pom.xml", description = "Checks the correct usage of @RequiresEnterpriseLicense and @RequiresEntitlement according to category defined in pom.xml.", priority = Priority.BLOCKER, tags = { "connector-certification" })
public class LicenseByCategoryCheck extends AbstractConnectorClassCheck {

    public static final String KEY = "license-by-category";

    public static final Predicate<AnnotationTree> HAS_REQUIRES_ENTERPRISE_LICENSE_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RequiresEnterpriseLicense.class);
        }
    };

    public static final Predicate<AnnotationTree> HAS_REQUIRES_ENTITLEMENT_ANNOTATION = new Predicate<AnnotationTree>() {

        @Override
        public boolean apply(@Nullable AnnotationTree input) {
            return input != null && ClassParserUtils.is(input, RequiresEntitlement.class);
        }
    };

    private final FileSystem fileSystem;

    public LicenseByCategoryCheck() {
        this.fileSystem = null;
    }

    /**
     * This constructor exists for testing only
     */
    @VisibleForTesting
    protected LicenseByCategoryCheck(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    protected void verifyConnector(@NotNull ClassTree classTree, @NotNull IdentifierTree connectorAnnotation) {
        MavenProject mavenProject = fileSystem != null ? PomUtils.createMavenProjectFromPomFile(fileSystem.baseDir()) : PomUtils.createMavenProjectFromPomFile(new File("."));
        ConnectorCategory category = PomUtils.category(mavenProject);
        final List<? extends AnnotationTree> annotations = classTree.modifiers().annotations();

        boolean hasEnterpriseAnnotation = Iterables.any(annotations, HAS_REQUIRES_ENTERPRISE_LICENSE_ANNOTATION);
        boolean hasEntitlementAnnotation = Iterables.any(annotations, HAS_REQUIRES_ENTITLEMENT_ANNOTATION);

        switch (category) {
            case PREMIUM:
                checkPremium(classTree, annotations, hasEnterpriseAnnotation, hasEntitlementAnnotation);
                break;

            case STANDARD:
            case SELECT:
            case CERTIFIED:
                checkSelectOrCertified(classTree, hasEnterpriseAnnotation, hasEntitlementAnnotation);
                break;

            case COMMUNITY:
                checkCommunity(classTree, hasEnterpriseAnnotation, hasEntitlementAnnotation);
                break;

            default:
                logAndRaiseIssue(classTree, "Invalid category specified in pom.xml");
                break;
        }
    }

    private void checkCommunity(@NotNull ClassTree classTree, boolean hasEnterpriseAnnotation, boolean hasEntitlementAnnotation) {
        if (hasEnterpriseAnnotation || hasEntitlementAnnotation) {
            logAndRaiseIssue(classTree, "@RequiresEnterpriseLicense and @RequiresEntitlement must not be present for Community category.");
        }
    }

    private void checkSelectOrCertified(@NotNull ClassTree classTree, boolean hasEnterpriseAnnotation, boolean hasEntitlementAnnotation) {
        if (!hasEnterpriseAnnotation || hasEntitlementAnnotation) {
            logAndRaiseIssue(classTree, "@RequiresEnterpriseLicense must be defined and @RequiresEntitlement must not be present for Select and Certified category.");
        }
    }

    private void checkPremium(@NotNull ClassTree classTree, List<? extends AnnotationTree> annotations, boolean hasEnterpriseAnnotation, boolean hasEntitlementAnnotation) {
        if (!hasEnterpriseAnnotation || !hasEntitlementAnnotation) {
            logAndRaiseIssue(classTree, "@RequiresEnterpriseLicense and @RequiresEntitlement need to be defined for Premium category.");
        }

        if (hasEntitlementAnnotation) {
            final AnnotationTree annotation = Iterables.find(annotations, HAS_REQUIRES_ENTITLEMENT_ANNOTATION);
            final List<? extends ExpressionTree> arguments = annotation.arguments();
            final ExpressionTree find = Iterables.find(arguments, new Predicate<ExpressionTree>() {

                @Override
                public boolean apply(@Nullable ExpressionTree input) {
                    return input != null && input.is(Tree.Kind.ASSIGNMENT) && "name".equals(((AssignmentExpressionTree) input).variable().toString());
                }
            }, null);
            if (find == null) {
                logAndRaiseIssue(classTree, "'name' attribute must be defined for @RequiresEntitlement using connector name.");
            }
        }
    }

}
