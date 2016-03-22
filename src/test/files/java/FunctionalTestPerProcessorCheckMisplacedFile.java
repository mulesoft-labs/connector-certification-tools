import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class FunctionalTestPerProcessorCheckMisplacedFile {

    @Processor
    public void methodC() { // Noncompliant {{'MethodCTestCases.java' must be placed under directory 'src/test/java/.../automation/functional'.}}
    }

}
