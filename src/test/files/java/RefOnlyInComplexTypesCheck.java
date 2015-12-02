import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class RefOnlyInComplexTypesCheck {

    @Processor
    public void failingMethod(SomeComplexType s1) {
    }

}
