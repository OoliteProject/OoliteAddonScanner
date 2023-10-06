/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hiran
 */
public class ExpansionTest {
    private static final Logger log = LogManager.getLogger();
    
    public ExpansionTest() {
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
     * Test of getExpansion method, of class Expansion.
     */
    @Test
    public void testGetExpansion() {
        log.info("testGetExpansion");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getExpansion());
    }

    /**
     * Test of addShip method, of class Expansion.
     */
    @Test
    public void testAddShip() {
        log.info("addShip");
        
        Expansion expansion = new Expansion();
        {
            assertNotNull(expansion.getShips());
            assertEquals(0, expansion.getShips().size());
        }
        
        {
            Ship ship = new Ship();
            try {
                expansion.addShip(ship);
                fail("Expected Exception but got none.");
            } catch (IllegalArgumentException e) {
                log.debug("Received expected exception", e);
            }
        }
        {
            Ship ship = new Ship();
            ship.setIdentifier("blah");
            expansion.addShip(ship);
            assertNotNull(expansion.getShips());
            assertEquals(1, expansion.getShips().size());
            assertEquals(ship, expansion.getShips().get(0));
        }
    }

    /**
     * Test of addReadme method, of class Expansion.
     */
    @Test
    public void testAddGetReadme() {
        log.info("addReadme");
        
        Expansion expansion = new Expansion();
        assertNotNull(expansion.getReadmes());
        assertEquals(0, expansion.getReadmes().size());
        
        expansion.addReadme("blah", "content");
        assertNotNull(expansion.getReadmes());
        assertEquals(1, expansion.getReadmes().size());
        assertEquals("blah", expansion.getReadmes().keySet().iterator().next());
        assertEquals("content", expansion.getReadmes().get("blah"));
    }

    /**
     * Test of toString method, of class Expansion.
     */
    @Test
    public void testToString() {
        log.info("toString, getIdentifier, setIdentifier");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.toString());
        assertNull(expansion.getIdentifier());
        
        expansion.setIdentifier("blah");
        assertEquals("blah", expansion.toString());
        assertEquals("blah", expansion.getIdentifier());
    }

    /**
     * Test of getEquipment method, of class Expansion.
     */
    @Test
    public void testAddGetEquipment() {
        log.info("getEquipment");
        
        Expansion expansion = new Expansion();
        assertNotNull(expansion.getEquipment());
        assertEquals(0, expansion.getEquipment().size());

        {
            Equipment equipment = new Equipment();
            try {
                expansion.addEquipment(equipment);
                fail("Expected exception but got none");
            } catch (IllegalArgumentException e) {
                log.debug("caught expected exception", e);
            }
        }
        
        {
            Equipment equipment = new Equipment();
            equipment.setIdentifier("blah");

            expansion.addEquipment(equipment);
            assertNotNull(expansion.getEquipment());
            assertEquals(1, expansion.getEquipment().size());
            assertEquals(equipment, expansion.getEquipment().get(0));
        }
    }

    /**
     * Test of getAuthor method, of class Expansion.
     */
    @Test
    public void testSetGetAuthor() {
        log.info("testSetGetAuthor");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getAuthor());
        
        expansion.setAuthor("blah");
        assertEquals("blah", expansion.getAuthor());
    }

    /**
     * Test of getCategory method, of class Expansion.
     */
    @Test
    public void testSetGetCategory() {
        log.info("getCategory");

        Expansion expansion = new Expansion();
        assertNull(expansion.getCategory());
        
        expansion.setCategory("blah");
        assertEquals("blah", expansion.getCategory());
    }

    /**
     * Test of getConflictOxps method, of class Expansion.
     */
    @Test
    public void testSetGetConflictOxps() {
        log.info("testSetGetConflictOxps");
        
//        Expansion expansion = new Expansion();
//        assertNull(expansion.getConflictOxps());
//        
//        Expansion.Dependency dep = new Expansion.Dependency();
//        List<Expansion.Dependency> deps = new ArrayList<>();
//        deps.add(dep);
//        
//        expansion.setConflictOxps(deps);
//        assertEquals(deps, expansion.getConflictOxps());
    }

    /**
     * Test of getVersion method, of class Expansion.
     */
    @Test
    public void testSetGetVersion() {
        log.info("testSetGetVersion");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getVersion());
        
        expansion.setVersion("blah");
        assertEquals("blah", expansion.getVersion());
    }

    /**
     * Test of getUploadDate method, of class Expansion.
     */
    @Test
    public void testSetGetUploadDate() {
        log.info("testSetGetUploadDate");

        Expansion expansion = new Expansion();
        assertNull(expansion.getUploadDate());
        
        expansion.setUploadDate("blah");
        assertEquals("blah", expansion.getUploadDate());
    }

    /**
     * Test of getDescription method, of class Expansion.
     */
    @Test
    public void testSetGetDescription() {
        log.info("testSetGetDescription");

        Expansion expansion = new Expansion();
        assertNull(expansion.getDescription());
        
        expansion.setDescription("blah");
        assertEquals("blah", expansion.getDescription());
    }

    /**
     * Test of getDownloadUrl method, of class Expansion.
     */
    @Test
    public void testSetGetDownloadUrl() {
        log.info("testSetGetDownloadUrl");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getDownloadUrl());
        
        expansion.setDownloadUrl("blah");
        assertEquals("blah", expansion.getDownloadUrl());
    }

    /**
     * Test of getTitle method, of class Expansion.
     */
    @Test
    public void testSetGetTitle() {
        log.info("testSetGetTitle getName");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getTitle());
        assertNull(expansion.getName());
        
        expansion.setTitle("blah");
        assertEquals("blah", expansion.getTitle());
        assertEquals("blah", expansion.getName());
    }

    /**
     * Test of getTags method, of class Expansion.
     */
    @Test
    public void testSetGetTags() {
        log.info("testSetGetTags");

        Expansion expansion = new Expansion();
        assertNull(expansion.getTags());
        
        expansion.setTags(Arrays.asList(new String[]{"blah"}));
        assertEquals("[blah]", String.valueOf(expansion.getTags()));
    }

    /**
     * Test of getFileSize method, of class Expansion.
     */
    @Test
    public void testSetGetFileSize() {
        log.info("testSetGetFileSize");

        Expansion expansion = new Expansion();
        assertNull(expansion.getFileSize());
        
        expansion.setFileSize("blah");
        assertEquals("blah", expansion.getFileSize());
    }

    /**
     * Test of getInformationUrl method, of class Expansion.
     */
    @Test
    public void testSetGetInformationUrl() {
        log.info("testSetGetInformationUrl");

        Expansion expansion = new Expansion();
        assertNull(expansion.getInformationUrl());
        
        expansion.setInformationUrl("blah");
        assertEquals("blah", expansion.getInformationUrl());
    }

    /**
     * Test of getLicense method, of class Expansion.
     */
    @Test
    public void testSetGetLicense() {
        log.info("testSetGetLicense");

        Expansion expansion = new Expansion();
        assertNull(expansion.getLicense());
        
        expansion.setLicense("blah");
        assertEquals("blah", expansion.getLicense());
    }

    /**
     * Test of getRequiredOoliteVersion method, of class Expansion.
     */
    @Test
    public void testSetGetRequiredOoliteVersion() {
        log.info("testSetGetRequiredOoliteVersion");

        Expansion expansion = new Expansion();
        assertNull(expansion.getRequiredOoliteVersion());
        
        expansion.setRequiredOoliteVersion("blah");
        assertEquals("blah", expansion.getRequiredOoliteVersion());
    }

    /**
     * Test of getRequiresOxps method, of class Expansion.
     */
    @Test
    public void testSetGetRequiresOxps() {
        log.info("testSetGetRequiresOxps");

//        Expansion expansion = new Expansion();
//        assertNull(expansion.getRequiresOxps());
//        
//        Expansion.Dependency dep = new Expansion.Dependency();
//        List<Expansion.Dependency> deps = new ArrayList<>();
//        deps.add(dep);
//        
//        expansion.setRequiresOxps(deps);
//        assertEquals(deps, expansion.getRequiresOxps());
    }

    /**
     * Test of getOptionalOxps method, of class Expansion.
     */
    @Test
    public void testSetGetOptionalOxps() {
        log.info("getOptionalOxps");

//        Expansion expansion = new Expansion();
//        assertNull(expansion.getOptionalOxps());
//        
//        Expansion.Dependency dep = new Expansion.Dependency();
//        List<Expansion.Dependency> deps = new ArrayList<>();
//        deps.add(dep);
//        
//        expansion.setOptionalOxps(deps);
//        assertEquals(deps, expansion.getOptionalOxps());
    }

    /**
     * Test of getMaximumOoliteVersion method, of class Expansion.
     */
    @Test
    public void testSetGetMaximumOoliteVersion() {
        log.info("testSetGetMaximumOoliteVersion");

        Expansion expansion = new Expansion();
        assertNull(expansion.getMaximumOoliteVersion());
        
        expansion.setMaximumOoliteVersion("blah");
        assertEquals("blah", expansion.getMaximumOoliteVersion());
    }

    /**
     * Test of getManifest method, of class Expansion.
     */
    @Test
    public void testSetGetManifest() {
        log.info("testSetGetManifest");

        Expansion expansion = new Expansion();
        ExpansionManifest manifest = new ExpansionManifest();

        assertNotNull(expansion.getManifest());
        assertNotEquals(manifest, expansion.getManifest());
        
        expansion.setManifest(manifest);
        assertEquals(manifest, expansion.getManifest());
    }

    /**
     * Test of getAsWikipage method, of class Expansion.
     */
    @Test
    public void testSetGetAsWikipage() {
        log.info("getAsWikipage");

        Expansion expansion = new Expansion();
        assertNull(expansion.getAsWikipage());
        
        expansion.setAsWikipage("blah");
        assertEquals("blah", expansion.getAsWikipage());
    }

    /**
     * Test of getType method, of class Expansion.
     */
    @Test
    public void testGetType() {
        log.info("getType");

        Expansion expansion = new Expansion();
        assertEquals("Expansion", expansion.getType());
    }

    /**
     * Test of addScript method, of class Expansion.
     */
    @Test
    public void testAddGetScript() {
        log.info("testAddGetScript");

        Expansion expansion = new Expansion();
        assertNotNull(expansion.getScripts());
        assertEquals(0, expansion.getScripts().size());
        
        expansion.addScript("name", "content");
        assertNotNull(expansion.getScripts());
        assertEquals(1, expansion.getScripts().size());
        assertEquals("name", expansion.getScripts().keySet().iterator().next());
        assertEquals("content", expansion.getScripts().get("name"));
    }

    /**
     * Test of addWarning method, of class Expansion.
     */
    @Test
    public void testAddGetWarning() {
        log.info("testAddGetWarning");
        
        Expansion expansion = new Expansion();
        assertNotNull(expansion.getWarnings());
        assertEquals(0, expansion.getWarnings().size());
        
        expansion.addWarning("blah");
        assertNotNull(expansion.getWarnings());
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("blah", expansion.getWarnings().get(0));
        
        ExpansionManifest manifest = new ExpansionManifest();
        expansion.setManifest(manifest);
        
        Equipment equipment = new Equipment();
        equipment.setIdentifier("equipment1");
        equipment.addWarning("equipment warning");
        expansion.addEquipment(equipment);
        
        Ship ship = new Ship();
        ship.setIdentifier("ship1");
        ship.addWarning("ship warning");
        expansion.addShip(ship);
        
        assertNotNull(expansion.getWarnings());
        assertEquals(3, expansion.getWarnings().size());
        assertEquals("blah", expansion.getWarnings().get(0));
        
        expansion.setManifest(null);

        assertNotNull(expansion.getWarnings());
        assertEquals(3, expansion.getWarnings().size());
        assertEquals("blah", expansion.getWarnings().get(0));
    }
    
    @Test
    public void testCreateExpansion() {
        log.info("testCreateExpansion");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getIdentifier());
        
        expansion = new Expansion("myoxp");
        assertEquals("myoxp", expansion.getIdentifier());
    }
    
    @Test
    public void testDuplicateEquipment() {
        log.info("testDuplicateEquipment");
        
        Expansion expansion = new Expansion();
        Equipment e1 = new Equipment("equipment");
        Equipment e2 = new Equipment("equipment");
        
        assertNotEquals(e1, e2);
        assertEquals(0, expansion.getEquipment().size());
        assertEquals(0, expansion.getWarnings().size());
        expansion.addEquipment(e1);
        assertEquals(1, expansion.getEquipment().size());
        assertEquals(0, expansion.getWarnings().size());
        expansion.addEquipment(e2);
        assertEquals(1, expansion.getEquipment().size());
        assertEquals(1, expansion.getWarnings().size());
    }

}
