/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
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
public class WikiTest {
    private static final Logger log = LogManager.getLogger();
    
    public WikiTest() {
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
     * Test of getPageUrl method, of class Wiki.
     */
    @Test
    public void testGetPageUrl() {
        System.out.println("getPageUrl");
        
        try {
            Wiki.getPageUrl(null);
            fail("Expected exception but got none.");
        } catch (IllegalArgumentException e) {
            log.debug("Caught expected exception", e);
        }
        assertEquals("http://wiki.alioth.net/index.php/name", Wiki.getPageUrl("name"));
        assertEquals("http://wiki.alioth.net/index.php/Dead%20Hunter", Wiki.getPageUrl("Dead Hunter"));
        assertEquals("http://wiki.alioth.net/index.php/Fuzzy-Fred%20of%20the%2020s", Wiki.getPageUrl("Fuzzy-Fred of the 20s"));
    }
    
    @Test
    public void testCheckWikiPage1() {
        {
            Equipment equipment = new Equipment();
            Wiki.checkWikiPage(equipment);
            assertEquals(1,  equipment.getWarnings().size());
            assertEquals("Wiki check failed: java.lang.IllegalArgumentException: parameter must not be null", equipment.getWarnings().get(0));
            assertNull(equipment.getAsWikipage());
        }
        {
            Equipment equipment = new Equipment();
            equipment.setIdentifier("neverexisted");
            Wiki.checkWikiPage(equipment);
            assertEquals(0,  equipment.getWarnings().size());
            assertNull(equipment.getAsWikipage());
        }
        {
            Equipment equipment = new Equipment();
            equipment.setName("neverexisted");
            Wiki.checkWikiPage(equipment);
            assertEquals(0,  equipment.getWarnings().size());
            assertNull(equipment.getAsWikipage());
        }
        {
            Equipment equipment = new Equipment();
            equipment.setName("Nexus");
            Wiki.checkWikiPage(equipment);
            assertEquals(0,  equipment.getWarnings().size());
            assertEquals("http://wiki.alioth.net/index.php/Nexus", equipment.getAsWikipage());
        }
    }

    /**
     * Test of checkWikiPage method, of class Wiki.
     */
    @Test
    public void testCheckWikiPage() {
        System.out.println("checkWikiPage");

        
        {
            Expansion expansion = new Expansion();
            Wiki.checkWikiPage(expansion);
            assertEquals(1,  expansion.getWarnings().size());
            assertEquals("Wiki check failed: java.lang.IllegalArgumentException: parameter must not be null", expansion.getWarnings().get(0));
            assertNull(expansion.getAsWikipage());
        }
        {
            Expansion expansion = new Expansion();
            expansion.setIdentifier("neverexisted");
            Wiki.checkWikiPage(expansion);
            assertEquals(1,  expansion.getWarnings().size());
            assertEquals("Wiki check failed: java.lang.IllegalArgumentException: parameter must not be null", expansion.getWarnings().get(0));
            assertNull(expansion.getAsWikipage());
        }
        {
            Expansion expansion = new Expansion();
            expansion.setIdentifier("Nexus");
            Wiki.checkWikiPage(expansion);
            assertEquals(1,  expansion.getWarnings().size());
            assertEquals("Wiki check failed: java.lang.IllegalArgumentException: parameter must not be null", expansion.getWarnings().get(0));
            assertNull(expansion.getAsWikipage());
        }
        
        {
            Ship ship = new Ship();
            Wiki.checkWikiPage(ship);
            assertEquals(1,  ship.getWarnings().size());
            assertEquals("Wiki check failed: java.lang.IllegalArgumentException: parameter must not be null", ship.getWarnings().get(0));
            assertNull(ship.getAsWikipage());
        }
        {
            Ship ship = new Ship();
            ship.setIdentifier("neverexisted");
            Wiki.checkWikiPage(ship);
            assertEquals(0,  ship.getWarnings().size());
            assertNull(ship.getAsWikipage());
        }
        {
            Ship ship = new Ship();
            ship.setIdentifier("Nexus");
            Wiki.checkWikiPage(ship);
            assertEquals(0,  ship.getWarnings().size());
            assertEquals("http://wiki.alioth.net/index.php/Nexus", ship.getAsWikipage());
        }
    }

    /**
     * Test of wikiPageFor method, of class Wiki.
     */
    @Test
    public void testWikiPageFor() {
        System.out.println("wikiPageFor");
    }
    
}
