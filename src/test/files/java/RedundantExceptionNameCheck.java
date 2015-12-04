@Connector
public class RedundantExceptionNameCheck {

    @Processor
    public void failingMethod() throws RedundantExceptionNameCheckConnectorException { // Noncompliant {{Exception 'RedundantExceptionNameCheckConnectorException' in processor 'failingMethod' should be renamed to 'RedundantExceptionNameCheckException'.}}

    }
}