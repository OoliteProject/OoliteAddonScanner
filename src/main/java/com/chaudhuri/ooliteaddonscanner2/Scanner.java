/*
 */

package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.CustomSearch;
import com.chaudhuri.ooliteaddonscanner2.model.CustomSearch.Hit;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
    private boolean fullIndex = false;
    
    /** Runtime error that caused the scanner to break. */
    private Throwable failure;
    
    private Registry registry;
    private ExpansionCache cache;

    /**
     * Creates a new Scanner and initializes default parameters.
     * 
     * @throws MalformedURLException something went wrong
     */
    public Scanner() throws MalformedURLException {
        this.catalogUrl = new URL("http://addons.oolite.space/api/1.0/overview");
    }

    /**
     * Returns whether a full index is generated even if we have custom searches.
     * @return true if the full index will be built
     */
    public boolean isFullIndex() {
        return fullIndex;
    }

    /**
     * Sets whether a full index is generated even if we have custom searches.
     * @param fullIndex true if the full index shall be built
     */
    public void setFullIndex(boolean fullIndex) {
        this.fullIndex = fullIndex;
    }

    /**
     * Adds a new custom search.
     * @param cs the search to add
     */
    public void addCustomSearch(CustomSearch cs) {
        if (customSearches == null) {
            customSearches = new ArrayList<>();
        }
        if (cs==null) {
            throw new IllegalArgumentException("cs must not be null");
        }
        if (cs.getPattern()==null) {
            throw new IllegalArgumentException("Custom search must have a pattern");
        }
        customSearches.add(cs);
    }

    /**
     * Adds a list of custom searches.
     * @param list the list of searches to add
     */
    public void addCustomSearches(List<CustomSearch> list) {
        if (list == null) {
            throw new IllegalArgumentException("list must not be null");
        }
        if (customSearches == null) {
            customSearches = new ArrayList<>();
        }
        for (CustomSearch cs: list) {
            if (cs==null) {
                throw new IllegalArgumentException("Custom search must not be null");
            }
            if (cs.getPattern()==null) {
                throw new IllegalArgumentException("Custom search must have a pattern");
            }
        }
        customSearches.addAll(list);
    }

    /**
     * Returns the list of configured custom searches.
     * 
     * @return the list
     */
    public List<CustomSearch> getCustomSearches() {
        return customSearches;
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
        ThreadPoolExecutor tpe = (ThreadPoolExecutor)Executors.newFixedThreadPool(24, new NamedThreadFactory("WikiScanner"));
        
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
    
    private List<CustomSearch> getInterestedCustomSearches(String filename) {
        List<CustomSearch> result = new ArrayList<>();
        for (CustomSearch cs: customSearches) {
            if (cs.willSearch(filename)) {
                result.add(cs);
            }
        }
        return result;
    }
    
    private void doCustomSearches() {
        if (customSearches == null || customSearches.isEmpty()) {
            return;
        }
        
        for (CustomSearch cs: customSearches) {
            cs.init();
        }
        
        List<Expansion> expansions = registry.getExpansions();
        int expansionCount = 0;
        Instant t0 = Instant.now();
        
        // for all expansions
        for (Expansion expansion: expansions) {
            expansionCount++;
            log.info("Scanning {}/{} {}", expansionCount, expansions.size(), expansion.getIdentifier());
            try {
                ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(expansion.getDownloadUrl())));
                ZipEntry zentry = null;
                // for all files in there
                while ((zentry = zin.getNextEntry()) != null) {
                    if (zentry.isDirectory()) {
                        continue;
                    }
                    
                    String source = expansion.getIdentifier() + "!" + zentry.getName();
                    
                    // check if any custom search is interested - if not, continue with next
                    List<CustomSearch> searches = getInterestedCustomSearches(zentry.getName());
                    if (!searches.isEmpty()) {
                        log.debug("Participating searches for {}:", source);
                        for (CustomSearch cs: searches) {
                            log.debug("  {}", cs);
                        }
                        
                        // open stream, loop over lines
                        InputStream in = AddonsUtil.getZipEntryStream(zin);
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                            int lineNumber = 0;
                            while (reader.ready()) {
                                String line = reader.readLine();
                                lineNumber++;
                                
                                for (CustomSearch cs: searches) {
                                    cs.searchLine(expansion.getIdentifier(), zentry.getName(), lineNumber, line);
                                }
                            }
                        }
                        // run all the interested custom searches
                    }
                }
            } catch (Exception e) {
                log.warn("Could not run custom searches on {}", expansion, e);
            }
        }
        
        Instant t1 = Instant.now();
        List<Hit> hits = customSearches.stream().flatMap(t->t.getResults().stream()).collect(Collectors.toList());
        Duration d = Duration.between(t0, t1);
        
        log.info("Scanned {} expansions, found {} hits in {}", expansions.size(), hits.size(), d);
        // collect and return results
    }
    
    /**
     * Returns whether the scan was successful.
     * @return the status
     */
    public boolean isSuccessful() {
        return failure == null;
    }
    
    /**
     * Returns a reason of failure.
     * @return the reason
     */
    public Throwable getFailure() {
        return failure;
    }
    
    @Override
    public void run() {
        try {
            if (! outputDir.exists()) {
                log.info("Creating output directory {}", outputDir);
                outputDir.mkdirs();
            }
            
            registry = new Registry();
            registry.setProperty("expansionManagerUrl", catalogUrl.toString());
            cache = new ExpansionCache(cacheDir);
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

            if (fullIndex || customSearches == null || customSearches.isEmpty()) {
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
            }

            if (customSearches != null && !customSearches.isEmpty()) {
                doCustomSearches();
                Properties props = new Properties();
                props.put("searches", customSearches);
                templateEngine.process(props, "customSearches.ftlh", new File(outputDir, "customSearches.html"));
            }

        } catch (Exception e) {
            failure = new Exception("Scan failed", e);
            log.info("Scan failed", e);
        }
    }
    
}
