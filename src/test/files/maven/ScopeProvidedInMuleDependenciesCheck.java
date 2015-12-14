import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class ScopeProvidedInMuleDependenciesCheck {

    @Processor
    public void aMethod() {
    }

}
