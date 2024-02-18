/*
 */

package com.chaudhuri.cataloggenerator;

import java.nio.file.Path;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addRequiredOption("i", "in", true, "Input file (one URL per line)");
        options.addRequiredOption("o", "out", true, "Output file");
        options.addOption("c", "cache", true, "Location to cache downloaded expansions");
        options.addOption("p", "pedantic", false, "Pedantic mode - requires the URLs to be sorted");
        options.addOption("f", "format", true, "Output format. A comma separated list of [html, json, plist, xml]. Defaults to plist");
        
        try {
            CommandLine commandline = new DefaultParser().parse(options, args);
            Generator generator = new Generator();
            if (commandline.hasOption("in")) {
                generator.setInputPath(Path.of(commandline.getOptionValue("in")));
            }
            if (commandline.hasOption("out")) {
                generator.setOutputPath(Path.of(commandline.getOptionValue("out")));
            }
            if (commandline.hasOption("format")) {
                generator.setOutputFormat(commandline.getOptionValue("format"));
            } else {
                generator.setOutputFormat("plist");
            }
            if (commandline.hasOption("c")) {
                generator.setCacheDIR(Path.of(commandline.getOptionValue("cache")));
            }
            generator.setPedantic(commandline.hasOption("pedantic"));

            log.info(Main.class.getPackage().getImplementationTitle() + " version " + Main.class.getPackage().getImplementationVersion());
            generator.call();
        } catch (MissingOptionException e) {
            log.error("Wrong invocation. Options missing: {}", e.getMissingOptions());
            
            new HelpFormatter().printHelp(Main.class.getName() + " [OPTION]...", options);
            System.exit(2);
        } catch (Exception e) {
            log.error("Generator failed.", e);
            System.exit(1);
        }
    }
}
