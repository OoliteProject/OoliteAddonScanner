/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
public class RegistryTest {
    private static final Logger log = LogManager.getLogger();
    
    public RegistryTest() {
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
     * Test of addExpansions method, of class Registry.
     */
    @Test
    public void testAddExpansions() {
        System.out.println("addExpansions");
    }

    /**
     * Test of addExpansion method, of class Registry.
     */
    @Test
    public void testAddExpansion_PlistParserDictionaryContext() {
        System.out.println("addExpansion");
    }

    /**
     * Test of addExpansion method, of class Registry.
     */
    @Test
    public void testAddExpansion_Expansion() {
        log.info("addExpansion");
        
        Registry registry = new Registry();
        assertNotNull(registry.getExpansions());
        assertEquals(0, registry.getExpansions().size());
        
        Expansion expansion = new Expansion();
        try {
            registry.addExpansion(expansion);
            fail("Expected exception but caught none.");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        expansion.setIdentifier("blah");
        registry.addExpansion(expansion);
        assertNotNull(registry.getExpansions());
        assertEquals(1, registry.getExpansions().size());
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_List() throws Exception {
        System.out.println("addEquipmentList");
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_PlistParserListContext() throws Exception {
        System.out.println("addEquipmentList");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Expansion_List() throws Exception {
        System.out.println("addEquipment");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_Ship() {
        System.out.println("addShip");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Equipment() {
        System.out.println("addEquipment");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Expansion_PlistParserListContext() throws Exception {
        System.out.println("addEquipment");
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_Map() {
        System.out.println("addShipList");
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_PlistParserDictionaryContext() {
        System.out.println("addShipList");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_1() {
        System.out.println("addShip");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_2() {
        System.out.println("addShip");
    }

    /**
     * Test of getExpansions method, of class Registry.
     */
    @Test
    public void testGetExpansions() {
        System.out.println("getExpansions");
    }

    /**
     * Test of getExpansionsByName method, of class Registry.
     */
    @Test
    public void testGetExpansionsByName() {
        System.out.println("getExpansionsByName");
    }

    /**
     * Test of getEquipment method, of class Registry.
     */
    @Test
    public void testGetEquipment() {
        System.out.println("getEquipment");
    }

    /**
     * Test of getEquipmentByName method, of class Registry.
     */
    @Test
    public void testGetEquipmentByName() {
        System.out.println("getEquipmentByName");
    }

    /**
     * Test of getShips method, of class Registry.
     */
    @Test
    public void testGetShips() {
        System.out.println("getShips");
    }

    /**
     * Test of getShipsByName method, of class Registry.
     */
    @Test
    public void testGetShipsByName() {
        System.out.println("getShipsByName");
    }

    /**
     * Test of getAllByIdentifier method, of class Registry.
     */
    @Test
    public void testGetAllByIdentifier() {
        System.out.println("getAllByIdentifier");
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_Map() {
        System.out.println("toManifest");
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_PlistParserDictionaryContext() {
        System.out.println("toManifest");
    }

    /**
     * Test of addWarning method, of class Registry.
     */
    @Test
    public void testAddWarning() {
        System.out.println("addWarning");
    }

    /**
     * Test of getWarnings method, of class Registry.
     */
    @Test
    public void testGetWarnings() {
        System.out.println("getWarnings");
    }

    /**
     * Test of getGlobalWarnings method, of class Registry.
     */
    @Test
    public void testGetGlobalWarnings() {
        System.out.println("getGlobalWarnings");
    }

    /**
     * Test of getProperty method, of class Registry.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
    }

    /**
     * Test of setProperty method, of class Registry.
     */
    @Test
    public void testSetProperty() {
        System.out.println("setProperty");
    }

    /**
     * Test of getProperties method, of class Registry.
     */
    @Test
    public void testGetProperties() {
        System.out.println("getProperties");
    }
    
}
