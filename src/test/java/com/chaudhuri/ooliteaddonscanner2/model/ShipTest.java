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
public class ShipTest {
    private static final Logger log = LogManager.getLogger();
    
    public ShipTest() {
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
     * Test of getExpansion method, of class Ship.
     */
    @Test
    public void testSetGetExpansion() {
        log.info("testSetGetExpansion");
        
        Ship ship = new Ship();
        assertNull(ship.getExpansion());
        
        Expansion expansion = new Expansion();
        ship.setExpansion(expansion);
        assertEquals(expansion, ship.getExpansion());
    }

    /**
     * Test of getIdentifier method, of class Ship.
     */
    @Test
    public void testSetGetIdentifier() {
        log.info("testSetGetIdentifier");
        
        Ship ship = new Ship();
        assertNull(ship.getIdentifier());
        
        ship.setIdentifier("blah");
        assertEquals("blah", ship.getIdentifier());
    }

    /**
     * Test of addFeature method, of class Ship.
     */
    @Test
    public void testAddHasGetFeature() {
        log.info("testAddHasGetFeature getFeatures");
        
        Ship ship = new Ship();
        assertNotNull(ship.getFeatures());
        assertEquals(0, ship.getFeatures().size());
        assertFalse(ship.hasFeature("name"));
        
        ship.addFeature("name", "content");
        assertNotNull(ship.getFeatures());
        assertEquals(1, ship.getFeatures().size());
        assertEquals("name", ship.getFeatures().keySet().iterator().next());
        assertEquals("content", ship.getFeature("name"));
        assertTrue(ship.hasFeature("name"));
    }

    /**
     * Test of getName method, of class Ship.
     */
    @Test
    public void testGetName() {
        log.info("getName");
        
        Ship ship = new Ship();
        assertNull(ship.getName());
        
        ship.setIdentifier("blah");
        assertEquals("blah", ship.getName());
        
        ship.addFeature("name", "hoho");
        assertEquals("hoho", ship.getName());
    }

    /**
     * Test of getAsWikipage method, of class Ship.
     */
    @Test
    public void testSetGetAsWikipage() {
        log.info("testSetGetAsWikipage");
        
        Ship ship = new Ship();
        assertNull(ship.getAsWikipage());
        
        ship.setAsWikipage("blah");
        assertEquals("blah", ship.getAsWikipage());
    }

    /**
     * Test of getType method, of class Ship.
     */
    @Test
    public void testGetType() {
        log.info("getType");
        
        Ship ship = new Ship();
        assertEquals("Ship", ship.getType());
    }

    /**
     * Test of addWarning method, of class Ship.
     */
    @Test
    public void testAddGetWarning() {
        log.info("addWarning");
        
        Ship ship = new Ship();
        assertNotNull(ship.getWarnings());
        assertEquals(0, ship.getWarnings().size());
        
        ship.addWarning("blah");
        assertNotNull(ship.getWarnings());
        assertEquals(1, ship.getWarnings().size());
        assertEquals("blah", ship.getWarnings().get(0));
    }

    /**
     * Test of isTemplate method, of class Ship.
     */
    @Test
    public void testIsTemplate() {
        log.info("isTemplate");
        
        Ship ship = new Ship();
        assertFalse(ship.isTemplate());
        
        ship.addFeature("is_template", "blah");
        assertFalse(ship.isTemplate());

        ship.addFeature("is_template", "1");
        assertTrue(ship.isTemplate());
        
        ship.addFeature("is_template", "yes");
        assertTrue(ship.isTemplate());
        
        ship.addFeature("is_template", "true");
        assertTrue(ship.isTemplate());
        
    }
    
}
