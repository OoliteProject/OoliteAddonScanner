/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.plist.ModelLexer;
import com.chaudhuri.plist.ModelParser;
import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import freemarker.template.TemplateException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

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

    /** Reads a file into a string, assuming the JVM's default charset encoding
     * and fixing linefeeds.
     * @see https://docs.oracle.com/en/java/javase/19/docs/api/java.base/java/nio/charset/Charset.html#defaultCharset()
     * 
     * @param in
     * @return 
     */
    static String readToString(InputStream in) {
        Scanner sc = new Scanner(in);
        
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            sb.append(sc.nextLine()).append("\n");
        }
        sc.close();
        
        return sb.toString();
    }
    
    private static void readExpansionsList(File data, Registry registry) throws IOException {
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
    
    private static void parseModel(InputStream data, String source) throws IOException {
        log.debug("parseModel({}, {})", data, source);
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
     * 
     * @param zin
     * @param ze
     * @return
     * @throws IOException 
     * 
     */
    private static InputStream getZipEntryStream(ZipInputStream zin) throws IOException {
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
     * Download oolite, unpack and scan for ships and equipment.
     * 
     * @param registry 
     */
    private static void readOolite(ExpansionCache cache, Registry registry) throws IOException, SAXException, ParserConfigurationException, RegistryException, TransformerException {
        log.debug("readOolite({})", registry); 
        
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
                    readShips(url+"!"+entry.getName(), getZipEntryStream(zin), registry, oxp);
                }
                if ("Oolite.app/Contents/Resources/Config/equipment.plist".equals(entry.getName())) {
                    log.debug("found {}", entry.getName());
                    readEquipment(url+"!"+entry.getName(), getZipEntryStream(zin), registry, oxp);
                }
            }
        }
    }
    
    private static void readEquipment(String url, InputStream in, Registry registry, Expansion oxp) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException {
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("Parsing {}", url);
            in.reset();
            List<Object> equipmentList = XMLPlistParser.parseList(in, null);
            oxp.addWarning("Found XML equipment list");
            log.info("Parsed {} ({} items of equipment)", oxp.getName(), equipmentList.size());
            registry.addEquipmentList(oxp, (List)equipmentList.get(0));
        } else {
            in.reset();
            PlistParser.ListContext lc = PlistParserUtil.parsePlistList(in, url);
            registry.addEquipmentList(oxp, lc);
        }
    }
    
    private static void readShips(String url, InputStream in, Registry registry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("Parsing {}", url);
            in.reset();

            Map<String, Object> shipList = XMLPlistParser.parseListOfMaps(in, null);
            log.info("Parsed {} ({} ships)", oxp.getName(), shipList.size());
            registry.addShipList(oxp, shipList);
        } else {
            in.reset();
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(in, url);
            registry.addShipList(oxp, dc);
        }
    }
    
    /**
     * 
     * @param cache
     * @param expansion
     * @return true when successful, false otherwise
     */
    private static void readShipModels(ExpansionCache cache, Expansion expansion) {
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(expansion.getDownloadUrl())));

            // parse all the models we can get, then distribute over ships
            ZipEntry zentry = null;
            while ((zentry = zin.getNextEntry()) != null) {
                if (zentry.getName().startsWith("Models/") && zentry.getName().length() > "Models/.dat".length() && zentry.getName().endsWith(".dat")) {
                    log.info("Found model {}!{}", expansion.getDownloadUrl(), zentry.getName());
                    
                    readModel(getZipEntryStream(zin), expansion, zentry.getName());
                }
            }

            // TODO: distribute models over ships. not yet implemented
        } catch (Exception e) {
            log.error("Incomplete Index: Could not read expansion {}", expansion, e);
            expansion.addWarning("Could not read expansion: "+e.getMessage());
        }
    }
    
    private static void readModel(InputStream in, Expansion expansion, String zname) {
        try {
            parseModel(in, expansion.getDownloadUrl() + "!" + zname);
        } catch (Exception e) {
            log.warn("Could not parse model {}!{}: {}", expansion.getDownloadUrl(), zname, e.getMessage());
            expansion.addWarning(String.format("Could not parse model %s!%s: %s", expansion.getDownloadUrl(), zname, e.getMessage()));
        }
    }
    
    /** Traverse the registry, find the ship data files and read them from the cache.
     * 
     */
    private static void readShipModels(ExpansionCache cache, Registry registry) {
        log.debug("readShipModels({}, {})", cache, registry);
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
    private static void readOxps(ExpansionCache cache, Registry registry) {
        log.debug("readOxps({}, {})", cache, registry);
        
        int i = 0;
        int total = registry.getExpansions().size();
        
        for(Expansion oxp: registry.getExpansions()) {
            i++;
            log.info("Reading expansions ({}/{})...", i, total);
            
            readOxp(cache, registry, oxp);
        }
    }
    
    private static void readOxp(ExpansionCache cache, Registry registry, Expansion oxp) {
        try {
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(cache.getPluginInputStream(oxp.getDownloadUrl())));
            ZipEntry zentry = null;
            while ((zentry = zin.getNextEntry()) != null) {
                readOxpEntry(zin, zentry, registry, oxp);
            }
        } catch (EOFException e) {
            log.warn("Incomplete plugin archive for {}", oxp.getDownloadUrl(), e);
            try {
                cache.invalidate(oxp.getDownloadUrl());
                log.warn("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", oxp.getDownloadUrl(), ex);
            }
            System.exit(1);
        } catch (ConnectException e) {
            String s = String.format("Could not download %s, %s: %s", oxp.getDownloadUrl(), e.getClass().getName(), e.getMessage());
            oxp.addWarning(s);
            log.error(s);
            try {
                cache.invalidate(oxp.getDownloadUrl());
                log.warn("Evicted from cache.");
            } catch (Exception ex) {
                log.error("Could not cleanup cache for {}", oxp.getDownloadUrl(), ex);
            }
        } catch (Exception e) {
            oxp.addWarning(String.format("Could not access: %s, %s: %s", oxp.getDownloadUrl(), e.getClass().getName(), e.getMessage()));
            log.error("Could not access plugin {}", oxp.getDownloadUrl(), e);
        }
    }
    
    private static void readOxpEntry(ZipInputStream zin, ZipEntry zentry, Registry registry, Expansion oxp) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException, OxpException {
        if ("Config/equipment.plist".equals(zentry.getName())) {
            log.trace("parsing equipment of {}", oxp.getDownloadUrl());
            InputStream in = getZipEntryStream(zin);
            String url = String.format("%s!%s", oxp.getDownloadUrl(), zentry.getName());
            readEquipment(url, in, registry, oxp);

        } else if("Config/shipdata.plist".equals(zentry.getName())) {
            try {
                log.trace("parsing shipdata of {}", oxp.getDownloadUrl());
                readShips(String.format("%s!%s", oxp.getDownloadUrl(), zentry.getName()), getZipEntryStream(zin), registry, oxp);
            } catch (Exception e) {
                oxp.addWarning(String.format("%s: %s at %s!%s", e.getClass().getName(), e.getMessage(), oxp.getDownloadUrl(), zentry.getName()));
            }

        } else if("manifest.plist".equals(zentry.getName())) {
            readManifest(zin, zentry, registry, oxp);
        } else if ("Config/script.js".equals(zentry.getName()) || (zentry.getName().startsWith(OXP_PATH_SCRIPTS) && zentry.getName().length()>OXP_PATH_SCRIPTS.length())) {
            String content = readToString(getZipEntryStream(zin));
            oxp.addScript(zentry.getName(), content);
        } else if ("Config/world-scripts.plist".equals(zentry.getName())) {
            readScript(zin, zentry, oxp);
        } else {
            String name = zentry.getName().toLowerCase();
            if(name.contains("read")) {
                log.trace("README {}!{}", oxp.getDownloadUrl(), zentry.getName());

                String r = readToString(getZipEntryStream(zin));                            
                oxp.addReadme(zentry.getName(), r);

            } else if (zentry.getName().startsWith("Config") || zentry.getName().startsWith("config")) {
                log.trace("skipping {}!{}", oxp.getDownloadUrl(), zentry.getName());
            } else {
                log.trace("skipping {}!{}", oxp.getDownloadUrl(), zentry.getName());
            }
        }
    }
    
    private static void readManifest(ZipInputStream zin, ZipEntry zentry, Registry registry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, TransformerException, OxpException {
        log.trace("parsing manifest from {}", oxp.getDownloadUrl());
        InputStream in = getZipEntryStream(zin);
        in.mark(10);

        Scanner sc = new Scanner(in);
        if (XML_HEADER.equals(sc.next())) {
            log.trace("XML content found in {}!{}", oxp.getDownloadUrl(), zentry.getName());
            in.reset();

            List<Object> manifest = XMLPlistParser.parseList(in, null);
            if (manifest.size() != 1) {
                throw new OxpException(String.format("Expected exactly one manifest, found %d", manifest.size()));
            }
            oxp.setManifest(registry.toManifest((Map<String, Object>)manifest.get(0)));
        } else {
            in.reset();
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(in, oxp.getDownloadUrl()+"!"+zentry.getName());
            oxp.setManifest(registry.toManifest(dc));
        }
    }
    
    private static void readScript(ZipInputStream zin, ZipEntry zentry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        InputStream in = getZipEntryStream(zin);
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
    
    private static void scanWikipages(Registry reg) {
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
    
    private static void printIndex(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        log.debug("printIndex(...)");
        registry.setProperty("ImplementationVendor", String.valueOf(Main.class.getPackage().getImplementationVendor()));
        registry.setProperty("ImplementationTitle", String.valueOf(Main.class.getPackage().getImplementationTitle()));
        registry.setProperty("ImplementationVersion", String.valueOf(Main.class.getPackage().getImplementationVersion()));
        templateEngine.process(registry, "index.ftlh", new File(outputdir, "index.html"));
        templateEngine.process(registry, "indexExpansionsByName.ftlh", new File(outputdir, "indexExpansionsByName.html"));
        templateEngine.process(registry, "indexEquipmentByName.ftlh", new File(outputdir, "indexEquipmentByName.html"));
        templateEngine.process(registry, "indexShipsByName.ftlh", new File(outputdir, "indexShipsByName.html"));
        templateEngine.process(registry, "indexAllByIdentifier.ftlh", new File(outputdir, "indexAllByIdentifier.html"));
        templateEngine.process(registry, "warnings.ftlh", new File(outputdir, "warnings.html"));
        templateEngine.process(registry, "style.ftlh", new File(outputdir, "style.css"));
    }
    
    private static void printExpansions(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        for (Expansion expansion: registry.getExpansions()) {
            templateEngine.process(expansion, "expansion.ftlh", new File(outputdir, "expansions/"+expansion.getIdentifier()+HTML_EXTENSION));
        }
    }
    
    private static void printEquipment(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        for (Equipment equipment: registry.getEquipment()) {
            templateEngine.process(equipment, "equipment.ftlh", new File(outputdir, "equipment/"+equipment.getIdentifier()+HTML_EXTENSION));
        }
    }
    
    private static void printShips(Registry registry, File outputdir, TemplateEngine templateEngine) throws IOException, TemplateException, TemplateEngineException {
        for (Ship ship: registry.getShips()) {
            templateEngine.process(ship, "ship.ftlh", new File(outputdir, "ships/"+ship.getIdentifier()+HTML_EXTENSION));
        }
    }
    
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
        options.addOption("u", "url", true, "URL for downloading the expansions list");
        CommandLine commandline = new DefaultParser().parse(options, args);
        
        String cachePath = commandline.getOptionValue("c", ExpansionCache.DEFAULT_CACHE_DIR.getAbsolutePath());
        File cacheDIR = new File(cachePath);
        
        String urlStr = commandline.getOptionValue("u", "http://addons.oolite.space/api/1.0/overview");
        String outputDirStr = commandline.getOptionValue("o", "target/OoliteExpansionIndex");
        
        // try to download from http://addons.oolite.org/api/1.0/overview
        File data = File.createTempFile("OoliteAddonScanner2", ".nsdata");
        URL u = new URL(urlStr);
        try (InputStream in = u.openStream(); OutputStream out = new FileOutputStream(data)) {
            in.transferTo(out);
        } catch (Exception e) {
            throw new OxpException("Could not download up to date expansions list from " + u, e);
        }
        
        log.info("Want to read {}", data.getAbsolutePath());

        Registry registry = new Registry();
        ExpansionCache cache = new ExpansionCache(cacheDIR);
        TemplateEngine templateEngine = new TemplateEngine();
        
        try {
            readOolite(cache, registry);
            
            readExpansionsList(data, registry);
            log.debug("Parsed {}", registry.getExpansions().size());
            
            readOxps(cache, registry);
            
            readShipModels(cache, registry);
            
            log.info("Parsed {} OXPs", registry.getExpansions().size());
            log.info("Parsed {} equipment", registry.getEquipment().size());
            log.info("Parsed {} ships", registry.getShips().size());
            
            // scan for wiki pages
            scanWikipages(registry);
            
            // scanning finished. Now verify...
            Verifier.verify(registry);
            
            File outputdir = new File(outputDirStr);
            outputdir.mkdirs();
            new File(outputdir, "equipment").mkdirs();
            new File(outputdir, "expansions").mkdirs();
            new File(outputdir, "ships").mkdirs();
            
            printIndex(registry, outputdir, templateEngine);
            printExpansions(registry, outputdir, templateEngine);
            printEquipment(registry, outputdir, templateEngine);
            printShips(registry, outputdir, templateEngine);
            templateEngine.process(registry, "wikiIindex.ftlh", new File(outputdir, "wiki.txt"));
            
            zipup(outputdir);
            
            System.exit(0);
        } catch (Exception e) {
            log.error("Could not process {}", data.getAbsolutePath(), e);
            System.exit(1);
        }
    }
}
