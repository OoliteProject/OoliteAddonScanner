/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

/**
 * Any entity that can host warnings.
 * 
 * @author hiran
 */
public interface Warnable {
   
    /**
     * Adds a warning to this equipment.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning);
}
