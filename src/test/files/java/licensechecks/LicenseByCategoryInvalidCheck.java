import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class LicenseByCategoryInvalidCheck { // Noncompliant {{Invalid category specified in pom.xml}}

    @Processor
    public void aMethod() {
    }

}
