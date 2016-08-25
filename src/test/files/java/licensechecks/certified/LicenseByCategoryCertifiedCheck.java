import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class LicenseByCategoryCertifiedCheck { // Noncompliant {{@RequiresEnterpriseLicense must be defined and @RequiresEntitlement must not be present for Select and Certified
                                               // category.}}

    @Processor
    public void aMethod() {
    }

}
