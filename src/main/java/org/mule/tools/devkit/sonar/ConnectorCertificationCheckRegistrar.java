package org.mule.tools.devkit.sonar;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;

public class ConnectorCertificationCheckRegistrar implements CheckRegistrar {

    @Override
    public void register(CheckRegistrar.RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(ConnectorCertificationRulesDefinition.REPOSITORY_KEY, ConnectorsChecks.javaChecks(), ImmutableList.<Class<? extends JavaCheck>>of());
    }

}
