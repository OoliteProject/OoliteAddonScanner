/*
 */
package com.chaudhuri.ooliteaddonscanner2;

/**
 *
 * @author hiran
 */
public class OxpException extends Exception {

    public OxpException(String message) {
        super(message);
    }

    public OxpException(String message, Throwable cause) {
        super(message, cause);
    }

    public OxpException(Throwable cause) {
        super(cause);
    }

    public OxpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
