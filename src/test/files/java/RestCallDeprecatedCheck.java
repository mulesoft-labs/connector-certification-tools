@Connector
public abstract class RestCallDeprecatedCheck {

    @Processor
    @RestCall(uri = "http://mulesoft.com/api/aService/?someParam=0")
    public abstract void failingMethod();
}