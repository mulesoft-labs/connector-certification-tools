import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.licensing.RequiresEntitlement;

@Connector
@RequiresEntitlement(name = "some-connector")
public class LicenseByCategoryPremiumNoRequiresLicenseCheck {

    @Processor
    public void aMethod() {
    }

}
