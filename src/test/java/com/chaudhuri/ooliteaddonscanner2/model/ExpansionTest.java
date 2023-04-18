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
    public void testGetDownloadUrl() {
        System.out.println("getDownloadUrl");
    }

    /**
     * Test of setDownloadUrl method, of class Expansion.
     */
    @Test
    public void testSetDownloadUrl() {
        System.out.println("setDownloadUrl");
    }

    /**
     * Test of getTitle method, of class Expansion.
     */
    @Test
    public void testGetTitle() {
        System.out.println("getTitle");
    }

    /**
     * Test of getName method, of class Expansion.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
    }

    /**
     * Test of setTitle method, of class Expansion.
     */
    @Test
    public void testSetTitle() {
        System.out.println("setTitle");
    }

    /**
     * Test of getTags method, of class Expansion.
     */
    @Test
    public void testGetTags() {
        System.out.println("getTags");
    }

    /**
     * Test of setTags method, of class Expansion.
     */
    @Test
    public void testSetTags() {
        System.out.println("setTags");
    }

    /**
     * Test of getFileSize method, of class Expansion.
     */
    @Test
    public void testGetFileSize() {
        System.out.println("getFileSize");
    }

    /**
     * Test of setFileSize method, of class Expansion.
     */
    @Test
    public void testSetFileSize() {
        System.out.println("setFileSize");
    }

    /**
     * Test of getInformationUrl method, of class Expansion.
     */
    @Test
    public void testGetInformationUrl() {
        System.out.println("getInformationUrl");
    }

    /**
     * Test of setInformationUrl method, of class Expansion.
     */
    @Test
    public void testSetInformationUrl() {
        System.out.println("setInformationUrl");
    }

    /**
     * Test of getLicense method, of class Expansion.
     */
    @Test
    public void testGetLicense() {
        System.out.println("getLicense");
    }

    /**
     * Test of setLicense method, of class Expansion.
     */
    @Test
    public void testSetLicense() {
        System.out.println("setLicense");
    }

    /**
     * Test of getRequiredOoliteVersion method, of class Expansion.
     */
    @Test
    public void testGetRequiredOoliteVersion() {
        System.out.println("getRequiredOoliteVersion");
    }

    /**
     * Test of setRequiredOoliteVersion method, of class Expansion.
     */
    @Test
    public void testSetRequiredOoliteVersion() {
        System.out.println("setRequiredOoliteVersion");
    }

    /**
     * Test of getRequiresOxps method, of class Expansion.
     */
    @Test
    public void testGetRequiresOxps() {
        System.out.println("getRequiresOxps");
    }

    /**
     * Test of setRequiresOxps method, of class Expansion.
     */
    @Test
    public void testSetRequiresOxps() {
        System.out.println("setRequiresOxps");
    }

    /**
     * Test of getOptionalOxps method, of class Expansion.
     */
    @Test
    public void testGetOptionalOxps() {
        System.out.println("getOptionalOxps");
    }

    /**
     * Test of setOptionalOxps method, of class Expansion.
     */
    @Test
    public void testSetOptionalOxps() {
        System.out.println("setOptionalOxps");
    }

    /**
     * Test of getMaximumOoliteVersion method, of class Expansion.
     */
    @Test
    public void testGetMaximumOoliteVersion() {
        System.out.println("getMaximumOoliteVersion");
    }

    /**
     * Test of setMaximumOoliteVersion method, of class Expansion.
     */
    @Test
    public void testSetMaximumOoliteVersion() {
        System.out.println("setMaximumOoliteVersion");
    }

    /**
     * Test of getManifest method, of class Expansion.
     */
    @Test
    public void testGetManifest() {
        System.out.println("getManifest");
    }

    /**
     * Test of setManifest method, of class Expansion.
     */
    @Test
    public void testSetManifest() {
        System.out.println("setManifest");
    }

    /**
     * Test of getAsWikipage method, of class Expansion.
     */
    @Test
    public void testGetAsWikipage() {
        System.out.println("getAsWikipage");
    }

    /**
     * Test of setAsWikipage method, of class Expansion.
     */
    @Test
    public void testSetAsWikipage() {
        System.out.println("setAsWikipage");
    }

    /**
     * Test of getType method, of class Expansion.
     */
    @Test
    public void testGetType() {
        System.out.println("getType");
    }

    /**
     * Test of addScript method, of class Expansion.
     */
    @Test
    public void testAddScript() {
        System.out.println("addScript");
    }

    /**
     * Test of getScripts method, of class Expansion.
     */
    @Test
    public void testGetScripts() {
        System.out.println("getScripts");
    }

    /**
     * Test of addWarning method, of class Expansion.
     */
    @Test
    public void testAddWarning() {
        System.out.println("addWarning");
    }

    /**
     * Test of getWarnings method, of class Expansion.
     */
    @Test
    public void testGetWarnings() {
        System.out.println("getWarnings");
    }
    
}
