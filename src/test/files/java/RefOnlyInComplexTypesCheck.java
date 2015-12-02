import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class RefOnlyInComplexTypesCheck {

    @Processor
    public void failingMethod(SomeComplexType s1) { // Noncompliant {{Processor 'failingMethod' contains variable 's1' of type 'SomeComplexType' (complex type) not annotated with @RefOnly.}}
    }

}
