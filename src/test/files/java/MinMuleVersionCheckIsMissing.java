import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector(connector = "connector")// Noncompliant {{@Connector should declare a 'minMuleVersion'.}}
public class MinMuleVersionInvalidConnector {

}