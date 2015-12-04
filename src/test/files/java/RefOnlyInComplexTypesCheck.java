import java.util.List;
import java.util.Map;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class RefOnlyInComplexTypesCheck {

    @Processor
    public void failingMethod(SomeComplexType s1) { // Noncompliant {{Processor 'failingMethod' contains variable 's1' of type 'SomeComplexType' (complex type) not annotated with
                                                    // @RefOnly.}}
    }

    @Processor
    public void aMethod(List<String> s1) {
    }

    @Processor
    public void failingMethod(List<Object> s1) { // Noncompliant {{Processor 'failingMethod' contains variable 's1' of type 'List<Object>' (complex type) not annotated with
                                                 // @RefOnly.}}
    }

    @Processor
    public void failingMethod(List<? extends Integer> s1) {
    }

    @Processor
    public void failingMethod2(List<? extends Object> s1) { // Noncompliant {{Processor 'failingMethod2' contains variable 's1' of type 'List<? extends Object>' (complex type) not
                                                            // annotated with @RefOnly.}}
    }

    @Processor
    public void failingMethod3(List<?> s1) { // Noncompliant {{Processor 'failingMethod3' contains variable 's1' of type 'List<?>' (complex type) not annotated with @RefOnly.}}
    }

    @Processor
    public void aMethod(Map<String, String> m1) {
    }

    @Processor
    public void failingMethod(Map<Long, SomeComplexType> m1) { // Noncompliant {{Processor 'failingMethod' contains variable 'm1' of type 'Map<Long, SomeComplexType>' (complex
                                                               // type) not annotated with @RefOnly.}}
    }
}
