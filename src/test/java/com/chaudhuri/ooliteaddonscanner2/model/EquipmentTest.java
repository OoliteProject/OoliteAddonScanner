/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hiran
 */
public class EquipmentTest {
    private static final Logger log = LogManager.getLogger(EquipmentTest.class);
    
    public EquipmentTest() {
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

    @Test
    void testSetGetExpansion() {
        log.debug("testSetGetExpansion()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getExpansion());
            Expansion expansion = new Expansion();
            equipment.setExpansion(expansion);
            assertEquals(expansion, equipment.getExpansion());
        }
    }

    @Test
    void testSetGetTechlevel() {
        log.debug("testSetGetTechlevel()");
    }

    @Test
    void testSetGetCost() {
        log.debug("testSetGetCost()");
    }

    @Test
    void testSetGetIdentifier() {
        log.debug("testSetGetIdentifier()");
    }

    @Test
    void testSetGetDescription() {
        log.debug("testSetGetDescription()");
    }

    @Test
    void testSetGetName() {
        log.debug("testSetGetName()");
    }

    @Test
    void testSetGetFeatures() {
        log.debug("testSetGetFeatures()");
    }

    @Test
    void testToString() {
        log.debug("testToString()");
    }

    @Test
    void testGetAsWikipage() {
        log.debug("testGetAsWikipage()");
    }

    @Test
    void testGetType() {
        log.debug("testGetType()");
    }

    @Test
    void testAddGetWarning() {
        log.debug("testAddWarning()");
    }

    @Test
    void testIsVisible() {
        log.info("testIsVisible()");
        
        Equipment equipment = new Equipment();
        assertTrue(equipment.isVisible(), "No value should result in true");
        
        equipment.putFeature("visible", "false");
        assertFalse(equipment.isVisible(), "Visibility set to false");
        
        equipment.putFeature("visible", "true");
        assertTrue(equipment.isVisible(), "Visibility set to true");
        
        equipment.putFeature("visible", "no");
        assertFalse(equipment.isVisible(), "Visibility set to no");
        
        equipment.putFeature("visible", "yes");
        assertTrue(equipment.isVisible(), "Visibility set to yes");
        
        equipment.putFeature("visible", "wrong");
        assertTrue(equipment.isVisible(), "Visibility set wrongly");
    }
    
}
