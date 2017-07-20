import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector(minMuleVersion="")// Noncompliant {{@Connector should declare a 'minMuleVersion'.}}
public class MinMuleVersionInvalidConnector {

}