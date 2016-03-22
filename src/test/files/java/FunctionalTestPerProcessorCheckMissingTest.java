import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class FunctionalTestPerProcessorCheck {

    @Processor
    public void methodA() {
    }

    @Processor
    public void methodB() { // Noncompliant {{There should be one functional test per @Processor. Add proper test for processor 'methodB'.}}
    }

}
