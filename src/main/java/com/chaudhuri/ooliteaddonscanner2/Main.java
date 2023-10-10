/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.CustomSearch;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
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
        log.info("Args {}", Arrays.asList(args));

        Options options = new Options();
        options.addOption("c", "cache", true, "Path where to cache the expansions so they do not have to be downloaded for every run");
        options.addRequiredOption("o", "out", true, "Path where to write result files");
        options.addOption("m", Scanner.MAX_EXPANSION, true, "Maximum amount of expansions to parse");
        options.addOption("M", "models", true, "Parse ship models");
        options.addOption("u", "url", true, "URL for downloading the expansions list");
        options.addOption("s", "customSearch", true, "File with custom searches. Use of this will deactivate the full index (unless forced via --fullIndex)");
        options.addOption("f", "fullIndex", false, "Build full index, even with custom searches");
        options.addOption("z", "zip", false, "Zip up result files");
                
        try {
            CommandLine commandline = new DefaultParser().parse(options, args);

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
            if (commandline.hasOption("customSearch")) {
                // TODO: parse all the customSearch files
                String[] searchFilenames = commandline.getOptionValues("customSearch");
                for (String searchFilename: searchFilenames) {
                    scanner.addCustomSearches(CustomSearch.parseSearchFilename(searchFilename));
                }
            }
            scanner.setFullIndex(commandline.hasOption("fullIndex"));
            scanner.setZip(commandline.hasOption("zip"));
            scanner.setReadModels(commandline.hasOption("models"));

            scanner.run();

            if (scanner.isSuccessful()) {
                log.info("Success. Terminating...");
                System.exit(0);
            } else {
                log.error("Could not scan", scanner.getFailure());
                System.exit(1);
            }
        } catch (Exception e) {
            log.error("Usage Error", e);

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getName(), options);

            System.exit(2);
        }
    }
}
