import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Payload;
import org.mule.api.annotations.param.Default;

@Connector
public class PayloadDeprecationCheck {

    @Processor
    public void aMethod(@Default("#[payload]") String id) {

    }

    @Processor
    public void failingMethod(@Payload final Object payload) { // Noncompliant {{@Payload must be removed from processor 'failingMethod' as it has been deprecated.}}

    }
}
