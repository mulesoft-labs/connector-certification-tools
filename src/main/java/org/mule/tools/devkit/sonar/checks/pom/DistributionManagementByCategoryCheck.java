package org.mule.tools.devkit.sonar.checks.pom;

import com.google.common.collect.Lists;
import org.apache.maven.model.DeploymentRepository;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.project.MavenProject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.LoggerFactory;
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
                        checkPremiumOrSelectOrCertified(category, issues, deploymentRepository, snapshotRepository);
                        break;

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

    private void checkCommunity(String category, @NonNull List<PomIssue> issues, @NonNull DeploymentRepository deploymentRepository,
            @NonNull DeploymentRepository snapshotRepository) {
        if (!(deploymentRepository.getId().equals("mulesoft-releases") && deploymentRepository.getName().equals("MuleSoft Repository")
                && deploymentRepository.getUrl().equals("http://repository-master.mulesoft.org/releases/") && snapshotRepository.getId().equals("mulesoft-snapshots")
                && snapshotRepository.getName().equals("MuleSoft Snapshot Repository") && snapshotRepository.getUrl().equals("http://repository-master.mulesoft.org/snapshots/") && !snapshotRepository
                    .isUniqueVersion())) {
            logAndRaiseIssue(issues, String.format("Distribution Management must be properly configured in pom.xml for %s category.", category));
        }

    }

    private void checkPremiumOrSelectOrCertified(String category, @NonNull List<PomIssue> issues, @NonNull DeploymentRepository deploymentRepository,
            @NonNull DeploymentRepository snapshotRepository) {
        if (!(deploymentRepository.getId().equals("mule-ee-releases") && deploymentRepository.getName().equals("MuleEE Releases Repository")
                && deploymentRepository.getUrl().equals("https://repository-master.mulesoft.org/nexus/content/repositories/releases-ee/")
                && snapshotRepository.getId().equals("mule-ee-snapshots") && snapshotRepository.getName().equals("MuleEE Snapshots Repository")
                && snapshotRepository.getUrl().equals("https://repository-master.mulesoft.org/snapshots/") && !snapshotRepository.isUniqueVersion())) {
            logAndRaiseIssue(issues, String.format("Distribution Management must be properly configured in pom.xml for %s category.", category));
        }

    }

    private final void logAndRaiseIssue(List<PomIssue> issues, String message) {
        LoggerFactory.getLogger(getClass()).info(message);
        issues.add((new PomIssue(KEY, message)));
    }

}
