/*
 */

package com.chaudhuri.cataloggenerator;

import com.chaudhuri.ooliteaddonscanner2.AddonsUtil;
import com.chaudhuri.ooliteaddonscanner2.ExpansionCache;
import com.chaudhuri.ooliteaddonscanner2.Registry;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.owlike.genson.Genson;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Reads a list of URLs and writes a catalog file.
 * 
 * @author hiran
 */
public class Generator implements Callable<Object> {
    private static final Logger log = LogManager.getLogger();

    private Path inputPath;
    private Path outputPath;
    private Path tempDir;
    private Path cacheDIR = ExpansionCache.DEFAULT_CACHE_DIR.toPath();
    private boolean pedantic;
    
    /**
     * Can be a comma delimited list of formats.
     */
    private String outputFormat;
    private int threadCount;
    
    private ExpansionCache cache;

    /**
     * Creates a new instance.
     */
    public Generator() {
    }

    /**
     * Returns whether pedantic mode is activated.
     * 
     * @return true if activated
     */
    public boolean isPedantic() {
        return pedantic;
    }

    /**
     * Sets whether pedantic mode is activated.
     * 
     * @param pedantic true if activated
     */
    public void setPedantic(boolean pedantic) {
        this.pedantic = pedantic;
    }

    /**
     * Returns the location where expansions are cached.
     * 
     * @return the location
     */
    public Path getCacheDIR() {
        return cacheDIR;
    }

    /**
     * Sets the location where expansions are cached.
     * This must happen before init() is called.
     * 
     * @return the location
     */
    public void setCacheDIR(Path cacheDIR) {
        if (cache != null) {
            throw new IllegalStateException("already initialized");
        }
        this.cacheDIR = cacheDIR;
    }

    /**
     * Gets the path/file where the input will be read from.
     * The file is expected to have one URL per line.
     * 
     * @return the path
     */
    public Path getInputPath() {
        return inputPath;
    }

    /**
     * Sets the path/file where the input will be read from.
     * The file is expected to have one URL per line.
     * 
     * @param inputPath the path
     */
    public void setInputPath(Path inputPath) {
        this.inputPath = inputPath;
    }

    /**
     * Gets the path/file where the output will be written.
     * 
     * @return the path
     */
    public Path getOutputPath() {
        return outputPath;
    }

    /**
     * Sets the path/file where the output will be written.
     * 
     * @param outputPath the path
     */
    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    /**
     * Gets the output format.
     * Valid values are html, json, xml, plist. You can supply a comma separated
     * list but then several output files are created.
     * 
     * @return the format
     */
    public String getOutputFormat() {
        return outputFormat;
    }

    /**
     * Sets the output format.
     * Valid values are html, json, xml, plist. You can supply a comma separated
     * list but then several output files are created.
     * 
     * @param outputFormat the format
     */
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * Gets the directory where to store temporary files.
     * 
     * @return the temp directory
     */
    public Path getTempDir() {
        return tempDir;
    }

    /**
     * Sets the directory where to store temporary files.
     * 
     * @param tempDir the temp directory
     */
    public void setTempDir(Path tempDir) {
        this.tempDir = tempDir;
    }

    /**
     * Gets the amount of threads to use for parallel processing.
     * 
     * @return number of threads
     */
    public int getThreadCount() {
        return threadCount;
    }

    /**
     * Sets the amount of threads to use for parallel processing.
     * 
     * @param threadCount number of threads
     */
    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }
    
    /**
     * Initializes the generator so it is ready to run.
     */
    public void init() {
        log.info("Caching expansions in {}", cacheDIR.toFile().getAbsolutePath());
        cache = new ExpansionCache(cacheDIR.toFile());
    }
    
    ExpansionManifest getManifestFromUrl(String urlString) {
        log.debug("getManifestFromUrl({})", urlString);
        if (cache == null) {
            throw new IllegalStateException("cache must not be null.");
        }
        
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(urlString)));
            ExpansionManifest em = getManifestFromOXZ(zin, urlString);
            File f = cache.getCachedFile(urlString);
            if (f != null && f.exists()) {
                em.setFileSize(String.valueOf(f.length()));
            } else {
                em.setFileSize(null);
            }
            em.setDownloadUrl(urlString);
            
            Instant i = cache.getLastModified(urlString);
            if (i != null) {
                em.setUploadDate(i.getEpochSecond());
            }
            
            return em;
        } catch (IOException e) {
            log.error("Could not access plugin {}", urlString, e);
            return null;
        }
    }

    /**
     * Scans an OXZ without extracting the whole archive.
     * 
     * @param zin the OXZ inputstream
     * @param urlString the url where it came from
     * @return the expansion manifest or null if not found
     */
    ExpansionManifest getManifestFromOXZ(ZipInputStream zin, String urlString) {
        log.debug("getManifestFromOXZ({}, {})", zin, urlString);
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        expansion.setDownloadUrl(urlString);

        try {
            ZipEntry zentry = null;
            
            while ((zentry = zin.getNextEntry()) != null) {
                AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
            }
        } catch (EOFException e) {
            log.warn("Incomplete plugin archive for {}", urlString, e);
            try {
                cache.invalidate(urlString);
                log.info("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", urlString, ex);
            }
            System.exit(1);
        } catch (ConnectException e) {
            String s = String.format("Could not download %s, %s: %s", urlString, e.getClass().getName(), e.getMessage());
            expansion.addWarning(s);
            log.error(s);
            try {
                cache.invalidate(expansion.getDownloadUrl());
                log.info("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", expansion.getDownloadUrl(), ex);
            }
            expansion.setManifest(null);
        } catch (Exception e) {
            expansion.addWarning(String.format("Could not access: %s, %s: %s", expansion.getDownloadUrl(), e.getClass().getName(), e.getMessage()));
            log.error("Could not access plugin {}", expansion.getDownloadUrl(), e);
            expansion.setManifest(null);
        }
        
        return expansion.getManifest();
    }
    
    private boolean isOrdered(List<String> urls) throws MalformedURLException {
        boolean result = true;
        
        URL lastUrl = null;
        String lastString = null;
        int lineCount = 0;
        int violations = 0;
        for (String url: urls) {
            lineCount++;
            if (url.startsWith("#") | url.isBlank()) {
                continue;
            }
            
            URL thisUrl = new URL(url);
            if (lastUrl == null) {
                lastUrl = thisUrl;
                lastString = thisUrl.getHost() + thisUrl.getPort() + thisUrl.getPath() + thisUrl.getFile() + thisUrl.getProtocol();
            } else {
                String thisString = thisUrl.getHost() + thisUrl.getPort() + thisUrl.getPath() + thisUrl.getFile() + thisUrl.getProtocol();
                int x = lastString.compareTo(thisString);
                if (x == 0) {
                    log.warn("Duplicate url detected in line {}: {}", lineCount, url);
                    violations++;
                    result = false;
                } else if ( x > 0) {
                    log.warn("Wrong sort order detected in line {}: {}", lineCount, url);
                    violations++;
                    result = false;
                }
                lastUrl = thisUrl;
                lastString = thisString;
            }
        }
        
        if (violations > 0) {
            log.error("Found {} sort order violations.", violations);
        }
        return result;
    }

    @Override
    public Object call() throws IOException {
        if (inputPath == null) {
            throw new IllegalStateException("inputPath must not be null.");
        }
        if (outputFormat == null) {
            throw new IllegalStateException("outputFormat must not be null.");
        }
        if (outputPath == null) {
            throw new IllegalStateException("outputPath must not be null.");
        }
        
        init();

        // read url list
        List<String> urls = Files.lines(inputPath)
                .collect(Collectors.toList());
        
        // check sort order of url list
        if (pedantic) {
            if (!isOrdered(urls)) {
                throw new IOException("Input data not good.");
            }
        }
        
        // create the catalog
        List<ExpansionManifest> catalog = null;
        catalog = urls.stream()
                .parallel()
                .filter(e -> !e.startsWith("#"))
                .filter(e -> !e.isBlank())
                .map(e -> {
                    ExpansionManifest em = getManifestFromUrl(e);
                    log.info("Parsed {}", e);
                    return em;
                })
                .filter(m -> m != null)
                .collect(Collectors.toList());

        log.info("Found {} manifests", catalog.size());

        // serialize the catalog
        final List<ExpansionManifest> fCatalog = catalog;
        Arrays.asList(outputFormat.split(",")).stream()
            .forEach(format -> {
                try {
                    Path parent = outputPath.getParent();
                    if (parent != null && !Files.exists(parent, LinkOption.NOFOLLOW_LINKS)) {
                        Files.createDirectories(parent);
                    }
                    
                    Path myOutputPath = outputPath;
                    if (!myOutputPath.toString().endsWith("." + format)) {
                        myOutputPath = outputPath.toAbsolutePath().resolveSibling(outputPath.getFileName() + "." + format);
                    }
                    
                    try (OutputStream os = Files.newOutputStream(myOutputPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                        switch (format) {
                            case "xml":
                                writeXml(fCatalog, os);
                                break;
                            case "plist":
                                writePlist(fCatalog, os);
                                break;
                            case "html":
                                writeHtml(fCatalog, os);
                                break;
                            case "json":
                                writeJson(fCatalog, os);
                                break;
                            default:
                                log.warn("unknown output format: {}", format);
                        }
                    }
                    
                    log.info("Wrote {}", myOutputPath.toAbsolutePath());
                } catch (Exception ex) {
                    throw new RuntimeException(String.format("Cannot write to %s in %s", format, outputPath.toAbsolutePath()), ex);
                }
            });
        
        return "success";
    }

    private void writeJson(List<ExpansionManifest> catalog, OutputStream out) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        new Genson().serialize(catalog, out);
    }

    private Element createElement(Document doc, String name, String content) {
        Element result = doc.createElement(name);
        result.appendChild(doc.createTextNode(content));
        return result;
    }
    
    private Element createDependencyList(Document doc, String name, List<Expansion.Dependency> dependencies) {
        final Element result = doc.createElement(name);
        
        dependencies.stream().forEach((dependency) -> {
            Element d = doc.createElement("dependency");
            d.appendChild(createElement(doc, "identifier", dependency.getIdentifier()));
            String v = dependency.getVersion();
            if (v == null) {
                v = "0";
            }
            d.appendChild(createElement(doc, "version", v));
            if (dependency.getMaxVersion() != null) {
                d.appendChild(createElement(doc, "maximum_version", dependency.getMaxVersion()));
            }
            if (dependency.getDescription()!= null) {
                d.appendChild(createElement(doc, "description", dependency.getDescription()));
            }
            result.appendChild(d);
        });
        
        return  result;
    }
    
    private Document generateXml(List<ExpansionManifest> catalog) throws ParserConfigurationException {
        if (catalog == null) {
            throw new IllegalArgumentException("catalog must not be null");
        }
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("catalog");
        doc.appendChild(root);
        
        catalog.stream().forEach(em -> {
            Element emNode = doc.createElement("manifest");
            emNode.setAttribute("identifier", em.getIdentifier());
            emNode.setAttribute("version", em.getVersion());
            emNode.appendChild(createElement(doc, "title", em.getTitle()));
            emNode.appendChild(createElement(doc, "required_oolite_version", em.getRequiredOoliteVersion()));
            emNode.appendChild(createElement(doc, "maximum_oolite_version", em.getMaximumOoliteVersion()));
            emNode.appendChild(createElement(doc, "description", em.getDescription()));
            emNode.appendChild(createElement(doc, "category", em.getCategory()));
            emNode.appendChild(createElement(doc, "author", em.getAuthor()));
            emNode.appendChild(createElement(doc, "license", em.getLicense()));
            emNode.appendChild(createElement(doc, "download_url", em.getDownloadUrl()));
            emNode.appendChild(createElement(doc, "information_url", em.getInformationUrl()));
            emNode.appendChild(createElement(doc, "file_size", em.getFileSize()));
            if (em.getTags() != null && !em.getTags().isEmpty()) {
                Element tags = doc.createElement("tags");
                em.getTags().stream().forEach(tag -> {
                    Element t = doc.createElement("tag");
                    t.appendChild(doc.createTextNode(tag));
                    tags.appendChild(t);
                });
                emNode.appendChild(tags);
            }
            if (em.getUploadDate() != null) {
                emNode.appendChild(createElement(doc, "upload_date", String.valueOf(em.getUploadDate())));
            }
            emNode.appendChild(createDependencyList(doc, "requires_oxps", em.getRequiresOxps()));
            emNode.appendChild(createDependencyList(doc, "optional_oxps", em.getOptionalOxps()));
            emNode.appendChild(createDependencyList(doc, "conflict_oxps", em.getConflictOxps()));
            root.appendChild(emNode);
        });
        
        return doc;
    }
    
    /**
     * Writes the catalog as XML document.
     * The catalog is generated and written out using the identity transformation.
     * 
     * @param catalog the catalog to write
     * @param out the outputstream to write to
     * @throws ParserConfigurationException something went wrong
     * @throws TransformerConfigurationException something went wrong
     * @throws TransformerException something went wrong
     */
    private void writeXml(List<ExpansionManifest> catalog, OutputStream out) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        Document doc = generateXml(catalog);
        
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }

    private void writeHtml(List<ExpansionManifest> catalog, OutputStream out) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException, SAXException {
        Document doc = generateXml(catalog);
        
        URL stylesheet = getClass().getResource("/com/chaudhuri/ooliteaddonscanner2/templates/catalog_html.xslt");
        log.debug("stylesheet url: {}", stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(stylesheet.openStream()));
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }
    
    private void writePlist(List<ExpansionManifest> catalog, OutputStream out) throws ParserConfigurationException, TransformerConfigurationException, TransformerException, IOException {
        Document doc = generateXml(catalog);
        
        URL stylesheet = getClass().getResource("/com/chaudhuri/ooliteaddonscanner2/templates/catalog_plist.xslt");
        log.debug("stylesheet url: {}", stylesheet);
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(stylesheet.openStream()));
        transformer.transform(new DOMSource(doc), new StreamResult(out));
    }
}
