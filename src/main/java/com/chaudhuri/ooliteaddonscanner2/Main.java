/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
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
    
    public static final String OOLITE_CORE = "oolite.core";
    public static final String XML_HEADER = "<?xml";
    public static final String HTML_EXTENSION = ".html";
    public static final String OXP_PATH_SCRIPTS = "Scripts/";
    public static final String MAX_EXPANSION = "maxExpansion";

    private static void zipupInternal(ZipOutputStream zos, File file, File root) throws IOException {
        log.debug("zipup_internal({}, {}, {})", zos, file, root);
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f: files) {
                if (!".".equals(f.getName()) && !"..".equals(f.getName())) {
                    zipupInternal(zos, f, root);
                }
            }
        } else {
            Path p1 = file.toPath();
            Path p2 = root.toPath();
            Path rel = p2.relativize(p1);
            log.info("Zip {} to {}", file, rel);
            ZipEntry ze = new ZipEntry(rel.toString());
            zos.putNextEntry(ze);
            
            // we announced something. Now copy data...
            try (FileInputStream fin = new FileInputStream(file)) {
                byte[] buffer = new byte[4096];
                int read = fin.read(buffer);
                while (read >= 0) {
                    zos.write(buffer, 0, read);
                    read = fin.read(buffer);
                }
            }
        }
    }
    
    private static void zipup(File directory) {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        File zipfile = new File(directory.getParentFile(), directory.getName()+"-"+sdf.format(d)+".zip");
        log.info("Zip {} -> {}", directory, zipfile);
        
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile))) {
            zipupInternal(zos, directory, directory.getParentFile());
        } catch(IOException e) {
            log.error("Could not zip to {}", zipfile, e);
        }
    }

    /**
     * Scans all wiki pages according to registry content.
     * Problems are reported as warnings on the registry's elements.
     * 
     * @param reg the registry
     */
    public static void scanWikipages(Registry reg) {
        log.debug("scanWikiPages({})", reg);
        
        Instant start = Instant.now();
        ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newFixedThreadPool(24);
        
        log.debug("Submitting expansions...");
        for (Wikiworthy w: reg.getExpansions()) {
            if (!OOLITE_CORE.equals(w.getIdentifier())) {
                tpe.submit(new Wiki.WikiCheck(w));
            }
        }
        log.debug("Submitting equipment...");
        for (Equipment w: reg.getEquipment()) {
            if (w.isVisible()) {
                tpe.submit(new Wiki.WikiCheck(w));
            }
        }
        log.debug("Submitting ships...");
        for (Ship w: reg.getShips()) {
            if (!w.isTemplate()) {
                tpe.submit(new Wiki.WikiCheck(w));
            }
        }
        
        log.debug("Submitted jobs");

        try {
            // give regular updates on the console while the background threads are processing
            while (!tpe.getQueue().isEmpty()) {
                Thread.sleep(5000);
                Instant now = Instant.now();
                Duration d = Duration.between(start, now);
                float speed = (float)tpe.getCompletedTaskCount() / (float)d.getSeconds();
                Duration ttc = Duration.of((int)(tpe.getQueue().size()/speed), ChronoUnit.SECONDS);
                log.info("Processing {} finished {}/{}, speed {} pps, eta={}", tpe.getActiveCount(), tpe.getCompletedTaskCount(), tpe.getTaskCount(), speed, ttc);
            }
            tpe.shutdown();
        } catch (InterruptedException ie) {
            log.error("caught exception", ie);
            tpe.shutdownNow();
            
            // Restore interrupted state...
            Thread.currentThread().interrupt();
        }
        Instant end = Instant.now();
        log.debug("Checked {} wiki lookups in {}", tpe.getTaskCount(), Duration.between(start, end));
    }
    
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
        options.addOption("m", MAX_EXPANSION, true, "Maximum amount of expansions to parse");
        options.addOption("u", "url", true, "URL for downloading the expansions list");
        CommandLine commandline = new DefaultParser().parse(options, args);
        
        String cachePath = commandline.getOptionValue("c", ExpansionCache.DEFAULT_CACHE_DIR.getAbsolutePath());
        File cacheDIR = new File(cachePath);
        
        String urlStr = commandline.getOptionValue("u", "http://addons.oolite.space/api/1.0/overview/");
        String outputDirStr = commandline.getOptionValue("o", "target/OoliteExpansionIndex");
        File outputdir = new File(outputDirStr);
        outputdir.mkdirs();
        
        // try to download from http://addons.oolite.org/api/1.0/overview
        File data = File.createTempFile("OoliteAddonScanner2", ".nsdata", outputdir);
        URL u = new URL(urlStr);
        try (InputStream in = u.openStream(); OutputStream out = new FileOutputStream(data)) {
            in.transferTo(out);
        } catch (Exception e) {
            throw new OxpException("Could not download up to date expansions list from " + u, e);
        }
        
        log.info("Want to read {}", data.getAbsolutePath());

        Registry registry = new Registry();
        registry.setProperty("expansionManagerUrl", u.toString());
        ExpansionCache cache = new ExpansionCache(cacheDIR);
        TemplateEngine templateEngine = new TemplateEngine();
        
        try {
            AddonsUtil.readOolite(cache, registry);
            
            if (commandline.hasOption(MAX_EXPANSION)) {
                int max = Integer.parseInt(commandline.getOptionValue(MAX_EXPANSION));
                AddonsUtil.readExpansionsList(data, registry, max);
            } else {
                AddonsUtil.readExpansionsList(data, registry);
            }
            log.debug("Parsed {}", registry.getExpansions().size());
            
            AddonsUtil.readOxps(cache, registry);
            
            AddonsUtil.readShipModels(cache, registry);
            
            log.info("Parsed {} OXPs", registry.getExpansions().size());
            log.info("Parsed {} equipment", registry.getEquipment().size());
            log.info("Parsed {} ships", registry.getShips().size());
            
            // scan for wiki pages
            scanWikipages(registry);
            
            // scanning finished. Now verify...
            Verifier.verify(registry);
            
            new File(outputdir, "equipment").mkdirs();
            new File(outputdir, "expansions").mkdirs();
            new File(outputdir, "ships").mkdirs();
            
            TemplateUtil.printIndexes(registry, outputdir, templateEngine);
            TemplateUtil.printExpansions(registry, outputdir, templateEngine);
            TemplateUtil.printEquipment(registry, outputdir, templateEngine);
            TemplateUtil.printShips(registry, outputdir, templateEngine);
            templateEngine.process(registry, "wikiIindex.ftlh", new File(outputdir, "wiki.txt"));
            
            zipup(outputdir);
            
            System.exit(0);
        } catch (Exception e) {
            log.error("Could not process {}", data.getAbsolutePath(), e);
            System.exit(1);
        }
    }
}
