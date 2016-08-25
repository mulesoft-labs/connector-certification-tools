package org.mule.tools.devkit.sonar;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

import static org.mule.tools.devkit.sonar.ConnectorCertificationRulesDefinition.REPOSITORY_NAME;
import static org.mule.tools.devkit.sonar.checks.git.GitLanguage.KEY;
import static org.sonar.api.profiles.RulesProfile.create;

public class ConnectorCertificationGitProfile extends ProfileDefinition {

    @Override
    public RulesProfile createProfile(ValidationMessages validation) {
        return create(REPOSITORY_NAME, KEY);
    }

}
