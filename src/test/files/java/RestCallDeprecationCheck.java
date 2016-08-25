import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public abstract class RestCallDeprecationCheck {

    @Processor
    @RestCall(uri = "http://mulesoft.com/api/aService/?someParam=0")  // Noncompliant {{@RestCall should be removed from processor 'failingMethod' as it is deprecated.}}
    public abstract void failingMethod();
}
