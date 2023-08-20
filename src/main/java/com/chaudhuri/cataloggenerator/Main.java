/*
 */

package com.chaudhuri.cataloggenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * CLI for the catalog generator.
 *
 * @author hiran
 */
public class Main {
    private static final Logger log = LogManager.getLogger();

    /**
     * Parses the command line and runs the generator.
     * <p>
     * Known parameters:
     *   --input a file containing the list of URLs, each on one line.
     *   --output the catalog file to write
     *   --format either one of XML, JSON or plist (default)
     * 
     * @param args 
     */
    public static void main(String[] args) {
        
    }
}
