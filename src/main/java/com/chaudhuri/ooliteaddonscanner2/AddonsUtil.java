/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import static com.chaudhuri.ooliteaddonscanner2.Main.OOLITE_CORE;
import static com.chaudhuri.ooliteaddonscanner2.Main.XML_HEADER;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.plist.ModelLexer;
import com.chaudhuri.plist.ModelParser;
import com.chaudhuri.plist.PlistLexer;
import com.chaudhuri.plist.PlistParser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
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

    /**
     * Prevent instances being created.
     */
    private AddonsUtil() {
    }
    
    public static void readExpansionsList(File data, Registry registry) throws IOException {
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
    
    public static void parseModel(InputStream data, String source) throws IOException {
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
    public static InputStream getZipEntryStream(ZipInputStream zin) throws IOException {
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
    
    public static void readShips(String url, InputStream in, Registry registry, Expansion oxp) throws IOException, ParserConfigurationException, SAXException, TransformerException {
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
    
    public static void readEquipment(String url, InputStream in, Registry registry, Expansion oxp) throws IOException, RegistryException, SAXException, TransformerException, ParserConfigurationException {
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
    
    /** 
     * Download oolite, unpack and scan for ships and equipment.
     * 
     * @param registry 
     */
    public static void readOolite(ExpansionCache cache, Registry registry) throws IOException, SAXException, ParserConfigurationException, RegistryException, TransformerException {
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
                    readShips(url+"!"+entry.getName(), AddonsUtil.getZipEntryStream(zin), registry, oxp);
                }
                if ("Oolite.app/Contents/Resources/Config/equipment.plist".equals(entry.getName())) {
                    log.debug("found {}", entry.getName());
                    readEquipment(url+"!"+entry.getName(), AddonsUtil.getZipEntryStream(zin), registry, oxp);
                }
            }
        }
    }
}
