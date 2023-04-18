/*
 */
package com.chaudhuri.ooliteaddonscanner2;

/**
 * An Exception marking up problems while processing templates.
 * 
 * @author hiran
 */
public class TemplateEngineException extends Exception {

    /**
     * Creates a new TemplateEngineException with a message.
     * 
     * @param message the message
     */
    public TemplateEngineException(String message) {
        super(message);
    }

    /**
     * Creates a new TemplateEngineException with a message and a cause.
     * 
     * @param message the message
     * @param cause the cause
     */
    public TemplateEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new TemplateEngineException with a cause.
     * 
     * @param cause the cause
     */
    public TemplateEngineException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new TemplateEngineException with a message and a cause and more.
     * 
     * @param message the message
     * @param cause the cause
     * @param enableSuppression see parent class
     * @param writableStackTrace see parent class
     */
    public TemplateEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
