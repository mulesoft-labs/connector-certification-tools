import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.licensing.RequiresEntitlement;

@Connector
@RequiresEntitlement
public class LicenseByCategoryCommunityCheck {

    @Processor
    public void aMethod() {
    }

}
