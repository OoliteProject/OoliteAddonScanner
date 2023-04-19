/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

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
        System.out.println("testGetExpansion");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getExpansion());
    }

    /**
     * Test of addShip method, of class Expansion.
     */
    @Test
    public void testAddShip() {
        System.out.println("addShip");
        
        Expansion expansion = new Expansion();
        {
            assertNotNull(expansion.getShips());
            assertEquals(0, expansion.getShips().size());
        }
        
        try {
            Ship ship = new Ship();
            expansion.addShip(ship);
            fail("Expected Exception but got none.");
        } catch (IllegalArgumentException e) {
            log.debug("Received expected exception", e);
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
        System.out.println("addReadme");
        
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
        System.out.println("toString, getIdentifier, setIdentifier");
        
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
        System.out.println("getEquipment");
        
        Expansion expansion = new Expansion();
        assertNotNull(expansion.getEquipment());
        assertEquals(0, expansion.getEquipment().size());

        try {
            Equipment equipment = new Equipment();
            expansion.addEquipment(equipment);
            fail("Expected exception but got none");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
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
        System.out.println("testSetGetAuthor");
        
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
        System.out.println("getCategory");

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
        System.out.println("testSetGetConflictOxps");
        
        Expansion expansion = new Expansion();
        assertNull(expansion.getConflictOxps());
        
        expansion.setConflictOxps("blah");
        assertEquals("blah", expansion.getConflictOxps());
    }

    /**
     * Test of getVersion method, of class Expansion.
     */
    @Test
    public void testSetGetVersion() {
        System.out.println("testSetGetVersion");
        
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
        System.out.println("testSetGetUploadDate");

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
        System.out.println("testSetGetDescription");

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
        System.out.println("testSetGetDownloadUrl");
        
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
        System.out.println("testSetGetTitle getName");
        
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
        System.out.println("testSetGetTags");

        Expansion expansion = new Expansion();
        assertNull(expansion.getTags());
        
        expansion.setTags("blah");
        assertEquals("blah", expansion.getTags());
    }

    /**
     * Test of getFileSize method, of class Expansion.
     */
    @Test
    public void testSetGetFileSize() {
        System.out.println("testSetGetFileSize");

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
        System.out.println("testSetGetInformationUrl");

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
        System.out.println("testSetGetLicense");

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
        System.out.println("testSetGetRequiredOoliteVersion");

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
        System.out.println("testSetGetRequiresOxps");

        Expansion expansion = new Expansion();
        assertNull(expansion.getRequiresOxps());
        
        expansion.setRequiresOxps("blah");
        assertEquals("blah", expansion.getRequiresOxps());
    }

    /**
     * Test of getOptionalOxps method, of class Expansion.
     */
    @Test
    public void testSetGetOptionalOxps() {
        System.out.println("getOptionalOxps");

        Expansion expansion = new Expansion();
        assertNull(expansion.getOptionalOxps());
        
        expansion.setOptionalOxps("blah");
        assertEquals("blah", expansion.getOptionalOxps());
    }

    /**
     * Test of getMaximumOoliteVersion method, of class Expansion.
     */
    @Test
    public void testSetGetMaximumOoliteVersion() {
        System.out.println("testSetGetMaximumOoliteVersion");

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
        System.out.println("testSetGetManifest");

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
        System.out.println("getAsWikipage");

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
        System.out.println("getType");

        Expansion expansion = new Expansion();
        assertEquals("Expansion", expansion.getType());
    }

    /**
     * Test of addScript method, of class Expansion.
     */
    @Test
    public void testAddGetScript() {
        System.out.println("testAddGetScript");

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
        System.out.println("testAddGetWarning");
        
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
    }

}
