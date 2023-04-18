/*
 */
package com.chaudhuri.ooliteaddonscanner2;

/**
 * An Exception marking up problems in an OXP.
 * 
 * @author hiran
 */
public class OxpException extends Exception {

    /**
     * Creates a new OxpException with a message.
     * 
     * @param message the message
     */
    public OxpException(String message) {
        super(message);
    }

    /**
     * Creates a new OxpException with a message and a cause.
     * 
     * @param message the message
     * @param cause the cause
     */
    public OxpException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new OxpException with a cause.
     * 
     * @param cause the cause
     */
    public OxpException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new OxpException with a message and a cause and more.
     * 
     * @param message the message
     * @param cause the cause
     * @param enableSuppression see parent class
     * @param writableStackTrace see parent class
     */
    public OxpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
