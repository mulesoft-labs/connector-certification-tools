import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector(minMuleVersion="")// Noncompliant {{The value of minMuleVersion can not be empty}}
public class MinMuleVersionValidConnector {

}
