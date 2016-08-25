public class RedundantConnectorException extends Exception { // Noncompliant {{Exception class 'RedundantConnectorException' should be renamed to 'RedundantException'.}}

    public RedundantConnectorException(String message) {
        super(message);
    }

}
