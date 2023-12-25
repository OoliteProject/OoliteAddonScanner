/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.plist.XMLPlistParser;
import com.chaudhuri.ooliteaddonscanner2.plist.ThrowingErrorListener;
import static com.chaudhuri.ooliteaddonscanner2.Scanner.OOLITE_CORE;
import static com.chaudhuri.ooliteaddonscanner2.Scanner.OXP_PATH_SCRIPTS;
import static com.chaudhuri.ooliteaddonscanner2.Scanner.XML_HEADER;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Model;
import com.chaudhuri.ooliteaddonscanner2.plist.CountingErrorListener;
import com.chaudhuri.plist.ModelLexer;
import com.chaudhuri.plist.ModelParser;
import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Contains utility functions for handling the Oolite addons model.
 * 
 * @author hiran
 */
public class AddonsUtil {
    private static final Logger log = LogManager.getLogger();
    
    private static final String EXCEPTION_CACHE_MUST_NOT_BE_NULL = "cache must not be null";
    private static final String EXCEPTION_DATA_MUST_NOT_BE_NULL = "data must not be null";
    private static final String EXCEPTION_EXPANSION_MUST_NOT_BE_NULL = "expansion must not be null";
    private static final String EXCEPTION_REGISTRY_MUST_NOT_BE_NULL = "registry must not be null";
    private static final String EXCEPTION_ZENTRY_MUST_NOT_BE_NULL = "zentry must not be null";
    private static final String EXCEPTION_ZIN_MUST_NOT_BE_NULL = "zin must not be null";

    /**
     * Prevent instances being created.
     */
    private AddonsUtil() {
    }
    
    /**
     * Reads the list of expansions from a file and adds it to the registry.
     * 
     * @param data the file to read
     * @param registry the registry to store the data
     * @throws IOException something went wrong
     */
    public static void readExpansionsList(File data, Registry registry) throws IOException {
        log.debug("readExpansionsList(...)");
        if (data == null) {
            throw new IllegalArgumentException(EXCEPTION_DATA_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (!data.canRead()) {
            throw new NoSuchFileException(data.getName());
        }
        
        try {
            NSObject lc = PropertyListParser.parse(data);
            registry.addExpansions((NSArray)lc);
        } catch (Exception e) {
            throw new IOException("cannot parse " + data.toString(), e);
        }
    }

    /**
     * Reads the list of expansions from a file and adds it to the registry.
     * 
     * @param data the file to read
     * @param registry the registry to store the data
     * @param max maximum amount of expansions to read
     * @throws IOException something went wrong
     */
    public static void readExpansionsList(File data, Registry registry, int max) throws IOException {
        log.debug("readExpansionsList({}, {}, {})", data, registry, max);
        
        readExpansionsList(data, registry);
        
        while (registry.getExpansions().size() > max) {
            List<Expansion> toRemove = registry.getExpansions().subList(max, registry.getExpansions().size());
            log.debug("Removing {}", toRemove.size());
            registry.removeExpansions(toRemove);
        }
    }
    
    /**
     * Reads a ship model and reports problems.
     * 
     * @param data the model data to read
     * @param source the source of the model data (such that it can be used in error messages)
     * @throws IOException something went wrong
     */
    public static Model parseModel(InputStream data, String source) throws IOException {
        log.debug("parseModel({}, {})", data, source);
        if (data == null) {
            throw new IllegalArgumentException(EXCEPTION_DATA_MUST_NOT_BE_NULL);
        }
        
        try {
            ThrowingErrorListener errorListener = new ThrowingErrorListener();

            ReadableByteChannel channel = Channels.newChannel(data);
            CharStream charStream = CharStreams.fromChannel(channel, StandardCharsets.UTF_8, 4096, CodingErrorAction.REPLACE, source, -1);

            ModelLexer lexer = new ModelLexer(charStream);
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            ModelParser parser = new ModelParser(tokenStream);
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);

            ModelParser.ModelContext mc = parser.model();
            
            return new Model(
                    source,
                    Integer.parseInt(mc.header().nverts().NUMBER().getText()), 
                    Integer.parseInt(mc.header().nfaces().NUMBER().getText())
            );
        } catch (ParseCancellationException e) {
            throw new IOException("Could not parse "+source, e);
        }
    }

    /**
     * Copies a ZIP file entry into a memory buffer and returns it for reading.
     * This allows the file to be re-read if need be.
     * 
     * @param zin The zip inputstream to read from
     * @return the re-readable memory based InputStream
     * @throws IOException something went wrong
     * 
     */
    public static InputStream getZipEntryStream(ZipInputStream zin) throws IOException {
        log.debug("getZipEntryStream(...)");
        if (zin == null) {
            throw new IllegalArgumentException(EXCEPTION_ZIN_MUST_NOT_BE_NULL);
        }
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read = 0;
        while ( (read = zin.read(buffer))>=0) {
            bos.write(buffer, 0, read);
        }
        bos.flush();
        bos.close();
        
        return new ByteArrayInputStream(bos.toByteArray());
    }
    
    /**
     * Reads a list of ships from a given InputStream and adds it to the registry.
     * 
     * @param url the (human understandable) url the file came from
     * @param in the InputStream to read
     * @param registry the registry to store found data
     * @param expansion the expansion to add the found ships to
     * @throws IOException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     */
    public static void readShips(String url, InputStream in, Registry registry, Expansion expansion) throws IOException, ParserConfigurationException, SAXException, TransformerException, XPathExpressionException {
        log.debug("readShips(...)");
        if (url == null || url.equals("null")) {
            throw new IllegalArgumentException("url must not be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("in must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }

        NSDictionary dc = null;
        try {
            dc = (NSDictionary)PropertyListParser.parse(in);
        } catch (Exception e) {
            throw new IOException("could not parse " + url, e);
        }
        if (dc == null) {
            throw new IOException("Shiplist is empty");
        }

        try {
            // on highest level the checks are not meaningful.
            for (Map.Entry<String, NSObject> pt: dc.entrySet()) {
                if (pt instanceof NSDictionary) {
                    NSDictionary subDc = (NSDictionary)pt;
                    checkPlistKeys(subDc, expansion, shipRequiredKeys, shipAllowedKeys);
                }
            }
        } catch (Exception e) {
            throw new IOException("could not validate " + url, e);
        }

        try {
            registry.addShipList(expansion, dc);
        } catch (Exception e) {
            throw new IOException("could not digest " + url, e);
        }
    }
    
    /**
     * Reads a list of equipment from a given InputStream and adds it to the registry.
     * 
     * @param url the (human understandable) url the file came from
     * @param in the InputStream to read
     * @param registry the registry to store found data
     * @param expansion the expansion to add the found equipment to
     * @throws IOException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     */
    public static void readEquipment(String url, InputStream in, Registry registry, Expansion expansion) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException {
        log.debug("readEquipment(...)");
        if (url == null || url.equals("null")) {
            throw new IllegalArgumentException("url must not be null");
        }
        if (in == null) {
            throw new IllegalArgumentException("in must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }

        try {
            NSArray lc = (NSArray)PropertyListParser.parse(in);
            registry.addEquipmentList(expansion, lc);
        } catch (Exception e) {
            throw new IOException("cannot digest " + url, e);
        }
    }
    
    /** 
     * Download oolite, unpack and scan for ships and equipment.
     * 
     * @param registry 
     */
    public static void readOolite(ExpansionCache cache, Registry registry) throws IOException, SAXException, ParserConfigurationException, RegistryException, TransformerException, XPathExpressionException {
        log.debug("readOolite({})", registry); 
        if (cache == null) {
            throw new IllegalArgumentException(EXCEPTION_CACHE_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        
        String tag = "latest";

        Map<String, Object> manifest = cache.getOoliteManifest(tag);
        String url = cache.getOoliteDownloadUrl(manifest);
        try (InputStream olitezip = cache.getPluginInputStream(url); ZipInputStream zin = new ZipInputStream(olitezip)) {
            Expansion oxp = new Expansion();
            oxp.setDownloadUrl(url);
            oxp.setTitle(String.valueOf(manifest.get("name")));
            oxp.setIdentifier(OOLITE_CORE);
            oxp.setDescription(String.valueOf(manifest.get("body")));
            oxp.setCategory("vanilla");
            oxp.setAuthor(((Map<String, String>)manifest.get("author")).get("login"));
            oxp.setVersion(String.valueOf(manifest.get("tag_name")));
            oxp.setUploadDate(String.valueOf(manifest.get("published_at")));
            // TODO: Fix tags to contain something like String.valueOf(manifest.get("target_commitish")) + " " + tag
            oxp.setTags(new ArrayList<>());
            
            ExpansionManifest oxpManifest = new ExpansionManifest();
            oxpManifest.setDescription(oxp.getDescription());
            oxpManifest.setIdentifier(oxp.getIdentifier());
            oxpManifest.setTitle(oxp.getTitle());
            oxpManifest.setCategory(oxp.getCategory());
            oxpManifest.setAuthor(oxp.getAuthor());
            oxpManifest.setTags(oxp.getTags());
            oxpManifest.setVersion(oxp.getVersion());
            oxp.setManifest(oxpManifest);
            
            registry.addExpansion(oxp);
            
            ZipEntry entry;
            while ((entry= zin.getNextEntry()) != null) {
                if ("Oolite.app/Contents/Resources/Config/shipdata.plist".equals(entry.getName())) {
                    log.debug("found {}", entry.getName());
                    readShips(url+"!"+entry.getName(), AddonsUtil.getZipEntryStream(zin), registry, oxp);
                }
                if ("Oolite.app/Contents/Resources/Config/equipment.plist".equals(entry.getName())) {
                    log.debug("found {}", entry.getName());
                    readEquipment(url+"!"+entry.getName(), AddonsUtil.getZipEntryStream(zin), registry, oxp);
                }
            }
        }
        registry.setProperty("ooliteDownloadUrl", url);
    }

    /**
     * Loads the given expansion and parses all contained ship models.
     * Results are added to the expansion.
     * 
     * @param cache the cache to load from
     * @param expansion the expansionn to load
     * @return true when successful, false otherwise
     */
    public static void readShipModels(ExpansionCache cache, Expansion expansion) {
        log.debug("readShipModels(...)");
        if (cache == null) {
            throw new IllegalArgumentException(EXCEPTION_CACHE_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(expansion.getDownloadUrl())));

            // parse all the models we can get, then distribute over ships
            ZipEntry zentry = null;
            while ((zentry = zin.getNextEntry()) != null) {
                if (zentry.getName().startsWith("Models/") && zentry.getName().length() > "Models/.dat".length() && zentry.getName().endsWith(".dat")) {
                    log.info("Found model {}!{}", expansion.getDownloadUrl(), zentry.getName());
                    
                    readModel(AddonsUtil.getZipEntryStream(zin), expansion, zentry.getName());
                }
            }

            // TODO: distribute models over ships. not yet implemented
        } catch (Exception e) {
            log.error("Incomplete Index: Could not read expansion {}", expansion, e);
            expansion.addWarning("Could not read expansion: "+e.getMessage());
        }
    }
    
    /**
     * Reads a (ship) model from a given inputstream and reports problems
     * as warnings in the expansion.
     * 
     * @param data the input stream to read
     * @param expansion the expansion to add warnings
     * @param zname the name of the file within the expansion
     */
    public static void readModel(InputStream data, Expansion expansion, String zname) {
        log.debug("readModel(...)");
        if (data == null) {
            throw new IllegalArgumentException(EXCEPTION_DATA_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        try {
            Model model = AddonsUtil.parseModel(data, expansion.getDownloadUrl() + "!" + zname);
            expansion.addModel(zname, model);
        } catch (Exception e) {
            log.warn("Could not parse model {}!{}: {}", expansion.getDownloadUrl(), zname, e.getMessage());
            expansion.addWarning(String.format("Could not parse model %s!%s: %s", expansion.getDownloadUrl(), zname, e.getMessage()));
        }
    }
    
    /** 
     * Traverse the registry, find the ship data files and read them from the cache.
     */
    public static void readShipModels(ExpansionCache cache, Registry registry) {
        log.debug("readShipModels({}, {})", cache, registry);
        if (cache == null) {
            throw new IllegalArgumentException(EXCEPTION_CACHE_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        
        int countSuccess = 0;
        int countFailure = 0;

        for (Expansion expansion: registry.getExpansions()) {
            readShipModels(cache, expansion);
        }
        
        log.info("Parsed {} models successfully and failed on {} models", countSuccess, countFailure);
    }
    
    /** 
     * Reads all OXPs that the registry knows about. Which means it opens
     * all the zip files and parses files like manifest.plist, equipment.plist
     * or shipdata.plist. The result is stored back in the registry again.
     * 
     * @param cache
     * @param registry 
     */
    public static void readOxps(ExpansionCache cache, Registry registry) {
        log.debug("readOxps({}, {})", cache, registry);
        if (cache == null) {
            throw new IllegalArgumentException(EXCEPTION_CACHE_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        
        int i = 0;
        int total = registry.getExpansions().size();
        
        for(Expansion oxp: registry.getExpansions()) {
            i++;
            log.info("Reading expansions ({}/{})...", i, total);
            
            readOxp(cache, registry, oxp);
        }
    }

    /**
     * Reads an expansion and adds it's items to the registry.
     * 
     * @param cache the cache to retrieve the file from
     * @param registry the registry to add the found data
     * @param expansion the oxp to report errors
     */
    public static void readOxp(ExpansionCache cache, Registry registry, Expansion expansion) {
        log.debug("readOxp(...)");
        if (cache == null) {
            throw new IllegalArgumentException(EXCEPTION_CACHE_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(expansion.getDownloadUrl())));
            ZipEntry zentry = null;
            while ((zentry = zin.getNextEntry()) != null) {
                readOxpEntry(zin, zentry, registry, expansion);
            }
        } catch (EOFException e) {
            log.warn("Incomplete plugin archive for {}", expansion.getDownloadUrl(), e);
            try {
                cache.invalidate(expansion.getDownloadUrl());
                log.warn("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", expansion.getDownloadUrl(), ex);
            }
            System.exit(1);
        } catch (ConnectException e) {
            String s = String.format("Could not download %s, %s: %s", expansion.getDownloadUrl(), e.getClass().getName(), e.getMessage());
            expansion.addWarning(s);
            log.error(s);
            try {
                cache.invalidate(expansion.getDownloadUrl());
                log.warn("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", expansion.getDownloadUrl(), ex);
            }
        } catch (Exception e) {
            expansion.addWarning(String.format("Could not access: %s, %s: %s", expansion.getDownloadUrl(), e.getClass().getName(), e.getMessage()));
            log.error("Could not access plugin {}", expansion.getDownloadUrl(), e);
        }
    }
    
    /**
     * Reads the file content of an OXP zip file and adds the found data to
     * the registry.
     * 
     * @param zin the OXP Zip InputStream
     * @param zentry the entry to read
     * @param registry the registry to add the found data
     * @param expansion the OXP to report errors
     * @throws IOException something went wrong
     * @throws RegistryException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws OxpException something went wrong
     */
    public static void readOxpEntry(ZipInputStream zin, ZipEntry zentry, Registry registry, Expansion expansion) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException, OxpException, XPathExpressionException {
        log.debug("readOxpEntry(...)");
        if (zin == null) {
            throw new IllegalArgumentException(EXCEPTION_ZIN_MUST_NOT_BE_NULL);
        }
        if (zentry == null) {
            throw new IllegalArgumentException(EXCEPTION_ZENTRY_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        if ("Config/equipment.plist".equals(zentry.getName())) {
            log.trace("parsing equipment of {}", expansion.getDownloadUrl());
            InputStream in = AddonsUtil.getZipEntryStream(zin);
            String url = String.format("%s!%s", expansion.getDownloadUrl(), zentry.getName());
            AddonsUtil.readEquipment(url, in, registry, expansion);

        } else if("Config/shipdata.plist".equals(zentry.getName())) {
            try {
                log.trace("parsing shipdata of {}", expansion.getDownloadUrl());
                AddonsUtil.readShips(String.format("%s!%s", expansion.getDownloadUrl(), zentry.getName()), AddonsUtil.getZipEntryStream(zin), registry, expansion);
            } catch (Exception e) {
                expansion.addWarning(String.format("%s: %s at %s!%s", e.getClass().getName(), e.getMessage(), expansion.getDownloadUrl(), zentry.getName()));
            }

        } else if("manifest.plist".equals(zentry.getName())) {
            readManifest(zin, zentry, registry, expansion);
        } else {
            readOxpEntry2(zin, zentry, expansion);
        }
    }

    private static void readOxpEntry2(ZipInputStream zin, ZipEntry zentry, Expansion oxp) throws IOException, SAXException, TransformerException, ParserConfigurationException, XPathExpressionException {
        if ("Config/script.js".equals(zentry.getName()) || (zentry.getName().startsWith(OXP_PATH_SCRIPTS) && zentry.getName().length()>OXP_PATH_SCRIPTS.length())) {
            StringBuilder sb = new StringBuilder();
            try (StringBuilderWriter sbw = new StringBuilderWriter(sb)) {
                IOUtils.copy(AddonsUtil.getZipEntryStream(zin), sbw, Charset.defaultCharset());
            }
            oxp.addScript(zentry.getName(), sb.toString());
        } else if ("Config/world-scripts.plist".equals(zentry.getName())) {
            readScript(zin, zentry, oxp);
        } else if (zentry.getName().endsWith(".plist")) {
            checkPlist(zin, zentry, oxp);
        } else {
            String name = zentry.getName().toLowerCase();
            if(name.contains("read")) {
                log.trace("README {}!{}", oxp.getDownloadUrl(), zentry.getName());

                StringBuilder sb = new StringBuilder();
                try (StringBuilderWriter sbw = new StringBuilderWriter(sb)) {
                    IOUtils.copy(AddonsUtil.getZipEntryStream(zin), sbw, Charset.defaultCharset());
                }
                oxp.addReadme(zentry.getName(), sb.toString());

            } else if (zentry.getName().startsWith("Config") || zentry.getName().startsWith("config")) {
                log.trace("skipping {}!{}", oxp.getDownloadUrl(), zentry.getName());
            } else {
                log.trace("skipping {}!{}", oxp.getDownloadUrl(), zentry.getName());
            }
        }
    }
    
    /**
     * Reads an OXP manifest from the zip entry.The resulting manifest is set
     * in the expansion.
     * 
     * @param zin the OXP Zip Inputstream to read
     * @param zentry the zip entry to read from zin
     * @param registry the registry to add the found data
     * @param expansion the OXP to report errors
     * @throws IOException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     * @throws OxpException something went wrong
     */
    public static void readManifest(ZipInputStream zin, ZipEntry zentry, Registry registry, Expansion expansion) throws IOException, ParserConfigurationException, SAXException, TransformerException, OxpException {
        log.debug("readManifest(...)");
        if (zin == null) {
            throw new IllegalArgumentException(EXCEPTION_ZIN_MUST_NOT_BE_NULL);
        }
        if (zentry == null) {
            throw new IllegalArgumentException(EXCEPTION_ZENTRY_MUST_NOT_BE_NULL);
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        log.trace("parsing manifest from {}", expansion.getDownloadUrl());
        
        InputStream in = AddonsUtil.getZipEntryStream(zin);

        try {
            NSDictionary dc = (NSDictionary)PropertyListParser.parse(in);
            expansion.setManifest(registry.toManifest(dc));
        } catch (Exception e) {
            throw new IOException("cannot digest " + expansion.getDownloadUrl()+"!"+zentry.getName(), e);
        }
    }

    /**
     * Reads a script file from an OXP Zip file and adds it to the expansion.
     * 
     * @param zin the OXP Zip Input Stream
     * @param zentry the entry to read from zin
     * @param expansion the OXP to add found data and report errors
     * @throws IOException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     */
    public static void readScript(ZipInputStream zin, ZipEntry zentry, Expansion expansion) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        log.debug("readScript(...)");
        if (zin == null) {
            throw new IllegalArgumentException(EXCEPTION_ZIN_MUST_NOT_BE_NULL);
        }
        if (zentry == null) {
            throw new IllegalArgumentException(EXCEPTION_ZENTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        InputStream in = AddonsUtil.getZipEntryStream(zin);

        try {
            NSArray lc = (NSArray)PropertyListParser.parse(in);
            for (NSObject vc: lc.getArray()) {
                expansion.addScript(OXP_PATH_SCRIPTS + vc.toString(), "notYetParsed");
            }
        } catch (Exception e) {
            throw new IOException("Cannot digest " + expansion.getDownloadUrl()+"!"+zentry.getName(), e);
        }
    }
    
    /**
     * Performs something.
     * 
     * @param zos
     * @param file
     * @param root needed to calculate relative paths
     * @throws IOException 
     */
    private static void zipupInternal(ZipOutputStream zos, File file, File root) throws IOException {
        log.debug("zipup_internal({}, {}, {})", zos, file, root);
        if (root == null) {
            throw new IllegalArgumentException("root must not be null");
        }
        
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
    
    /**
     *  ZIPs up a file/directory and returns the archive.
     * The archive will be created as sibling to the file/directory.
     * 
     * @param directory the file/directory to zip up
     * @return the archive file
     */
    public static File zipup(File directory) throws FileNotFoundException {
        if (directory == null) {
            throw new IllegalArgumentException("directory must not be null");
        }
        if (!directory.exists()) {
            throw new FileNotFoundException(directory.getAbsolutePath());
        }
        
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        File zipfile = new File(directory.getParentFile(), directory.getName()+"-"+sdf.format(d)+".zip");
        log.info("Zip {} -> {}", directory, zipfile);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipfile))) {
            File parent = directory.getParentFile();
            if (parent == null) {
                parent = directory;
            }
            zipupInternal(zos, directory, parent);
        } catch(IOException | IllegalArgumentException e) {
            log.error("Could not zip {} to {}", directory.getAbsolutePath(), zipfile.getAbsolutePath(), e);
        }
        return zipfile;
    }
    
    /**
     * Parses an expansion's plist and adds warnings to the expansion.
     * 
     * @param zin
     * @param zentry
     * @param oxp 
     */
    static void checkPlist(ZipInputStream zin, ZipEntry zentry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("checkPlist({}, {}, {})", zin, zentry, oxp);
        
        checkPlist(getZipEntryStream(zin), zentry.getName(), oxp);
    }
    
    /**
     * Parses an expansion's plist and adds warnings to the expansion.
     * 
     * @param in
     * @param entry
     * @param oxp 
     */
    static void checkPlist(InputStream in, String entry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        log.debug("checkPlist({}, {}, {})", in, entry, oxp);
        
        if (in == null) {
            throw new IllegalArgumentException("in must not be null");
        }
        if (oxp == null) {
            throw new IllegalArgumentException("oxp must not be null");
        }
        
        try {
            CountingErrorListener cel = new CountingErrorListener(entry);
            NSObject lc = PropertyListParser.parse(in);
            // todo: parse errors should end up as warnings in the oxp?

            // todo: check if keys can be trimmed
            if (lc instanceof NSDictionary) {
                checkPlistKeys((NSDictionary)lc, oxp, null, null);
            }
        } catch (Exception e) {
            throw new IOException("cannot digest " + oxp.getDownloadUrl()+"!"+entry, e);
        }
    }
    
//    /**
//     * Check dictionary keys for extraenous whitespace.
//     * Returns findings as warnings in expansion.
//     * 
//     * @param pc the parsecontext to check
//     * @param oxp the oxp to return warnings
//     */
//    private static void checkPlistKeys(ParserRuleContext prc, Expansion oxp) {
//        for (int i=0; i< prc.getChildCount(); i++) {
//            ParseTree pt = prc.getChild(i);
//            checkPlistKeys(pt, oxp);
//        }
//    }
    
//    /**
//     * Check dictionary keys for extraenous whitespace.
//     * Returns findings as warnings in expansion.
//     * 
//     * @param pc the parsecontext to check
//     * @param oxp the oxp to return warnings
//     */
//    private static void checkPlistKeys(ParseTree pt, Expansion oxp) {
//        if (pt instanceof PlistParser.KeyvaluepairContext) {
//            PlistParser.KeyvaluepairContext kvpc = (PlistParser.KeyvaluepairContext)pt;
//            Token keyToken = kvpc.getStart();
//            String key = keyToken.getText();
//            if (!key.equals(key.trim())) {
//                oxp.addWarning(String.format("Extreanous whitespace on key '%s'", key));
//            }
//        }
//        
//        for (int i=0; i< pt.getChildCount(); i++) {
//            ParseTree pt2 = pt.getChild(i);
//            checkPlistKeys(pt2, oxp);
//        }
//    }
    
//    /**
//     * Check dictionary keys for extraenous whitespace.
//     * Returns findings as warnings in expansion.
//     * 
//     * @param node the DOM node to check
//     * @param oxp the oxp to return warnings
//     */
//    private static void checkPlistKeys(Node node, Expansion oxp) throws XPathExpressionException {
//        XPath xpath = XPathFactory.newDefaultInstance().newXPath();
//        NodeList nl = (NodeList)xpath.evaluate("//key", node, XPathConstants.NODESET);
//        for (int i=0; i<nl.getLength();i++) {
//            Node keyNode = nl.item(i);
//            String key = keyNode.getTextContent();
//            if (!key.equals(key.trim())) {
//                oxp.addWarning(String.format("Extreanous whitespace on key '%s'", key));
//            }
//        }
//    }
    
    private static List<String> manifestRequiredKeys = Arrays.asList("manifest1", "manifest2", "manifest3");
    private static List<String> manifestAllowedKeys = Arrays.asList("manifest1", "manifest2", "manifest3");
    
    private static List<String> shipRequiredKeys = Arrays.asList("roles");
    private static List<String> shipAllowedKeys = Arrays.asList("aft_eject_position", 
            "ai_type", "auto_ai", "cargo_type", "energy_recharge_rate", 
            "exhaust", "forward_weapon_type", "fuel", "has_ecm", "has_escape_pod",
            "has_scoop", "likely_cargo", "max_cargo", "max_energy", "max_flight_pitch",
            "max_flight_roll", "max_flight_speed", "missile_launch_position",
            "missiles", "model", "name", "roles", "thrust", "weapon_energy",
            "weapon_position_aft", "weapon_position_forward", "weapon_position_port",
            "weapon_position_starboard"
    );

    /**
     * Check dictionary keys for extraenous whitespace.
     * Returns findings as warnings in expansion.
     * 
     * @param pc the parsecontext to check
     * @param oxp the oxp to return warnings
     */
    private static void checkPlistKeys(NSDictionary dict, Expansion oxp, List<String> requiredKeys, List<String> allowedKeys) {
        for (Map.Entry<String, NSObject> pt: dict.entrySet()) {
            String key = pt.getKey();
            if (!key.equals(key.trim())) {
                oxp.addWarning(String.format("Extreanous whitespace on key '%s'", key));
            }
            if (allowedKeys != null && !allowedKeys.contains(key)) {
                oxp.addWarning(String.format("Unknown key '%s'", key));
            }
            
            if (requiredKeys != null) {
                requiredKeys.remove(key);
            }
        }

        /* todo: Is this meant to be recursive?
        
        for (int i=0; i< pt.getChildCount(); i++) {
            ParseTree pt2 = pt.getChild(i);
            checkPlistKeys(pt2, oxp, requiredKeys, allowedKeys);
        }
        */
    }
    
    /**
     * Check dictionary keys for extraenous whitespace.
     * Returns findings as warnings in expansion.
     * 
     * @param node the DOM node to check
     * @param oxp the oxp to return warnings
     */
    private static void checkPlistKeys(Node node, Expansion oxp, List<String> requiredKeys, List<String> allowedKeys) throws XPathExpressionException {
        List<String> myRequiredKeys = new ArrayList<>();
        if (requiredKeys != null) {
            myRequiredKeys.addAll(requiredKeys);
        }
        
        XPath xpath = XPathFactory.newDefaultInstance().newXPath();
        NodeList nl = (NodeList)xpath.evaluate("//key", node, XPathConstants.NODESET);
        for (int i=0; i<nl.getLength();i++) {
            Node keyNode = nl.item(i);
            String key = keyNode.getTextContent();
            if (!key.equals(key.trim())) {
                oxp.addWarning(String.format("Extreanous whitespace on key '%s'", key));
            }
            if (allowedKeys != null && !allowedKeys.contains(key)) {
                oxp.addWarning(String.format("Unknown key '%s'", key));
            }
            
            myRequiredKeys.remove(key);
        }
        
        if (!myRequiredKeys.isEmpty()) {
            oxp.addWarning("Missing keys " + String.valueOf(myRequiredKeys));
        }
    }
}
