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
    public void testSetGetExpansion() {
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
    public void testSetGetTechlevel() {
        log.info("testSetGetTechlevel()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getTechlevel());
            equipment.setTechlevel("something");
            assertEquals("something", equipment.getTechlevel());
        }
    }

    @Test
    public void testSetGetCost() {
        log.debug("testSetGetCost()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getCost());
            equipment.setCost("something");
            assertEquals("something", equipment.getCost());
        }
    }

    @Test
    public void testSetGetIdentifier() {
        log.debug("testSetGetIdentifier()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getIdentifier());
            equipment.setIdentifier("something");
            assertEquals("something", equipment.getIdentifier());
        }
    }

    @Test
    public void testSetGetDescription() {
        log.debug("testSetGetDescription()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getDescription());
            equipment.setDescription("something");
            assertEquals("something", equipment.getDescription());
        }
    }

    @Test
    public void testSetGetName() {
        log.debug("testSetGetName()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getName());
            equipment.setIdentifier("ide");
            assertEquals("ide", equipment.getName());
            equipment.setName("something");
            assertEquals("something", equipment.getName());
        }
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getName());
            equipment.setName("");
            assertNull(equipment.getName());
        }
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getName());
            equipment.setName(null);
            equipment.setIdentifier("ide");
            assertEquals("ide", equipment.getName());
        }
    }

    @Test
    public void testSetGetFeatures() {
        log.debug("testSetGetFeatures()");
        {
            Equipment equipment = new Equipment();
            assertNotNull(equipment.getFeatures());
            assertEquals(0, equipment.getFeatures().size());
            
            equipment.putFeature("test", "myvalue");
            assertEquals(1, equipment.getFeatures().size());
            assertEquals("myvalue", equipment.getFeatures().get("test"));
        }
    }

    @Test
    public void testToString() {
        log.debug("testToString()");
        {
            Equipment equipment = new Equipment();
            assertEquals("com.chaudhuri.ooliteaddonscanner2.model.Equipment(expansion=null, identifier=null)", equipment.toString());
        }
    }

    @Test
    public void testGetAsWikipage() {
        log.debug("testGetAsWikipage()");
        {
            Equipment equipment = new Equipment();
            assertNull(equipment.getAsWikipage());
            equipment.setAsWikipage("mywiki");
            assertEquals("mywiki", equipment.getAsWikipage());
        }
    }

    @Test
    public void testGetType() {
        log.debug("testGetType()");
        {
            Equipment equipment = new Equipment();
            assertEquals("Equipment", equipment.getType());
        }
    }

    @Test
    public void testAddGetWarning() {
        log.debug("testAddWarning()");
        {
            Equipment equipment = new Equipment();
            assertNotNull(equipment.getWarnings());
            assertEquals(0, equipment.getWarnings().size());

            equipment.addWarning("attention");
            assertEquals(1, equipment.getWarnings().size());
            assertEquals("attention", equipment.getWarnings().get(0));
        }
    }

    @Test
    public void testIsVisible() {
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

    @Test
    public void testIsPrimeable() {
        log.info("testIsPrimeable()");
        
        Equipment equipment = new Equipment();
        assertFalse(equipment.isPrimeable());
        
        equipment.putFeature("script", "somescript");
        assertTrue(equipment.isPrimeable());
    }
}
