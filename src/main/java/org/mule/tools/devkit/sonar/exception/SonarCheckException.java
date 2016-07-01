package org.mule.tools.devkit.sonar.exception;

public class SonarCheckException extends RuntimeException {

    public SonarCheckException() {
        super();
    }

    public SonarCheckException(String message) {
        super(message);
    }

    public SonarCheckException(Throwable cause) {
        super(cause);
    }

    public SonarCheckException(String message, Throwable cause) {
        super(cause);
    }
}
