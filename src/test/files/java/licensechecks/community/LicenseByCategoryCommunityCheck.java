import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.licensing.RequiresEntitlement;

@Connector
@RequiresEntitlement
public class LicenseByCategoryCommunityCheck { // Noncompliant {{@RequiresEnterpriseLicense and @RequiresEntitlement must not be present for Community category.}}

    @Processor
    public void aMethod() {
    }

}
