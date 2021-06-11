/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class EquipmentTest {
    private static final Logger log = LoggerFactory.getLogger(EquipmentTest.class);
    
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
    public void testSetGetExpansion() {
        log.debug("testSetGetExpansion()");
    }

    @Test
    public void testSetGetTechlevel() {
        log.debug("testSetGetTechlevel()");
    }

    @Test
    public void testSetGetCost() {
        log.debug("testSetGetCost()");
    }

    @Test
    public void testSetGetIdentifier() {
        log.debug("testSetGetIdentifier()");
    }

    @Test
    public void testSetGetDescription() {
        log.debug("testSetGetDescription()");
    }

    @Test
    public void testSetGetName() {
        log.debug("testSetGetName()");
    }

    @Test
    public void testSetGetFeatures() {
        log.debug("testSetGetFeatures()");
    }

    @Test
    public void testToString() {
        log.debug("testToString()");
    }

    @Test
    public void testGetAsWikipage() {
        log.debug("testGetAsWikipage()");
    }

    @Test
    public void testGetType() {
        log.debug("testGetType()");
    }

    @Test
    public void testAddGetWarning() {
        log.debug("testAddWarning()");
    }

    @Test
    public void testIsVisible() {
        log.debug("testIsVisible()");
        
        Equipment equipment = new Equipment();
        Assertions.assertTrue(equipment.isVisible(), "No value should result in true");
        
        equipment.putFeature("visible", "false");
        Assertions.assertFalse(equipment.isVisible(), "Visibility set to false");
        
        equipment.putFeature("visible", "true");
        Assertions.assertTrue(equipment.isVisible(), "Visibility set to true");
        
        equipment.putFeature("visible", "no");
        Assertions.assertFalse(equipment.isVisible(), "Visibility set to no");
        
        equipment.putFeature("visible", "yes");
        Assertions.assertTrue(equipment.isVisible(), "Visibility set to yes");
        
        equipment.putFeature("visible", "wrong");
        Assertions.assertTrue(equipment.isVisible(), "Visibility set wrongly");
    }
    
}
