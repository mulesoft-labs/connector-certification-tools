package org.mule.tools.devkit.sonar;

import org.sonar.plugins.java.api.CheckRegistrar;

public class ConnectorCertificationCheckRegistrar implements CheckRegistrar {

    @Override
    public void register(CheckRegistrar.RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(ConnectorCertificationRulesDefinition.getJavaRepositoryKey(), ConnectorsChecks.javaChecks(),
                ConnectorsChecks.javaTestChecks());
    }
}
