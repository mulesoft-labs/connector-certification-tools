package org.mule.tools.devkit.sonar;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;

public class ConnectorCertificationStructProfile extends ProfileDefinition {

    @Override
    public RulesProfile createProfile(ValidationMessages validation) {
        return RulesProfile.create(ConnectorCertificationRulesDefinition.REPOSITORY_NAME, "struct");
    }

}
