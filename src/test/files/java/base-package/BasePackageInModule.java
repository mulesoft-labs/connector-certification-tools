package org.mule.module.someconnector; // Noncompliant {{Connector base package found 'org.mule.module.someconnector'. Consider moving it to 'org.mule.modules.someconnector'.}}

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class BasePackageCheck {

    @Processor
    public void aMethod() {
    }

}
