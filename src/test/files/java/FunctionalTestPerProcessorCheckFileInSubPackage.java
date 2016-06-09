import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector
public class FunctionalTestPerProcessorCheckFileInSubPackage {

    @Processor
    public void methodF() {
    }

}
