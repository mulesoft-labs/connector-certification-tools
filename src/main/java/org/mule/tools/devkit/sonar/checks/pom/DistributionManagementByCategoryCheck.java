package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

import java.util.List;

@Rule(key = DistributionManagementByCategoryCheck.KEY, name = "Distribution Management must be properly configured in pom.xml", description = "Checks that <distributionManagement> is declared in pom.xml and correctly configured (both <repository> and <snapshotRepository>) based on the connector category.", tags = { "connector-certification" })
@ActivatedByDefault
public class DistributionManagementByCategoryCheck implements PomCheck {

    public static final String KEY = "distribution-management-by-category";

    @Override
    public Iterable<PomIssue> analyze(MavenProject mavenProject) {
        final List<PomIssue> issues = Lists.newArrayList();
        String category = mavenProject.getProperties().getProperty("category");
        DistributionManagement distribution = mavenProject.getDistributionManagement();

        if (distribution == null) {
            logAndRaiseIssue(issues, "Distribution Management must be properly configured in pom.xml under a <distributionManagement> tag, according to connector category.");
        } else {
            DeploymentRepository deploymentRepository = distribution.getRepository();
            DeploymentRepository snapshotRepository = distribution.getSnapshotRepository();

            if (deploymentRepository == null) {
                logAndRaiseIssue(issues, "Distribution Management is missing required <repository> configuration.");
            } else if (snapshotRepository == null) {
                logAndRaiseIssue(issues, "Distribution Management is missing required <snapshotRepository> configuration.");
            } else {
                switch (category.toUpperCase()) {
                    case "PREMIUM":
                    case "STANDARD":
                    case "SELECT":
                    case "CERTIFIED":
                        checkPremiumOrSelectOrCertified(category, issues, deploymentRepository, snapshotRepository);
                        break;

                    case "COMMUNITY":
                        checkCommunity(category, issues, deploymentRepository, snapshotRepository);
                        break;

                    default:
                        logAndRaiseIssue(issues, "Invalid category specified in pom.xml");
                        break;
                }
            }
        }

        return issues;
    }

    private void checkCommunity(String category, @NonNull List<PomIssue> issues, @NonNull DeploymentRepository deployRepo, @NonNull DeploymentRepository snapshotRepo) {
        if (!hasDeployRepoId(deployRepo)) {
            logAndRaiseIssue(issues, String.format("%s connectors must have a <repository> tag configured with <id>mulesoft-releases</id>.", category));
        }
        if (!hasDeployRepoUrl(deployRepo)) {
            logAndRaiseIssue(issues,
                    String.format("%s connectors must have a <repository> tag configured with <url>http://repository-master.mulesoft.org/releases/</url>.", category));
        }
        if (!hasSnapshotRepoId(snapshotRepo)) {
            logAndRaiseIssue(issues, String.format("%s connectors must have a <snapshotRepository> tag configured with <id>mulesoft-snapshots</id>.", category));
        }
        if (!hasSnapshotRepoUrl(snapshotRepo)) {
            logAndRaiseIssue(issues,
                    String.format("%s connectors must have a <snapshotRepository> tag configured with <url>http://repository-master.mulesoft.org/releases/</url>.", category));
        }
        if (!hasSnapshotRepoUniqueVersion(snapshotRepo)) {
            logAndRaiseIssue(issues,
                    String.format("%s connectors must have a <snapshotRepository> tag configured with <uniqueVersion>false</uniqueVersion>. Default value is 'true'.", category));
        }
    }

    private void checkPremiumOrSelectOrCertified(String category, @NonNull List<PomIssue> issues, @NonNull DeploymentRepository deployRepoEE,
            @NonNull DeploymentRepository snapshotRepoEE) {
        if (!hasDeployRepoEEId(deployRepoEE)) {
            logAndRaiseIssue(issues, String.format("%s connectors must have a <repository> tag configured with <id>mulesoft-ee-releases</id>.", category));
        }
        if (!hasDeployRepoEEUrl(deployRepoEE)) {
            logAndRaiseIssue(issues, String.format(
                    "%s connectors must have a <repository> tag configured with <url>https://repository-master.mulesoft.org/nexus/content/repositories/releases-ee/</url>.",
                    category));
        }
        if (!hasSnapshotRepoEEId(snapshotRepoEE)) {
            logAndRaiseIssue(issues, String.format("%s connectors must have a <snapshotRepository> tag configured with <id>mulesoft-ee-snapshots</id>.", category));
        }
        if (!hasSnapshotRepoEEUrl(snapshotRepoEE)) {
            logAndRaiseIssue(issues, String.format(
                    "%s connectors must have a <snapshotRepository> tag configured with <url>repository-master.mulesoft.org/nexus/content/repositories/ci-snapshots/</url>.",
                    category));
        }
        if (!hasSnapshotRepoUniqueVersion(snapshotRepoEE)) {
            logAndRaiseIssue(issues,
                    String.format("%s connectors must have a <snapshotRepository> tag configured with <uniqueVersion>false</uniqueVersion>. Default value is 'true'.", category));
        }
    }

    private boolean hasDeployRepoId(DeploymentRepository deployRepo) {
        return deployRepo.getId().equals("mulesoft-releases");
    }

    private boolean hasDeployRepoUrl(DeploymentRepository deployRepo) {
        return deployRepo.getUrl().equals("http://repository-master.mulesoft.org/releases/");
    }

    private boolean hasSnapshotRepoId(DeploymentRepository snapshotRepo) {
        return snapshotRepo.getId().equals("mulesoft-snapshots") && snapshotRepo.getUrl().equals("http://repository-master.mulesoft.org/snapshots/")
                && !snapshotRepo.isUniqueVersion();
    }

    private boolean hasSnapshotRepoUrl(DeploymentRepository snapshotRepo) {
        return snapshotRepo.getUrl().equals("http://repository-master.mulesoft.org/snapshots/");
    }

    private boolean hasSnapshotRepoUniqueVersion(DeploymentRepository snapshotRepo) {
        return !snapshotRepo.isUniqueVersion();
    }

    private boolean hasDeployRepoEEId(DeploymentRepository deployRepoEE) {
        return deployRepoEE.getId().equals("mule-ee-releases");
    }

    private boolean hasDeployRepoEEUrl(DeploymentRepository deployRepoEE) {
        return deployRepoEE.getUrl().equals("https://repository-master.mulesoft.org/nexus/content/repositories/releases-ee/");
    }

    private boolean hasSnapshotRepoEEId(DeploymentRepository snapshotRepoEE) {
        return snapshotRepoEE.getId().equals("mule-ee-snapshots");
    }

    private boolean hasSnapshotRepoEEUrl(DeploymentRepository snapshotRepoEE) {
        return snapshotRepoEE.getUrl().equals("https://repository-master.mulesoft.org/nexus/content/repositories/ci-snapshots/");
    }

    private final void logAndRaiseIssue(List<PomIssue> issues, String message) {
        issues.add(new PomIssue(KEY, message));
    }

}
