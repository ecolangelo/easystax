package org.xml.parsing.core.builders;

/**
 * Created by eros on 27/09/14.
 */
public class BuilderInitializationException extends RuntimeException {

    public BuilderInitializationException() {
        super();
    }

    public BuilderInitializationException(String message) {
        super(message);
    }

    public BuilderInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderInitializationException(Throwable cause) {
        super(cause);
    }

    protected BuilderInitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
