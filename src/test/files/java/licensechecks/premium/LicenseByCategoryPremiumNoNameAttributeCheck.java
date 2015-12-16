import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.annotations.licensing.RequiresEntitlement;

@Connector
@RequiresEnterpriseLicense
@RequiresEntitlement
public class LicenseByCategoryPremiumNoNameAttributeCheck { // Noncompliant {{'name' attribute must be defined for @RequiresEntitlement using connector name.}}

    @Processor
    public void aMethod() {
    }

}
