/**
 * This file is the sample code against we run our unit test.
 * It is placed src/test/files in order to not be part of the maven compilation.
 **/
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;

@Connector
class LicenseByCategoryCertifiedCheck { // Noncompliant {{@RequiresEnterpriseLicense must be defined and @RequiresEntitlement must not be present for Select and Certified category.}}

    @Processor
    public void aMethod() {
    }

}
