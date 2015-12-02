import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import com.google.common.collect.BoundType;

@Connector
public class NumberOfArgumentsInProcessorCheck {

    @Processor
    public void aMethod() {
    }

    @Processor
    public void aMethod(BoundType s1, String s2, String s3, String s4, String s5, String s6) {
    }

    @Processor
    public void aMethod(String s1, Boolean s2, String s3, SomeComplexType s4, String s5, String s6, String s7) {
    }

    @Processor // Noncompliant {{Processor 'failingMethod' has 5 complex-type parameters (more than 4, which is the maximum allowed).}}
    public void failingMethod(SomeComplexType s1, SomeComplexType s2, SomeComplexType s3, SomeComplexType s4, SomeComplexType s5) {
    }

}
