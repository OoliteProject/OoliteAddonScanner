/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.File;
import java.net.URL;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point for the scanner.
 * 
 * @author hiran
 */
public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);
    

    /**
     * Main entry point to run the scanner.
     * 
     * @param args command line arguments
     * @throws Exception something went wrong
     */
    public static void main(String[] args) throws Exception {
        log.info("Starting {} {} {}", Main.class.getPackage().getImplementationVendor(), Main.class.getPackage().getImplementationTitle(),  Main.class.getPackage().getImplementationVersion());

        Options options = new Options();
        options.addOption("c", "cache", true, "Path where to cache the expansions so they do not have to be downloaded for every run");
        options.addOption("o", "output", true, "Path where to write result files");
        options.addOption("m", Scanner.MAX_EXPANSION, true, "Maximum amount of expansions to parse");
        options.addOption("u", "url", true, "URL for downloading the expansions list");
        CommandLine commandline = new DefaultParser().parse(options, args);
                
        try {
            Scanner scanner = new Scanner();
            if (commandline.hasOption("cache")) {
                String cachePath = commandline.getOptionValue("c");
                scanner.setCacheDir(new File(cachePath));
            }
            if (commandline.hasOption("url")) {
                String urlStr = commandline.getOptionValue("url");
                scanner.setCatalogUrl(new URL(urlStr));
            }
            if (commandline.hasOption("output")) {
                String outputDirStr = commandline.getOptionValue("output");
                File outputdir = new File(outputDirStr);
                scanner.setOutputDir(outputdir);
            }
            if (commandline.hasOption(Scanner.MAX_EXPANSION)) {
                int max = Integer.parseInt(commandline.getOptionValue(Scanner.MAX_EXPANSION));
                scanner.setMaxExpansions(max);
            }
            scanner.run();
            // TODO: Check is scanner was successful
            
            System.exit(0);
        } catch (Exception e) {
            log.error("Usage Error", e);
            System.exit(2);
        }
    }
}
