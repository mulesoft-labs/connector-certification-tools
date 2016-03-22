package whatever.someconnector; // Noncompliant {{Connector base package found 'whatever.someconnector'. Connector's base package should be 'org.mule.modules.connectorname'.}}

import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class BasePackageCheck {

    @Processor
    public void aMethod() {
    }

}
