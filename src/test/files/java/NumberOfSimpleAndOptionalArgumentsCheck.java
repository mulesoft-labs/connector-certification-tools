import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import com.google.common.collect.BoundType;
import org.mule.api.annotations.param.Optional;

@Connector
public class NumberOfSimpleAndOptionalArgumentsInProcessorCheck {

    @Processor
    public void aMethod() {
    }

    @Processor
    public void aMethod(SompleComplexType s1, String s2, String s3, String s4, String s5) {
    }

    @Processor
    public void aMethod(SompleComplexType s1, @Optional String s2, @Optional String s3, String s4, String s5) {
    }

    @Processor
    public void aMethod(SomeComplexType s1, SomeComplexType s2, SomeComplexType s3, SomeComplexType s4, SomeComplexType s5) {
    }

    @Processor // Noncompliant {{Processor 'failingMethod' has 6 simple-type parameters marked as @Optional (more than 4, which is the maximum allowed). It's strongly recommended that all optional parameters are grouped inside a separate POJO class.}}
    public void failingMethod(@Optional String s1, @Optional Boolean s2, @Optional String s3, SomeComplexType s4, @Optional String s5, @Optional String s6, @Optional String s7) {
    }

}
