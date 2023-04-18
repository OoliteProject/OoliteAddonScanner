/*
 */
package com.chaudhuri.ooliteaddonscanner2;

/**
 * Exception marking up problems in the Registry.
 * 
 * @author hiran
 */
public class RegistryException extends Exception {

    /**
     * Creates a new RegistryException with a message.
     * 
     * @param message the message
     */
    public RegistryException(String message) {
        super(message);
    }

    /**
     * Creates a new RegistryException with a message and a cause.
     * 
     * @param message the message
     * @param cause the cause
     */
    public RegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new RegistryException with a cause.
     * 
     * @param cause the cause
     */
    public RegistryException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new RegistryException with a message, a cause and more.
     * 
     * @param message the message
     * @param cause the cause
     * @param enableSuppression see parent class
     * @param writableStackTrace see parent class
     */
    public RegistryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
