/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import static com.chaudhuri.ooliteaddonscanner2.Main.OOLITE_CORE;
import static com.chaudhuri.ooliteaddonscanner2.Main.OXP_PATH_SCRIPTS;
import static com.chaudhuri.ooliteaddonscanner2.Main.XML_HEADER;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.plist.ModelLexer;
import com.chaudhuri.plist.ModelParser;
import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Contains utility functions for handling the Oolite addons model.
 * 
 * @author hiran
 */
public class AddonsUtil {
    private static final Logger log = LogManager.getLogger();
    
    private static final String EXCEPTION_EXPANSION_MUST_NOT_BE_NULL = "expansion must not be null";
    private static final String EXCEPTION_REGISTRY_MUST_NOT_BE_NULL = "registry must not be null";

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
            throw new IllegalArgumentException("data must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        
        ThrowingErrorListener errorListener = new ThrowingErrorListener();

        CharStream charStream = CharStreams.fromPath(data.toPath());
        PlistLexer lexer = new PlistLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        PlistParser parser = new PlistParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        PlistParser.ListContext lc = parser.list();

        registry.addExpansions(lc);
    }
    
    /**
     * Reads a ship model and reports problems.
     * 
     * @param data the model data to read
     * @param source the source of the model data (such that it can be used in error messages)
     * @throws IOException something went wrong
     */
    public static void parseModel(InputStream data, String source) throws IOException {
        log.debug("parseModel({}, {})", data, source);
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
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

            parser.model();
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
            throw new IllegalArgumentException("zin must not be null");
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
    public static void readShips(String url, InputStream in, Registry registry, Expansion expansion) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        log.debug("readShips(...)");
        if (in == null) {
            throw new IllegalArgumentException("in must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("Parsing {}", url);
            in.reset();

            Map<String, Object> shipList = XMLPlistParser.parseListOfMaps(in, null);
            log.info("Parsed {} ({} ships)", expansion.getName(), shipList.size());
            registry.addShipList(expansion, shipList);
        } else {
            in.reset();
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(in, url);
            registry.addShipList(expansion, dc);
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
        if (in == null) {
            throw new IllegalArgumentException("in must not be null");
        }
        if (registry == null) {
            throw new IllegalArgumentException(EXCEPTION_REGISTRY_MUST_NOT_BE_NULL);
        }
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("Parsing {}", url);
            in.reset();
            List<Object> equipmentList = XMLPlistParser.parseList(in, null);
            expansion.addWarning("Found XML equipment list");
            log.info("Parsed {} ({} items of equipment)", expansion.getName(), equipmentList.size());
            registry.addEquipmentList(expansion, (List)equipmentList.get(0));
        } else {
            in.reset();
            PlistParser.ListContext lc = PlistParserUtil.parsePlistList(in, url);
            registry.addEquipmentList(expansion, lc);
        }
    }
    
    /** 
     * Download oolite, unpack and scan for ships and equipment.
     * 
     * @param registry 
     */
    public static void readOolite(ExpansionCache cache, Registry registry) throws IOException, SAXException, ParserConfigurationException, RegistryException, TransformerException {
        log.debug("readOolite({})", registry); 
        if (cache == null) {
            throw new IllegalArgumentException("cache must not be null");
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
            oxp.setTags(String.valueOf(manifest.get("target_commitish")) + " " + tag);
            
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
    }

    /**
     * 
     * @param cache
     * @param expansion
     * @return true when successful, false otherwise
     */
    public static void readShipModels(ExpansionCache cache, Expansion expansion) {
        log.debug("readShipModels(...)");
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
     * Reads a (ship) model from a given inputstream.
     * 
     * @param in the input stream to read
     * @param expansion the expansion to add warnings
     * @param zname the name of the file within the expansion
     */
    public static void readModel(InputStream in, Expansion expansion, String zname) {
        log.debug("readModel(...)");
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        try {
            AddonsUtil.parseModel(in, expansion.getDownloadUrl() + "!" + zname);
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
    
    /** reads all OXPs that the registry knows about. Which means it opens
     * all the zip files and parses files like manifest.plist, equipment.plist
     * or shipdata.plist. The result is stored back in the registry again.
     * 
     * @param cache
     * @param registry 
     */
    public static void readOxps(ExpansionCache cache, Registry registry) {
        log.debug("readOxps({}, {})", cache, registry);
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
     * @param oxp the OXP to report errors
     * @throws IOException something went wrong
     * @throws RegistryException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws OxpException something went wrong
     */
    public static void readOxpEntry(ZipInputStream zin, ZipEntry zentry, Registry registry, Expansion oxp) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException, OxpException {
        log.debug("readOxpEntry(...)");
        if (zentry == null) {
            throw new IllegalArgumentException("zentry must not be null");
        }
        
        if ("Config/equipment.plist".equals(zentry.getName())) {
            log.trace("parsing equipment of {}", oxp.getDownloadUrl());
            InputStream in = AddonsUtil.getZipEntryStream(zin);
            String url = String.format("%s!%s", oxp.getDownloadUrl(), zentry.getName());
            AddonsUtil.readEquipment(url, in, registry, oxp);

        } else if("Config/shipdata.plist".equals(zentry.getName())) {
            try {
                log.trace("parsing shipdata of {}", oxp.getDownloadUrl());
                AddonsUtil.readShips(String.format("%s!%s", oxp.getDownloadUrl(), zentry.getName()), AddonsUtil.getZipEntryStream(zin), registry, oxp);
            } catch (Exception e) {
                oxp.addWarning(String.format("%s: %s at %s!%s", e.getClass().getName(), e.getMessage(), oxp.getDownloadUrl(), zentry.getName()));
            }

        } else if("manifest.plist".equals(zentry.getName())) {
            readManifest(zin, zentry, registry, oxp);
        } else {
            readOxpEntry2(zin, zentry, oxp);
        }
    }

    private static void readOxpEntry2(ZipInputStream zin, ZipEntry zentry, Expansion oxp) throws IOException, SAXException, TransformerException, ParserConfigurationException {
        if ("Config/script.js".equals(zentry.getName()) || (zentry.getName().startsWith(OXP_PATH_SCRIPTS) && zentry.getName().length()>OXP_PATH_SCRIPTS.length())) {
            StringBuilder sb = new StringBuilder();
            try (StringBuilderWriter sbw = new StringBuilderWriter(sb)) {
                IOUtils.copy(AddonsUtil.getZipEntryStream(zin), sbw, Charset.defaultCharset());
            }
            oxp.addScript(zentry.getName(), sb.toString());
        } else if ("Config/world-scripts.plist".equals(zentry.getName())) {
            readScript(zin, zentry, oxp);
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
     * Reads an OXP manifest from the OXP.
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
        if (expansion == null) {
            throw new IllegalArgumentException(EXCEPTION_EXPANSION_MUST_NOT_BE_NULL);
        }
        
        log.trace("parsing manifest from {}", expansion.getDownloadUrl());
        
        InputStream in = AddonsUtil.getZipEntryStream(zin);
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("XML content found in {}!{}", expansion.getDownloadUrl(), zentry.getName());
            in.reset();

            List<Object> manifest = XMLPlistParser.parseList(in, null);
            if (manifest.size() != 1) {
                throw new OxpException(String.format("Expected exactly one manifest, found %d", manifest.size()));
            }
            expansion.setManifest(registry.toManifest((Map<String, Object>)manifest.get(0)));
        } else {
            in.reset();
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(in, expansion.getDownloadUrl()+"!"+zentry.getName());
            expansion.setManifest(registry.toManifest(dc));
        }
    }

    /**
     * Reads a script file from an OXP Zip file.
     * 
     * @param zin the OXP Zip Input Stream
     * @param zentry the entry to read from zin
     * @param oxp the OXP to add found data and report errors
     * @throws IOException something went wrong
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws TransformerException something went wrong
     */
    public static void readScript(ZipInputStream zin, ZipEntry zentry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        log.debug("readScript(...)");
        if (zin == null) {
            throw new IllegalArgumentException("zin must not be null");
        }
        
        InputStream in = AddonsUtil.getZipEntryStream(zin);
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("XML content found in {}!{}", oxp.getDownloadUrl(), zentry.getName());
            in.reset();
            List<Object> worldscripts = XMLPlistParser.parseList(in, null);
            List<Object> scriptlist = (List)worldscripts.get(0);
            for (Object worldscript: scriptlist) {
                oxp.addScript(String.valueOf(OXP_PATH_SCRIPTS + worldscript), "notYetParsed");
            }
        } else {
            in.reset();
            PlistParser.ListContext lc = PlistParserUtil.parsePlistList(in, oxp.getDownloadUrl()+"!"+zentry.getName());
            for (PlistParser.ValueContext vc: lc.value()) {
                oxp.addScript(OXP_PATH_SCRIPTS + vc.getText(), "notYetParsed");
            }
        }
    }
    
}
