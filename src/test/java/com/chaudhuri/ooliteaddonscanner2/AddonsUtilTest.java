/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
     * Test of parseModel method, of class AddonsUtil.
     */
    @Test
    public void testParseModel() throws Exception {
        System.out.println("parseModel");
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
     * Test of getZipEntryStream method, of class AddonsUtil.
     */
    @Test
    public void testGetZipEntryStream() throws Exception {
        System.out.println("getZipEntryStream");
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
     * Test of readShips method, of class AddonsUtil.
     */
    @Test
    public void testReadShips() throws Exception {
        System.out.println("readShips");
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
     * Test of readEquipment method, of class AddonsUtil.
     */
    @Test
    public void testReadEquipment() throws Exception {
        System.out.println("readEquipment");
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
        System.out.println("readShipModels");
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
        System.out.println("readModel");
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
        System.out.println("readShipModels");
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
        System.out.println("readOxps");
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
        System.out.println("readOxp");
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
        System.out.println("readManifest");
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
        System.out.println("readScript");
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
