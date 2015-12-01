import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import com.google.common.collect.BoundType;

@Connector
public class NumberOfSimpleAndOptionalArgumentsInProcessorCheck {

    @Processor
    public void aMethod() {
    }

    @Processor
    public void aMethod(SompleComplexType s1, String s2, String s3, String s4, String s5) {
    }

    @Processor
    public void aMethod(SomeComplexType s1, SomeComplexType s2, SomeComplexType s3, SomeComplexType s4, SomeComplexType s5) {
    }

    @Processor
    public void failingMethod(String s1, Boolean s2, String s3, SomeComplexType s4, String s5, String s6, String s7) {
    }

}
