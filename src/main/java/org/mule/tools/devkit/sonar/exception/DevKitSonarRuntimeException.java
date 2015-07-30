package org.mule.tools.devkit.sonar.exception;

public class DevKitSonarRuntimeException extends  RuntimeException{

    public DevKitSonarRuntimeException(String message) {
        super(message);
    }

    public DevKitSonarRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DevKitSonarRuntimeException(Throwable cause) {
        super(cause);
    }
}
