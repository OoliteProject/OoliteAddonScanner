/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hiran
 */
public class AddonsUtilTest {
    private static final Logger log = LogManager.getLogger();
    
    private static File tempCacheDir;
    
    public AddonsUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        log.debug("setUpClass()");
        
        tempCacheDir = new File("target/testCacheDir");
        tempCacheDir.mkdirs();
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of readExpansionsList method, of class AddonsUtil.
     */
    @Test
    public void testReadExpansionsList() throws Exception {
        log.info("readExpansionsList");

        try {
            AddonsUtil.readExpansionsList(null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("data must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readExpansionsList method, of class AddonsUtil.
     */
    @Test
    public void testReadExpansionsList2() throws Exception {
        log.info("readExpansionsList2");

        File data = new File("nonexistent_file");
        try {
            AddonsUtil.readExpansionsList(data, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readExpansionsList method, of class AddonsUtil.
     */
    @Test
    public void testReadExpansionsList3() throws Exception {
        log.info("readExpansionsList3");

        File data = new File("nonexistent_file");
        Registry registry = new Registry();
        try {
            AddonsUtil.readExpansionsList(data, registry);
            fail("expected exception");
        } catch (NoSuchFileException e) {
            assertEquals("nonexistent_file", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readExpansionsList method, of class AddonsUtil.
     */
    @Test
    public void testReadExpansionsList4() throws Exception {
        log.info("readExpansionsList4");

        File data = new File("src/test/data/empty_file");
        Registry registry = new Registry();
        try {
            AddonsUtil.readExpansionsList(data, registry);
            fail("expected exception");
        } catch (ParseCancellationException e) {
            assertEquals("line 1:0 [@0,0:-1='<EOF>',<-1>,1:0] mismatched input '<EOF>' expecting '('", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readExpansionsList method, of class AddonsUtil.
     */
    @Test
    public void testReadExpansionsList5() throws Exception {
        log.info("readExpansionsList5");

        File data = new File("src/test/resources/AddonsUtilTest/ExpansionManagerList.plist");
        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        AddonsUtil.readExpansionsList(data, registry);
        assertEquals(743, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
    }

    /**
     * Test of parseModel method, of class AddonsUtil.
     */
    @Test
    public void testParseModel() throws Exception {
        log.info("parseModel");
        InputStream data = null;
        String source = "";
        try {
            AddonsUtil.parseModel(data, source);
            fail("expected exception");
        } catch(IllegalArgumentException e) {
            assertEquals("data must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of parseModel method, of class AddonsUtil.
     */
    @Test
    public void testParseModel2() throws Exception {
        log.info("parseModel2");
        InputStream data = new FileInputStream("src/test/data/empty_file");
        String source = "";
        try {
            AddonsUtil.parseModel(data, source);
            fail("expected exception");
        } catch(IOException e) {
            assertEquals("Could not parse ", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of parseModel method, of class AddonsUtil.
     */
    @Test
    public void testParseModel3() throws Exception {
        log.info("parseModel3");
        InputStream data = new FileInputStream("src/test/resources/AddonsUtilTest/icourier.dat");
        String source = "";
        AddonsUtil.parseModel(data, source);
        assertTrue(true, "we did not reach here. Why?");
    }

    /**
     * Test of getZipEntryStream method, of class AddonsUtil.
     */
    @Test
    public void testGetZipEntryStream() throws Exception {
        log.info("getZipEntryStream");
        ZipInputStream zin = null;
        try {
            AddonsUtil.getZipEntryStream(zin);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("zin must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of getZipEntryStream method, of class AddonsUtil.
     */
    @Test
    public void testGetZipEntryStream2() throws Exception {
        log.info("getZipEntryStream2");
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/empty_file"));
        InputStream result = AddonsUtil.getZipEntryStream(zin);
        assertEquals(0, result.available());
    }

    /**
     * Test of getZipEntryStream method, of class AddonsUtil.
     */
    @Test
    public void testGetZipEntryStream3() throws Exception {
        log.info("getZipEntryStream3");
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/resources/AddonsUtilTest/icourier.zip"));
        ZipEntry entry = zin.getNextEntry();
        InputStream result = AddonsUtil.getZipEntryStream(zin);
        assertEquals(27245, result.available());
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips() throws Exception {
        log.info("readShips");
        String url = "";
        InputStream in = null;
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readShips(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("in must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips2() throws Exception {
        log.info("readShips2");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readShips(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips3() throws Exception {
        log.info("readShips3");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = new Registry();
        Expansion oxp = null;
        try {
            AddonsUtil.readShips(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips4() throws Exception {
        log.info("readShips4");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();
        try {
            AddonsUtil.readShips(url, in, registry, oxp);
            fail("expected exception");
        } catch (NoSuchElementException e) {
            assertEquals(null, e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips5() throws Exception {
        log.info("readShips5");
        String url = "";
        InputStream in = new FileInputStream("src/test/resources/AddonsUtilTest/ship1.plist");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();

        assertEquals(0, registry.getShips().size());
        AddonsUtil.readShips(url, new BufferedInputStream(in), registry, oxp);
        assertEquals(3, registry.getShips().size());
    }

    /**
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips6() throws Exception {
        log.info("readShips6");
        String url = "";
        InputStream in = new FileInputStream("src/test/resources/AddonsUtilTest/ship1.xml");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();

        assertEquals(0, registry.getShips().size());
        AddonsUtil.readShips(url, new BufferedInputStream(in), registry, oxp);
        assertEquals(3, registry.getShips().size());
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment() throws Exception {
        log.info("readEquipment");
        String url = "";
        InputStream in = null;
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readEquipment(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("in must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment2() throws Exception {
        log.info("readEquipment2");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readEquipment(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment3() throws Exception {
        log.info("readEquipment3");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = new Registry();
        Expansion oxp = null;
        try {
            AddonsUtil.readEquipment(url, in, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment4() throws Exception {
        log.info("readEquipment4");
        String url = "";
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();
        try {
            AddonsUtil.readEquipment(url, in, registry, oxp);
            fail("expected exception");
        } catch (NoSuchElementException e) {
            assertEquals(null, e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment5() throws Exception {
        log.info("readEquipment5");
        String url = "";
        InputStream in = new FileInputStream("src/test/resources/AddonsUtilTest/equipment1.xml");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();
        
        assertEquals(0, registry.getEquipment().size());
        AddonsUtil.readEquipment(url, new BufferedInputStream(in), registry, oxp);
        assertEquals(3, registry.getEquipment().size());
    }

    /**
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment6() throws Exception {
        log.info("readEquipment6");
        String url = "";
        InputStream in = new FileInputStream("src/test/resources/AddonsUtilTest/equipment1.plist");
        Registry registry = new Registry();
        Expansion oxp = new Expansion();
        
        assertEquals(0, registry.getEquipment().size());
        AddonsUtil.readEquipment(url, new BufferedInputStream(in), registry, oxp);
        assertEquals(3, registry.getEquipment().size());
    }

    /**
     * Test of readOolite method, of class AddonsUtil.
     */
    @Test
    public void testReadOolite() throws Exception {
        log.info("readOolite");
        ExpansionCache cache = null;
        Registry registry = null;
        try {
            AddonsUtil.readOolite(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("cache must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readOolite method, of class AddonsUtil.
     */
    @Test
    public void testReadOolite2() throws Exception {
        log.info("readOolite2");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        Registry registry = null;
        
        try {
            AddonsUtil.readOolite(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readOolite method, of class AddonsUtil.
     */
    @Test
    public void testReadOolite3() throws Exception {
        log.info("readOolite3");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        cache.setBaseUrl(new File("src/test/data/").toURI().toURL().toString());

        Registry registry = new Registry();

        assertEquals(0, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        AddonsUtil.readOolite(cache, registry);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(41, registry.getEquipment().size());
        assertEquals(194, registry.getShips().size());
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Expansion() {
        log.info("testReadShipModels_ExpansionCache_Expansion");
        ExpansionCache cache = null;
        Expansion expansion = null;
        try {
            AddonsUtil.readShipModels(cache, expansion);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("cache must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Expansion2() throws IOException {
        log.info("testReadShipModels_ExpansionCache_Expansion2");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        Expansion expansion = null;
        try {
            AddonsUtil.readShipModels(cache, expansion);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Expansion3() throws IOException {
        log.info("testReadShipModels_ExpansionCache_Expansion3");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        Expansion expansion = new Expansion();

        assertEquals(0, expansion.getWarnings().size());
        AddonsUtil.readShipModels(cache, expansion);
        assertEquals(1, expansion.getWarnings().size());
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Expansion4() throws IOException {
        log.info("testReadShipModels_ExpansionCache_Expansion4");
        File oxpFile = new File("src/test/data/oolite.oxp.Norby.MinerCobra.oxz");
        assertTrue(oxpFile.exists());
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        Expansion expansion = new Expansion();
        expansion.setDownloadUrl(oxpFile.toURI().toString());

        assertEquals(0, expansion.getWarnings().size());
        AddonsUtil.readShipModels(cache, expansion);
        log.warn("warnings {}", expansion.getWarnings());
        assertEquals(0, expansion.getWarnings().size());
    }

    /**
     * Test of readModel method, of class AddonsUtil.
     */
    @Test
    public void testReadModel() {
        log.info("readModel");
        InputStream in = null;
        Expansion expansion = null;
        String zname = "";
        try {
            AddonsUtil.readModel(in, expansion, zname);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("data must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readModel method, of class AddonsUtil.
     */
    @Test
    public void testReadModel2() throws FileNotFoundException {
        log.info("readModel2");
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Expansion expansion = null;
        String zname = "";
        try {
            AddonsUtil.readModel(in, expansion, zname);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readModel method, of class AddonsUtil.
     */
    @Test
    public void testReadModel3() throws FileNotFoundException {
        log.info("readModel3");
        InputStream in = new FileInputStream("src/test/data/empty_file");
        Expansion expansion = new Expansion();
        String zname = "";

        assertEquals(0, expansion.getWarnings().size());
        AddonsUtil.readModel(in, expansion, zname);
        assertEquals(1, expansion.getWarnings().size());
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Registry() {
        log.info("readShipModels");
        ExpansionCache cache = null;
        Registry registry = null;
        try {
            AddonsUtil.readShipModels(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("cache must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Registry2() throws IOException {
        log.info("readShipModels2");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        Registry registry = null;
        try {
            AddonsUtil.readShipModels(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Registry3() throws IOException {
        log.info("readShipModels3");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);

        File oxpFile = new File("src/test/data/oolite.oxp.Norby.MinerCobra.oxz");
        assertTrue(oxpFile.exists());
        Expansion expansion = new Expansion("Norby.MinerCobra");
        expansion.setDownloadUrl(oxpFile.toURI().toString());
        Registry registry = new Registry();
        registry.addExpansion(expansion);

        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        AddonsUtil.readShipModels(cache, registry);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
    }

    /**
     * Test of readOxps method, of class AddonsUtil.
     */
    @Test
    public void testReadOxps() {
        log.info("readOxps");
        ExpansionCache cache = null;
        Registry registry = null;
        try {
            AddonsUtil.readOxps(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("cache must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readOxps method, of class AddonsUtil.
     */
    @Test
    public void testReadOxps2() throws IOException {
        log.info("testReadOxps2");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);

        Registry registry = null;
        try {
            AddonsUtil.readOxps(cache, registry);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }

    /**
     * Test of readOxps method, of class AddonsUtil.
     */
    @Test
    public void testReadOxps3() throws IOException {
        log.info("testReadOxps3");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);
        
        File oxpFile = new File("src/test/data/oolite.oxp.Norby.MinerCobra.oxz");
        assertTrue(oxpFile.exists());
        Expansion expansion = new Expansion("Norby.MinerCobra");
        expansion.setDownloadUrl(oxpFile.toURI().toString());
        Registry registry = new Registry();
        registry.addExpansion(expansion);
        
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        AddonsUtil.readOxps(cache, registry);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(3, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
    }

    /**
     * Test of readOxp method, of class AddonsUtil.
     */
    @Test
    public void testReadOxp() {
        log.info("readOxp");
        ExpansionCache cache = null;
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readOxp(cache, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("cache must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxp method, of class AddonsUtil.
     */
    @Test
    public void testReadOxp2() throws IOException {
        log.info("readOxp2");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);

        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readOxp(cache, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxp method, of class AddonsUtil.
     */
    @Test
    public void testReadOxp3() throws IOException {
        log.info("readOxp3");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);

        Registry registry = new Registry();

        Expansion oxp = null;
        try {
            AddonsUtil.readOxp(cache, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxp method, of class AddonsUtil.
     */
    @Test
    public void testReadOxp4() throws IOException {
        log.info("readOxp4");
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        ExpansionCache cache = new ExpansionCache(testCache);

        Registry registry = new Registry();

        Expansion oxp = new Expansion("nonexistent");
        oxp.setDownloadUrl("http://127.0.0.1:1/OoliteAddonScanner/test");
        registry.addExpansion(oxp);
        
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        AddonsUtil.readOxp(cache, registry, oxp);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(1, registry.getWarnings().size());
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry() throws Exception {
        log.info("readOxpEntry");

        try {
            AddonsUtil.readOxpEntry(null, null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("zin must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry2() throws Exception {
        log.info("readOxpEntry2");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.Norby.MinerCobra.oxz"));

        try {
            AddonsUtil.readOxpEntry(zin, null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("zentry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry3() throws Exception {
        log.info("readOxpEntry3");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.Frame.FuelCollector.oxz"));
        ZipEntry zentry = zin.getNextEntry();

        try {
            AddonsUtil.readOxpEntry(zin, zentry, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry4() throws Exception {
        log.info("readOxpEntry4");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.Frame.FuelCollector.oxz"));
        ZipEntry zentry = zin.getNextEntry();
        while (!zentry.getName().contains("equipment.plist")) {
            zentry = zin.getNextEntry();
        }
        
        Registry registry = new Registry();

        try {
            AddonsUtil.readOxpEntry(zin, zentry, registry, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry5() throws Exception {
        log.info("readOxpEntry5");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.Frame.FuelCollector.oxz"));
        ZipEntry zentry = zin.getNextEntry();
        while (!zentry.getName().contains("equipment.plist")) {
            zentry = zin.getNextEntry();
        }
        
        Registry registry = new Registry();
        
        Expansion expansion = new Expansion("myid");
        registry.addExpansion(expansion);


        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(1, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[Found XML equipment list]", registry.getWarnings().toString());
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry6() throws Exception {
        log.info("readOxpEntry6");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.Frame.FuelCollector.oxz"));
        ZipEntry zentry = zin.getNextEntry();
        while (!zentry.getName().contains("shipdata.plist")) {
            zentry = zin.getNextEntry();
        }
        
        Registry registry = new Registry();
        
        Expansion expansion = new Expansion("myid");
        registry.addExpansion(expansion);


        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
        AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(2, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry7() throws Exception {
        log.info("readOxpEntry7");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.ByronArn.AutoRefuel.oxz"));
        ZipEntry zentry = zin.getNextEntry();
        while (!zentry.getName().contains("script.js")) {
            zentry = zin.getNextEntry();
        }
        
        Registry registry = new Registry();
        
        Expansion expansion = new Expansion("myid");
        registry.addExpansion(expansion);


        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
        assertEquals(0, expansion.getScripts().size());
        AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
        assertEquals(1, expansion.getScripts().size());
    }

    /**
     * Test of readOxpEntry method, of class AddonsUtil.
     */
    @Test
    public void testReadOxpEntry8() throws Exception {
        log.info("readOxpEntry8");
        
        ZipInputStream zin = new ZipInputStream(new FileInputStream("src/test/data/oolite.oxp.cim.combat-simulator.oxz"));
        ZipEntry zentry = zin.getNextEntry();
        while (!zentry.getName().contains("world-scripts.plist")) {
            zentry = zin.getNextEntry();
        }
        
        Registry registry = new Registry();
        
        Expansion expansion = new Expansion("myid");
        registry.addExpansion(expansion);


        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
        assertEquals(0, expansion.getScripts().size());
        AddonsUtil.readOxpEntry(zin, zentry, registry, expansion);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getShips().size());
        assertEquals("[]", registry.getWarnings().toString());
        assertEquals(1, expansion.getScripts().size());
    }

    /**
     * Test of readManifest method, of class AddonsUtil.
     */
    @Test
    public void testReadManifest() throws Exception {
        log.info("readManifest");
        ZipInputStream zin = null;
        ZipEntry zentry = null;
        Registry registry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readManifest(zin, zentry, registry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of readScript method, of class AddonsUtil.
     */
    @Test
    public void testReadScript() throws Exception {
        log.info("readScript");
        ZipInputStream zin = null;
        ZipEntry zentry = null;
        Expansion oxp = null;
        try {
            AddonsUtil.readScript(zin, zentry, oxp);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("zin must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
}
