/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
    
    public AddonsUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
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
     * Test of readShipModels method, of class AddonsUtil.
     */
    @Test
    public void testReadShipModels_ExpansionCache_Expansion() {
        log.info("readShipModels");
        ExpansionCache cache = null;
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
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
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
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
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
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
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
            assertEquals("oxp must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
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
            assertEquals("zentry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
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
            assertEquals("oxp must not be null", e.getMessage());
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