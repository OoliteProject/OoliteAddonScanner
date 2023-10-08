/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hiran
 */
public class TemplateUtilTest {
    private static final Logger log = LogManager.getLogger();

    private static File tempCacheDir;
    
    public TemplateUtilTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        log.debug("setUpClass()");

        TreeMap<String, String> props = new TreeMap(System.getProperties());
        for (String key: props.keySet()) {
            log.info("{}->{}", key, System.getProperty(key));
        }
        log.info("cwd: {}", new File(".").getAbsolutePath());

        tempCacheDir = new File("target/testCacheDir");
        tempCacheDir.mkdirs();
    }
    
    @AfterAll
    public static void tearDownClass() throws IOException {
        log.debug("tearDownClass()");
        
//        if (tempCacheDir != null) {
//            FileUtils.deleteDirectory(tempCacheDir);
//        }
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes() throws Exception {
        log.info("printIndexes");
        
        try {
            TemplateUtil.printIndexes(null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes2() throws Exception {
        log.info("printIndexes2");
        
        Registry registry = new Registry();
        
        try {
            TemplateUtil.printIndexes(registry, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("outputdir must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes3() throws Exception {
        log.info("printIndexes3");
        
        Registry registry = new Registry();
        File outputdir = new File(tempCacheDir, "testPrintIndexes3");
        
        try {
            TemplateUtil.printIndexes(registry, outputdir, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("templateEngine must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes4() throws Exception {
        log.info("printIndexes4");
        
        Registry registry = new Registry();
        File outputdir = new File(tempCacheDir, "testPrintIndexes4");
        TemplateEngine templateEngine = new TemplateEngine();

        assertFalse(outputdir.isDirectory());
        try {
            TemplateUtil.printIndexes(registry, outputdir, templateEngine);
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("Registry is missing property key 'expansionManagerUrl'", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes5() throws Exception {
        log.info("printIndexes5");
        
        Registry registry = new Registry();
        registry.setProperty("expansionManagerUrl", "expansionManagerUrl");
        File outputdir = new File(tempCacheDir, "testPrintIndexes4");
        TemplateEngine templateEngine = new TemplateEngine();

        assertFalse(outputdir.isDirectory());
        try {
            TemplateUtil.printIndexes(registry, outputdir, templateEngine);
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("Registry is missing property key 'ooliteDownloadUrl'", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes6() throws Exception {
        log.info("printIndexes6");
        
        Registry registry = new Registry();
        registry.setProperty("expansionManagerUrl", "expansionManagerUrl");
        registry.setProperty("ooliteDownloadUrl", "ooliteDownloadUrl");
        File outputdir = new File(tempCacheDir, "testPrintIndexes4");
        TemplateEngine templateEngine = new TemplateEngine();

        assertFalse(outputdir.isDirectory());
        try {
            TemplateUtil.printIndexes(registry, outputdir, templateEngine);
            fail("expected exception");
        } catch (FileNotFoundException e) {
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printIndexes method, of class TemplateUtil.
     */
    @Test
    public void testPrintIndexes7() throws Exception {
        log.info("printIndexes7");
        
        Registry registry = new Registry();
        registry.setProperty("expansionManagerUrl", "expansionManagerUrl");
        registry.setProperty("ooliteDownloadUrl", "ooliteDownloadUrl");
        File outputdir = new File(tempCacheDir, "testPrintIndexes5");
        outputdir.mkdirs();
        TemplateEngine templateEngine = new TemplateEngine();

        assertTrue(outputdir.isDirectory());
        TemplateUtil.printIndexes(registry, outputdir, templateEngine);
        assertTrue(new File(outputdir, "index.html").isFile());
        assertTrue(new File(outputdir, "indexAllByIdentifier.html").isFile());
        assertTrue(new File(outputdir, "indexEquipmentByName.html").isFile());
        assertTrue(new File(outputdir, "indexExpansionsByName.html").isFile());
        assertTrue(new File(outputdir, "indexShipsByName.html").isFile());
        assertTrue(new File(outputdir, "indexShipsByName.html").isFile());
        assertTrue(new File(outputdir, "style.css").isFile());
        assertTrue(new File(outputdir, "warnings.html").isFile());
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions() throws Exception {
        log.info("printExpansions");

        try {
            TemplateUtil.printExpansions(null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions2() throws Exception {
        log.info("printExpansions2");
        
        Registry registry = new Registry();

        try {
            TemplateUtil.printExpansions(registry, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("outputdir must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions3() throws Exception {
        log.info("printExpansions3");
        
        Registry registry = new Registry();

        File outputdir = new File(tempCacheDir, "testPrintExpansions3");
        outputdir.mkdirs();
        
        try {
            TemplateUtil.printExpansions(registry, outputdir, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("templateEngine must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions4() throws Exception {
        log.info("printExpansions4");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("myId");
        expansion.setTitle("myOxp");
        expansion.setDescription("description");
        expansion.setCategory("category");
        expansion.setAuthor("author");
        expansion.setVersion("version");
        registry.addExpansion(expansion);

        File outputdir = new File(tempCacheDir, "testPrintExpansions4");
        outputdir.mkdirs();
        
        TemplateEngine templateEngine = new TemplateEngine();
        
        assertTrue(outputdir.isDirectory());
        TemplateUtil.printExpansions(registry, outputdir, templateEngine);
        assertTrue(new File(outputdir, "expansions/myId.html").isFile());
    }

    /**
     * Test of printExpansions method, of class TemplateUtil.
     */
    @Test
    public void testPrintExpansions5() throws Exception {
        log.info("printExpansions5");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("myId");
        expansion.setTitle("myOxp");
        expansion.setDescription("description");
        expansion.setCategory("category");
        expansion.setAuthor("author");
        expansion.setVersion("version");
        expansion.getManifest().setTags(Arrays.asList(new String[]{"tag", "one", "two"}));
        registry.addExpansion(expansion);

        File outputdir = new File(tempCacheDir, "testPrintExpansions4");
        outputdir.mkdirs();
        
        TemplateEngine templateEngine = new TemplateEngine();
        
        assertTrue(outputdir.isDirectory());
        TemplateUtil.printExpansions(registry, outputdir, templateEngine);
        assertTrue(new File(outputdir, "expansions/myId.html").isFile());
    }

    /**
     * Test of printEquipment method, of class TemplateUtil.
     */
    @Test
    public void testPrintEquipment() throws Exception {
        log.info("printEquipment");

        try {
            TemplateUtil.printEquipment(null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printEquipment method, of class TemplateUtil.
     */
    @Test
    public void testPrintEquipment2() throws Exception {
        log.info("printEquipment2");
        
        Registry registry = new Registry();

        try {
            TemplateUtil.printEquipment(registry, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("outputdir must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printEquipment method, of class TemplateUtil.
     */
    @Test
    public void testPrintEquipment3() throws Exception {
        log.info("printEquipment3");
        
        Registry registry = new Registry();

        File outputdir = new File(tempCacheDir, "testPrintEquipment3");
        outputdir.mkdirs();

        assertTrue(outputdir.isDirectory());
        try {
            TemplateUtil.printEquipment(registry, outputdir, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("templateEngine must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printEquipment method, of class TemplateUtil.
     */
    @Test
    public void testPrintEquipment4() throws Exception {
        log.info("printEquipment4");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("myExpansion");
        expansion.setTitle("title");
        Equipment equipment = new Equipment("myEquipment");
        equipment.setExpansion(expansion);
        equipment.setDescription("description");
        equipment.setCost("cost");
        equipment.setTechlevel("45");
        registry.addEquipment(equipment);

        File outputdir = new File(tempCacheDir, "testPrintEquipment4");
        outputdir.mkdirs();
        
        TemplateEngine templateEngine = new TemplateEngine();

        assertTrue(outputdir.isDirectory());
        TemplateUtil.printEquipment(registry, outputdir, templateEngine);
        assertTrue(new File(outputdir, "equipment/myEquipment.html").isFile());
    }

    /**
     * Test of printShips method, of class TemplateUtil.
     */
    @Test
    public void testPrintShips() throws Exception {
        log.info("testPrintShips");

        try {
            TemplateUtil.printShips(null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of testPrintShips method, of class TemplateUtil.
     */
    @Test
    public void testPrintShips2() throws Exception {
        log.info("printShips2");
        
        Registry registry = new Registry();

        try {
            TemplateUtil.printShips(registry, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("outputdir must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printShips method, of class TemplateUtil.
     */
    @Test
    public void testPrintShips3() throws Exception {
        log.info("printShips3");
        
        Registry registry = new Registry();

        File outputdir = new File(tempCacheDir, "testPrintShips3");
        outputdir.mkdirs();

        assertTrue(outputdir.isDirectory());
        try {
            TemplateUtil.printShips(registry, outputdir, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("templateEngine must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of printShips method, of class TemplateUtil.
     */
    @Test
    public void testPrintShips4() throws Exception {
        log.info("printShips4");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("myExpansion");
        expansion.setTitle("title");
        registry.addExpansion(expansion);
        Ship ship = new Ship("myShip");
        ship.setExpansion(expansion);
        registry.addShip(ship);

        File outputdir = new File(tempCacheDir, "testPrintShips4");
        outputdir.mkdirs();
        
        TemplateEngine templateEngine = new TemplateEngine();

        assertTrue(outputdir.isDirectory());
        TemplateUtil.printShips(registry, outputdir, templateEngine);
        assertTrue(new File(outputdir, "ships/myShip.html").isFile());
    }

}
