import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class TestingFrameworkNotOverwrittenCheck {

    @Processor
    public void aMethod() {
    }

}
