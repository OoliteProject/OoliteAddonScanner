/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.plist.PlistParser;
import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.xml.sax.SAXException;

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
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_Ship() {
        log.info("addShip");
        
        Registry registry = new Registry();
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(0, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
       
        {
            Ship ship = new Ship();
            try {
                registry.addShip(ship);
                fail("expected exception");
            } catch (IllegalArgumentException e) {
                log.debug("caught expected exception", e);
            }
        }
        {
            Ship ship = new Ship("identity");
            try {
                registry.addShip(ship);
                fail("expected exception");
            } catch (IllegalArgumentException e) {
                log.debug("caught expected exception", e);
            }
        }


        Expansion oxp = new Expansion();
        
        Ship ship = new Ship("s1");
        ship.setExpansion(oxp);
        registry.addShip(ship);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(1, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        
        Ship ship2 = new Ship("s2");
        ship2.setExpansion(oxp);
        registry.addShip(ship2);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(2, registry.getShips().size());
        assertEquals(0, registry.getWarnings().size());
        
        Ship ship3 = new Ship("s2");
        ship3.setExpansion(oxp);
        registry.addShip(ship3);
        log.debug("ships in registry: {}", registry.getShips());
        assertEquals(2, registry.getShips().size());
        assertEquals(1, registry.getWarnings().size());
    }

    /**
     * Test of getExpansionsByName method, of class Registry.
     */
    @Test
    public void testGetExpansionsByName() {
        log.info("getExpansionsByName");
        
        Registry registry = new Registry();
        Expansion e1 = new Expansion();
        e1.setIdentifier("C");
        e1.setTitle("A");
        Expansion e2 = new Expansion();
        e2.setIdentifier("B");
        e2.setTitle("B");
        Expansion e3 = new Expansion();
        e3.setIdentifier("A");
        e3.setTitle("C");
        registry.addExpansion(e1);
        registry.addExpansion(e2);
        registry.addExpansion(e3);
        
        List<Expansion> list = null;
        list = registry.getExpansions();
        log.debug("expansions: {}", list);
        assertEquals(e3, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e1, list.get(2));

        list = registry.getExpansionsByName();
        log.debug("expansions: {}", list);
        assertEquals(e1, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e3, list.get(2));
    }

    /**
     * Test of getShipsByName method, of class Registry.
     */
    @Test
    public void testGetShipsByName() {
        log.info("getShipsByName");
        
        Expansion expansion = new Expansion("myexpansion");
        Ship s1 = new Ship("1");
        s1.setExpansion(expansion);
        s1.setName("C");
        Ship s2 = new Ship("2");
        s2.setExpansion(expansion);
        s2.setName("B");
        Ship s3 = new Ship("3");
        s3.setExpansion(expansion);
        s3.setName("A");
        
        Registry registry = new Registry();
        registry.addShip(s1);
        registry.addShip(s2);
        registry.addShip(s3);
        
        assertEquals(3, registry.getShips().size());
        assertEquals("1", registry.getShips().get(0).getIdentifier());
        assertEquals("2", registry.getShips().get(1).getIdentifier());
        assertEquals("3", registry.getShips().get(2).getIdentifier());
        
        assertEquals("3", registry.getShipsByName().get(0).getIdentifier());
        assertEquals("2", registry.getShipsByName().get(1).getIdentifier());
        assertEquals("1", registry.getShipsByName().get(2).getIdentifier());
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_Map() {
        log.info("toManifest");
        
        Map<String, Object> map = new TreeMap<>();
        map.put("arbitrary", "what?");
        map.put(Registry.EXPANSION_AUTHOR, "myauthor");
        map.put(Registry.EXPANSION_CATEGORY, "mycategory");
        map.put(Registry.EXPANSION_CONFLICT_OXPS, "conflict");
        map.put(Registry.EXPANSION_DESCRIPTION, "description");
        map.put(Registry.EXPANSION_IDENTIFIER, "identifier");
        map.put(Registry.EXPANSION_INFORMATION_URL, "infourl");
        map.put(Registry.EXPANSION_LICENSE, "license");
        map.put(Registry.EXPANSION_MAXIMUM_OOLITE_VERSION, "maxoolite");
        map.put(Registry.EXPANSION_OPTIONAL_OXPS, "optoxps");
        map.put(Registry.EXPANSION_REQUIRED_OOLITE_VERSION, "reqoolite");
        map.put(Registry.EXPANSION_REQUIRES_OXPS, "reqoxps");
        map.put(Registry.EXPANSION_TAGS, "mytags");
        map.put(Registry.EXPANSION_TITLE, "mytitle");
        map.put(Registry.EXPANSION_VERSION, "version");
        
        Registry registry = new Registry();
        ExpansionManifest em = registry.toManifest(map);
        
        assertEquals("myauthor", em.getAuthor());
        assertEquals("mycategory", em.getCategory());
        assertEquals("conflict", em.getConflictOxps());
        assertEquals("description", em.getDescription());
        assertEquals("identifier", em.getIdentifier());
        assertEquals("infourl", em.getInformationUrl());
        assertEquals("license", em.getLicense());
        assertEquals("maxoolite", em.getMaximumOoliteVersion());
        assertEquals("optoxps", em.getOptionalOxps());
        assertEquals("reqoolite", em.getRequiredOoliteVersion());
        assertEquals("reqoxps", em.getRequiresOxps());
        assertEquals("mytags", em.getTags());
        assertEquals("mytitle", em.getTitle());
        assertEquals("version", em.getVersion());
        assertEquals(1, em.getWarnings().size());
    }

    /**
     * Test of addWarning method, of class Registry.
     */
    @Test
    public void testAddGetWarning() {
        log.info("addGetWarning");
        
        Registry registry = new Registry();
        assertNotNull(registry.getWarnings());
        assertEquals(0, registry.getWarnings().size());
        
        registry.addWarning("mywarning");
        assertEquals(1, registry.getWarnings().size());
        assertEquals("mywarning", registry.getWarnings().get(0));
        
        Expansion expansion = new Expansion();
        expansion.setIdentifier("myoxp");
        registry.addExpansion(expansion);
        assertEquals(1, registry.getWarnings().size());
        assertEquals("mywarning", registry.getWarnings().get(0));

        expansion.addWarning("expansion warning");
        assertEquals(2, registry.getWarnings().size());
        assertEquals("expansion warning", registry.getWarnings().get(1));

        assertEquals(1, registry.getGlobalWarnings().size());
        assertEquals("mywarning", registry.getGlobalWarnings().get(0));
    }

    /**
     * Test of getProperty method, of class Registry.
     */
    @Test
    public void testGetProperties() {
        log.info("testGetProperties");
        
        Registry registry = new Registry();
        assertNotNull(registry.getProperties());
        assertEquals(0, registry.getProperties().size());
    }
    
    @Test
    public void testSetGetProperty() {
        log.info("testSetGetProperty");

        Registry registry = new Registry();
        assertEquals(0, registry.getProperties().size());
        assertNull(registry.getProperty("mykey"));

        registry.setProperty("mykey", "myvalue");
        assertEquals(1, registry.getProperties().size());
        assertEquals("myvalue", registry.getProperty("mykey"));
        
        registry.setProperty("mykey", null);
        assertNull(registry.getProperty("mykey"));

        try {
            registry.setProperty(null, "myvalue");
            fail("expected exception but caught none");
        } catch (NullPointerException e) {
            log.debug("caught expected exception", e);
        }
    }
    
    @Test
    public void testOverwriteExpansion() {
        log.info("testOverwriteExpansion");
        
        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        assertEquals(0, registry.getWarnings().size());
        
        Expansion e1 = new Expansion("A");
        registry.addExpansion(e1);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(0, registry.getWarnings().size());
        
        Expansion e2 = new Expansion("A");
        registry.addExpansion(e2);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(1, registry.getWarnings().size());
        assertEquals("OXP Overwrite! A (null) and A (null) share same id A", registry.getWarnings().get(0));
    }

    /**
     * Test of addExpansions method, of class Registry.
     */
    @Test
    public void testAddExpansions() {
        log.info("addExpansions");
        
        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        
        Expansion expansion = new Expansion("unique");
        registry.addExpansion(expansion);
        assertEquals(1, registry.getExpansions().size());
        assertEquals(expansion, registry.getExpansions().get(0));
        
    }
    
    /**
     * Test adding a list of expansions.
     */
    @Test
    public void testAddExpansion_ListContext() throws IOException {
        log.info("testAddExpansion_ListContext");
        
        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        try {
            registry.addExpansions(null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        {
            URL url = getClass().getResource("/registryTest/manifest4.plist");

            // parse plist test data
            PlistParser.ListContext lc = PlistParserUtil.parsePlistList(url.openStream(), url.toString());

            registry.addExpansions(lc);
            log.info("{}", registry.getWarnings());
            assertEquals(3, registry.getExpansions().size());
            assertEquals(0, registry.getWarnings().size());
        }
    }

    @Test
    public void testAddEquipmentList_Expansion_ListContext() throws IOException, RegistryException {
        log.info("testAddEquipmentList_Expansion_ListContext");
        
        Registry registry = new Registry();
        try {
            registry.addEquipmentList(null, (PlistParser.ListContext)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }

        URL url = getClass().getResource("/registryTest/equipment1.plist");

        // parse plist test data
        PlistParser.ListContext lc = PlistParserUtil.parsePlistList(url.openStream(), url.toString());
        try {
            registry.addEquipmentList(null, (PlistParser.ListContext)lc);
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }

        Expansion expansion = new Expansion();

        try {
            registry.addEquipmentList(expansion, (PlistParser.ListContext)null);
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        registry.addEquipmentList(expansion, (PlistParser.ListContext)lc);
        assertEquals(3, registry.getEquipment().size());
    }
    
    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddGetEquipment_Equipment() {
        log.info("testAddGetEquipment");
        
        Expansion expansion = new Expansion("myoxp");
        
        Registry registry = new Registry();
        assertEquals(0, registry.getEquipment().size());
        
        Equipment equipment = new Equipment();
        try {
            registry.addEquipment(equipment);
            fail("expected exception but caught none");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }

        equipment.setIdentifier("blah");
        try {
            registry.addEquipment(equipment);
            fail("expected exception but caught none");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        equipment.setExpansion(expansion);
        registry.addEquipment(equipment);
        assertEquals(1, registry.getEquipment().size());
        assertEquals(equipment, registry.getEquipment().get(0));

        Equipment equipment2 = new Equipment();
        equipment2.setIdentifier("blah2");
        equipment2.setExpansion(expansion);
        registry.addEquipment(equipment2);
        assertEquals(2, registry.getEquipment().size());
        assertEquals(equipment, registry.getEquipment().get(0));
        assertEquals(equipment2, registry.getEquipment().get(1));

        Equipment equipment3 = new Equipment();
        equipment3.setIdentifier("blah");
        equipment3.setExpansion(expansion);
        registry.addEquipment(equipment3);
        assertEquals(2, registry.getEquipment().size());
        assertEquals(equipment3, registry.getEquipment().get(0));
        assertEquals(equipment2, registry.getEquipment().get(1));
    }

    /**
     * Test of getEquipmentByName method, of class Registry.
     */
    @Test
    public void testGetEquipmentByName() {
        log.info("getEquipmentByName");
        
        Expansion expansion = new Expansion();
        
        Equipment e1 = new Equipment();
        e1.setIdentifier("1");
        e1.setExpansion(expansion);
        Equipment e2 = new Equipment();
        e2.setIdentifier("2");
        e2.setExpansion(expansion);
        Equipment e3 = new Equipment();
        e3.setIdentifier("3");
        e3.setExpansion(expansion);
        
        Registry registry = new Registry();
        registry.addEquipment(e1);
        registry.addEquipment(e2);
        registry.addEquipment(e3);

        assertEquals(3, registry.getEquipment().size());
        assertEquals(e1, registry.getEquipment().get(0));
        assertEquals(e2, registry.getEquipment().get(1));
        assertEquals(e3, registry.getEquipment().get(2));

        assertEquals(3, registry.getEquipmentByName().size());
        assertEquals(e1, registry.getEquipmentByName().get(0));
        assertEquals(e2, registry.getEquipmentByName().get(1));
        assertEquals(e3, registry.getEquipmentByName().get(2));
    }

    /**
     * Test of getAllByIdentifier method, of class Registry.
     */
    @Test
    public void testGetAllByIdentifier() {
        log.info("getAllByIdentifier");
        
        Expansion expansion = new Expansion("C");
        Equipment equipment = new Equipment("B");
        equipment.setExpansion(expansion);
        Ship ship = new Ship("A");
        ship.setExpansion(expansion);
        
        Registry registry = new Registry();
        registry.addExpansion(expansion);
        registry.addEquipment(equipment);
        registry.addShip(ship);
        
        assertEquals(3, registry.getAllByIdentifier().size());
        assertEquals(ship, registry.getAllByIdentifier().get(0));
        assertEquals(equipment, registry.getAllByIdentifier().get(1));
        assertEquals(expansion, registry.getAllByIdentifier().get(2));
    }

    /**
     * Test of addExpansion method, of class Registry.
     */
    @Test
    public void testAddExpansion_PlistParserDictionaryContext() throws IOException {
        log.info("addExpansion");

        Registry registry = new Registry();
        assertEquals(0, registry.getExpansions().size());
        {
            URL url = getClass().getResource("/registryTest/manifest1.plist");

            // parse plist test data
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());

            registry.addExpansion(dc);
            assertEquals(1, registry.getExpansions().size());
            assertEquals(1, registry.getWarnings().size());
        }
        {
            URL url = getClass().getResource("/registryTest/manifest2.plist");

            // parse plist test data
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());

            registry.addExpansion(dc);
            assertEquals(1, registry.getExpansions().size());
            assertEquals(1, registry.getWarnings().size());
        }
        {
            URL url = getClass().getResource("/registryTest/manifest3.plist");

            // parse plist test data
            PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());

            registry.addExpansion(dc);
            assertEquals(1, registry.getExpansions().size());
            assertEquals(3, registry.getWarnings().size());
        }
        log.debug("Registry warnings: {}", registry.getWarnings());
    }

    /**
     * Test of toManifest method, of class Registry.
     */
    @Test
    public void testToManifest_PlistParserDictionaryContext() throws IOException {
        log.info("toManifest");
        
        URL url = getClass().getResource("/registryTest/expansionManifest1.plist");
        PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());
        
        Registry registry = new Registry();
        ExpansionManifest em = registry.toManifest(dc);
        assertEquals("oolite.oxp.Alnivel.RoutePlanner", em.getIdentifier());
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_PlistParserListContext() throws Exception {
        log.info("addEquipmentList");
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_Map() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        log.info("addShipList");
        
        Registry registry = new Registry();
        try {
            registry.addShipList(null, (Map)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
        
        Expansion expansion = new Expansion("testexpansion");
        
        try {
            registry.addShipList(expansion, (Map)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("shipList must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
        
        URL url = getClass().getResource("/registryTest/ship1.xml");
        Map<String, Object> map = XMLPlistParser.parseDictionary(url.openStream(), null);
        registry.addShipList(expansion, map);
        assertEquals(3, registry.getShips().size());
    }

    /**
     * Test of addShipList method, of class Registry.
     */
    @Test
    public void testAddShipList_Expansion_PlistParserDictionaryContext() throws IOException {
        log.info("addShipList");
        
        Registry registry = new Registry();

        try {
            registry.addShipList(null, (PlistParser.DictionaryContext)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
        
        Expansion expansion = new Expansion("testAddShipListExpansion");

        try {
            registry.addShipList(expansion, (PlistParser.DictionaryContext)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("dc must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }

        URL url = getClass().getResource("/registryTest/ship1.plist");
        PlistParser.DictionaryContext dc = PlistParserUtil.parsePlistDictionary(url.openStream(), url.toString());
        registry.addShipList(expansion, dc);
        
        assertEquals(3, registry.getShips().size());
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_1() {
        log.info("addShip");
    }

    /**
     * Test of addShip method, of class Registry.
     */
    @Test
    public void testAddShip_3args_2() {
        log.info("addShip");
    }

    /**
     * Test of addEquipment method, of class Registry.
     */
    @Test
    public void testAddEquipment_Expansion_PlistParserListContext() throws Exception {
        log.info("addEquipment");
        
        Registry registry = new Registry();
        try {
            registry.addEquipment(null, (PlistParser.ListContext)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }

        Expansion expansion = new Expansion();

        try {
            registry.addEquipment(expansion, (PlistParser.ListContext)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            log.debug("caught expected exception", e);
        }
        
        URL url = getClass().getResource("/registryTest/equipment2.plist");

        // parse plist test data
        PlistParser.ListContext lc = PlistParserUtil.parsePlistList(url.openStream(), url.toString());
        registry.addEquipment(expansion, (PlistParser.ListContext)lc);
        assertEquals(1, registry.getEquipment().size());
    }

    @Test
    public void testAddEquipment_Expansion_List() throws IOException, ParserConfigurationException, SAXException, RegistryException {
        log.info("testAddEquipment_Expansion_List");
        
        Registry registry = new Registry();
        try {
            registry.addEquipment((Expansion)null, (List<Map<String, Object>>)null);
            fail("Expected exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testAddEquipment_Expansion_List2() throws IOException, ParserConfigurationException, SAXException, RegistryException {
        log.info("testAddEquipment_Expansion_List2");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        try {
            registry.addEquipment(expansion, (List<Map<String, Object>>)null);
            fail("Expected exception.");
        } catch (IllegalArgumentException e) {
            assertEquals("list must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testAddEquipment_Expansion_List3() throws IOException, ParserConfigurationException, SAXException, TransformerException, RegistryException {
        log.info("testAddEquipment_Expansion_List3");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        URL url = getClass().getResource("/registryTest/equipment1.xml");
        List<Map<String, Object>> list = (List<Map<String, Object>>)(Object)XMLPlistParser.parseList(url.openStream(), null);

        try {
            registry.addEquipment(expansion, list);
            fail("Exception expected");
        } catch (RegistryException e) {
            assertEquals("Could not evaluate expansion null equipment [[[0, 12500, Personal Fireworks, EQ_FIREWORKS, Have you just become ELITE? Have you smashed the biggest party of Thargoids ever? Or have you any other reason to celebrate? Here's the way to do it! A display of fireworks will be arranged solely for you by the station master right in front of the station. Just launch and enjoy your personal fireworks!, {available_to_all=true}], [99, 7500, Portable Fireworks, EQ_FIREWORKS_MINE, Enjoyed your fireworks? This is the portable version. Just carry one of these pylon-mounted devices with you and use it whenever you feel like celebrating! It is launched like a mine, has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}], [99, 5000, Fireworks Missile, EQ_FIREWORKS_MISSILE, The Fireworks Missile is a blank missile fitted with smaller blanks. It has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}]]]", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testAddEquipment_Expansion_List4() throws IOException, ParserConfigurationException, SAXException, TransformerException, RegistryException {
        log.info("testAddEquipment_Expansion_List4");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        URL url = getClass().getResource("/registryTest/equipment1.xml");
        List<List<Map<String, Object>>> list = (List<List<Map<String, Object>>>)(Object)XMLPlistParser.parseList(url.openStream(), null);

        try {
            registry.addEquipment(expansion, list.get(0));
            fail("Exception expected");
        } catch (RegistryException e) {
            assertEquals("Could not evaluate expansion null equipment [[0, 12500, Personal Fireworks, EQ_FIREWORKS, Have you just become ELITE? Have you smashed the biggest party of Thargoids ever? Or have you any other reason to celebrate? Here's the way to do it! A display of fireworks will be arranged solely for you by the station master right in front of the station. Just launch and enjoy your personal fireworks!, {available_to_all=true}], [99, 7500, Portable Fireworks, EQ_FIREWORKS_MINE, Enjoyed your fireworks? This is the portable version. Just carry one of these pylon-mounted devices with you and use it whenever you feel like celebrating! It is launched like a mine, has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}], [99, 5000, Fireworks Missile, EQ_FIREWORKS_MISSILE, The Fireworks Missile is a blank missile fitted with smaller blanks. It has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}]]", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    @Test
    public void testAddEquipment_Expansion_List5() throws IOException, ParserConfigurationException, SAXException, TransformerException, RegistryException {
        log.info("testAddEquipment_Expansion_List5");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        URL url = getClass().getResource("/registryTest/equipment1.xml");
        List<List<List<Map<String, Object>>>> list = (List<List<List<Map<String, Object>>>>)(Object)XMLPlistParser.parseList(url.openStream(), null);

        assertEquals(0, registry.getEquipment().size());
        assertEquals(0, registry.getWarnings().size());
        registry.addEquipment(expansion, list.get(0).get(0));
        assertEquals(1, registry.getEquipment().size());
        assertEquals(0, registry.getWarnings().size());
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_List() throws Exception {
        log.info("addEquipmentList_Expansion_List");
        
        Registry registry = new Registry();
        try {
            registry.addEquipmentList(null, (List<List<Map<String, Object>>>)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_List2() throws Exception {
        log.info("addEquipmentList_Expansion_List2");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        try {
            registry.addEquipmentList(expansion, (List<List<Map<String, Object>>>)null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("list must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of addEquipmentList method, of class Registry.
     */
    @Test
    public void testAddEquipmentList_Expansion_List3() throws Exception {
        log.info("addEquipmentList_Expansion_List3");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        URL url = getClass().getResource("/registryTest/equipment1.xml");
        List<List<Map<String, Object>>> list = (List<List<Map<String, Object>>>)(Object)XMLPlistParser.parseList(url.openStream(), null);
        try {
            registry.addEquipmentList(expansion, list);
            fail("expected exception");
        } catch (RegistryException e) {
            assertEquals("Could not evaluate expansion null equipment [[0, 12500, Personal Fireworks, EQ_FIREWORKS, Have you just become ELITE? Have you smashed the biggest party of Thargoids ever? Or have you any other reason to celebrate? Here's the way to do it! A display of fireworks will be arranged solely for you by the station master right in front of the station. Just launch and enjoy your personal fireworks!, {available_to_all=true}], [99, 7500, Portable Fireworks, EQ_FIREWORKS_MINE, Enjoyed your fireworks? This is the portable version. Just carry one of these pylon-mounted devices with you and use it whenever you feel like celebrating! It is launched like a mine, has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}], [99, 5000, Fireworks Missile, EQ_FIREWORKS_MISSILE, The Fireworks Missile is a blank missile fitted with smaller blanks. It has no destructive power, and is used purely for displays of joy., {available_to_all=true, requires_empty_pylon=true}]]", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

}
