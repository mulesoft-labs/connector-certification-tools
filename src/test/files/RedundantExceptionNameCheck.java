@Connector
public class RedundantExceptionNameCheck {

    @Processor
    public void failingMethod() throws RedundantExceptionNameCheckConnectorException {

    }
}