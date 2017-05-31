import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector(minMuleVersion=3.5)// Noncompliant {{The value of minMuleVersion must be a string}}
public class MinMuleVersionValidConnector {

}
