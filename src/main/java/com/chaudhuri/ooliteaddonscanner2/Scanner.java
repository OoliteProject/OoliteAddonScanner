/*
 */

package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.CustomSearch;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class Scanner implements Runnable {
    private static final Logger log = LogManager.getLogger();

    public static final String OOLITE_CORE = "oolite.core";
    public static final String XML_HEADER = "<?xml";
    public static final String HTML_EXTENSION = ".html";
    public static final String OXP_PATH_SCRIPTS = "Scripts/";
    public static final String MAX_EXPANSION = "maxExpansion";
    
    private List<CustomSearch> customSearches;
    private File cacheDir = ExpansionCache.DEFAULT_CACHE_DIR;
    private URL catalogUrl;
    private File outputDir = new File("output");
    private int maxExpansions = Integer.MAX_VALUE;

    /**
     * Creates a new Scanner and initializes default parameters.
     * 
     * @throws MalformedURLException something went wrong
     */
    public Scanner() throws MalformedURLException {
        this.catalogUrl = new URL("http://addons.oolite.space/api/1.0/overview/");
    }

    /**
     * Returns the expansion cache directory.
     * 
     * @return the directory
     */
    public File getCacheDir() {
        return cacheDir;
    }

    /**
     * Sets the expansion cache directory.
     * 
     * @param cacheDir the directory
     */
    public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * Returns the URL to the expansions catalog. 
     * Sometimes referred as Expamsion Manager List.
     * @return the url
     */
    public URL getCatalogUrl() {
        return catalogUrl;
    }

    /**
     * Sets the URL to the expansions catalog. 
     * Sometimes referred as Expamsion Manager List.
     * @param catalogUrl the url
     */
    public void setCatalogUrl(URL catalogUrl) {
        this.catalogUrl = catalogUrl;
    }

    /**
     * Returns the output directory.
     * 
     * @return the directory
     */
    public File getOutputDir() {
        return outputDir;
    }

    /**
     * Sets the output directory.
     * 
     * @param outputDir the directory
     */
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * Returns the maximum amount of expansions to be scanned.
     * Used in test scenarios to limit (runtime/traffic/storage) cost.
     * 
     * @return the maximum number of expansions
     */
    public int getMaxExpansions() {
        return maxExpansions;
    }

    /**
     * Sets the maximum amount of expansions to be scanned.
     * Used in test scenarios to limit (runtime/traffic/storage) cost.
     * 
     * @param maxExpansions the maximum number of expansions
     */
    public void setMaxExpansions(int maxExpansions) {
        this.maxExpansions = maxExpansions;
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
    
    @Override
    public void run() {
        try {
            Registry registry = new Registry();
            registry.setProperty("expansionManagerUrl", catalogUrl.toString());
            ExpansionCache cache = new ExpansionCache(cacheDir);
            TemplateEngine templateEngine = new TemplateEngine();

            // try to download from http://addons.oolite.org/api/1.0/overview
            File data = File.createTempFile("OoliteAddonScanner2", ".nsdata", outputDir);
            URL u = catalogUrl;
            try (InputStream in = u.openStream(); OutputStream out = new FileOutputStream(data)) {
                in.transferTo(out);
            } catch (Exception e) {
                throw new OxpException("Could not download up to date expansions list from " + u, e);
            }

            log.info("Expansions Catalog in {}", data.getAbsolutePath());
            
            AddonsUtil.readOolite(cache, registry);
            AddonsUtil.readExpansionsList(data, registry, maxExpansions);
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

            new File(outputDir, "equipment").mkdirs();
            new File(outputDir, "expansions").mkdirs();
            new File(outputDir, "ships").mkdirs();

            TemplateUtil.printIndexes(registry, outputDir, templateEngine);
            TemplateUtil.printExpansions(registry, outputDir, templateEngine);
            TemplateUtil.printEquipment(registry, outputDir, templateEngine);
            TemplateUtil.printShips(registry, outputDir, templateEngine);
            templateEngine.process(registry, "wikiIindex.ftlh", new File(outputDir, "wiki.txt"));

            AddonsUtil.zipup(outputDir);
        } catch (Exception e) {
            log.fatal("Scan failed", e);
        }
    }
    
}
