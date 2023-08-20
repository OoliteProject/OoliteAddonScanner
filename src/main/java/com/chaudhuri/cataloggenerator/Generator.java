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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Reads a list of URLs and writes a catalog file.
 * 
 * @author hiran
 */
public class Generator implements Runnable {
    private static final Logger log = LogManager.getLogger();

    private Path inputPath;
    private Path outputPath;
    private Path tempDir;
    private Path cacheDIR = ExpansionCache.DEFAULT_CACHE_DIR.toPath();
    
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

    public Path getInputPath() {
        return inputPath;
    }

    public void setInputPath(Path inputPath) {
        this.inputPath = inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

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
    
    public void init() {
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
            return em;
        } catch (IOException e) {
            log.error("Could not access plugin {}", urlString, e);
            return null;
        }
    }

    
    ExpansionManifest getManifestFromOXZ(ZipInputStream zin, String urlString) {
        log.warn("getManifestFromOXZ({})", zin);
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();

        try {
            ZipEntry zentry = null;
            
            while ((zentry = zin.getNextEntry()) != null) {
                AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
            }
        } catch (EOFException e) {
            log.warn("Incomplete plugin archive for {}", urlString, e);
            try {
                cache.invalidate(urlString);
                log.warn("Evicted from cache.");
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
                log.warn("Evicted from cache.");
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

    @Override
    public void run() {
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
        
        List<ExpansionManifest> catalog = null;
        try {
            catalog = Files.lines(inputPath)
                    .filter(e -> !e.startsWith("#"))
                    .filter(e -> !e.isBlank())
                    .map(e -> getManifestFromUrl(e))
                    .collect(Collectors.toList());
            
            log.warn("Found {} manifests", catalog.size());
        } catch (IOException e) {
            log.error("Could not read input", e);
            return;
        }

        final List<ExpansionManifest> fCatalog = catalog;
        Arrays.asList(outputFormat.split(",")).stream()
            .forEach(format -> {
                try {
                    Files.createDirectories(outputPath.getParent());
                    
                    Path myOutputPath = outputPath;
                    if (!myOutputPath.toString().endsWith("." + format)) {
                        myOutputPath = outputPath.resolveSibling(outputPath.getFileName() + "." + format);
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
                } catch (Exception ex) {
                    log.error("cannot write {} output", format, ex);
                }
            });
    }

    private void writeJson(List<ExpansionManifest> catalog, OutputStream out) throws ParserConfigurationException, TransformerConfigurationException, TransformerException {
        new Genson().serialize(catalog, out);
    }

    private Element createElement(Document doc, String name, String content) {
        Element result = doc.createElement(name);
        result.appendChild(doc.createTextNode(content));
        return result;
    }
    
    private Document generateXml(List<ExpansionManifest> catalog) throws ParserConfigurationException {
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
            emNode.appendChild(createElement(doc, "requires_oxps", String.valueOf(em.getRequiresOxps())));
            emNode.appendChild(createElement(doc, "optional_oxps", String.valueOf(em.getOptionalOxps())));
            emNode.appendChild(createElement(doc, "tags", em.getTags()));
            emNode.appendChild(createElement(doc, "conflict_oxps", String.valueOf(em.getConflictOxps())));
            root.appendChild(emNode);
        });
        
        return doc;
    }
    
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
