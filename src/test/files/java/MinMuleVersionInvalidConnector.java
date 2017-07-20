import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;

@Connector()// Noncompliant {{The connector should specify a minMuleVersion}}
public class MinMuleVersionInvalidConnector {

}