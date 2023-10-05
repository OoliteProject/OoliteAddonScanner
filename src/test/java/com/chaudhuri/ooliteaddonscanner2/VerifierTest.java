/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Equipment;
import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import com.chaudhuri.ooliteaddonscanner2.model.Ship;
import java.util.ArrayList;
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
public class VerifierTest {
    private static final Logger log = LogManager.getLogger();
    
    public VerifierTest() {
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
    public void testFindDiffereringPosition() {
        log.info("testFindDiffereringPosition");
        {
            String l1 = "";
            String l2 = "";
            String result = Verifier.findDiffereringPosition(l1, l2);

            assertEquals("no difference found", result);
        }

        {
            String l1 = "thirteen";
            String l2 = "thirty";
            String result = Verifier.findDiffereringPosition(l1, l2);

            assertEquals("at character position 0006 (LATIN SMALL LETTER E vs LATIN SMALL LETTER Y)", result);
        }

        {
            String l1 = "New background images all standard gui screens, plus new title screen theme music. Needs one of the Xenon UI Resources packs to be installed: Pack A, C, E, G, I, K or M for 16:9 screens, Pack B, D, F, H, J, L or N for 16:10 screens. Choose a pack that matches the font you are using - see resources pack details for information on which font is used. This OXP will override any backgrounds currently applied by other OXP's. This is by design, in order to maintain the illusion of looking at a computer display. If conflicts arise, where important information needs to be given to the player via a background screen, code can be applied to allow exceptions to take place. See the Wiki page for more information.";
            String l2 = "New background images all standard gui screens, plus new title screen theme music. Needs one of the Xenon UI Resources packs to be installed: Pack A, C, E, G, I, K or M for 16:9 screens, Pack B, D, F, H, J, L or N for 16:10 screens. Choose a pack that matches the font you are using - see resources pack details for information on which font is used. This OXP will override any backgrounds currently applied by other OXP's. This is by design, in order to maintain the illusion of looking at a computer display. If conflicts arise, where important information needs to be given to the player via a background screen, code can be applied to allow exceptions to take place. See the Wiki page for more information.";
            String result = Verifier.findDiffereringPosition(l1, l2);

            assertEquals("no difference found", result);
        }
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Expansion() {
        log.info("testVerify_Expansion");
        Expansion expansion = new Expansion();
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verify(expansion, null);
        assertEquals(0, expansion.getWarnings().size());
    }
        
    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Expansion2() {
        Expansion expansion = new Expansion();
        expansion.setDescription("one");
        ExpansionManifest manifest = new ExpansionManifest();
        manifest.setDescription("two");
        expansion.setManifest(manifest);

        assertEquals(0, expansion.getWarnings().size());
        Verifier.verify(expansion, null);
        assertEquals(1, expansion.getWarnings().size());
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Expansion3() {
        Expansion expansion = new Expansion();
        expansion.setIdentifier("one");
        ExpansionManifest manifest = new ExpansionManifest();
        manifest.setIdentifier("two");
        expansion.setManifest(manifest);

        assertEquals(0, expansion.getWarnings().size());
        Verifier.verify(expansion, null);
        assertEquals(1, expansion.getWarnings().size());
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Equipment() {
        log.info("testVerify_Equipment");
        {
            Equipment equipment = new Equipment();
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(0, equipment.getWarnings().size());
        }
        {
            Equipment equipment = new Equipment();
            equipment.putFeature("visible", "true");
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(0, equipment.getWarnings().size());
        }
        {
            Equipment equipment = new Equipment();
            equipment.putFeature("visible", "false");
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(0, equipment.getWarnings().size());
        }
        {
            Equipment equipment = new Equipment();
            equipment.putFeature("visible", "yes");
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(0, equipment.getWarnings().size());
        }
        {
            Equipment equipment = new Equipment();
            equipment.putFeature("visible", "no");
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(0, equipment.getWarnings().size());
        }
        {
            Equipment equipment = new Equipment();
            equipment.putFeature("visible", "wrong");
            assertEquals(0, equipment.getWarnings().size());
            Verifier.verify(equipment);
            assertEquals(1, equipment.getWarnings().size());
        }
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Ship() {
        log.info("testVerify_Ship");
        {
            Ship ship = new Ship();
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(0, ship.getWarnings().size());
        }
        {
            Ship ship = new Ship();
            ship.addFeature("is_template", "1");
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(0, ship.getWarnings().size());
        }
        {
            Ship ship = new Ship();
            ship.addFeature("is_template", "true");
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(0, ship.getWarnings().size());
        }
        {
            Ship ship = new Ship();
            ship.addFeature("is_template", "0");
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(0, ship.getWarnings().size());
        }
        {
            Ship ship = new Ship();
            ship.addFeature("is_template", "false");
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(0, ship.getWarnings().size());
        }
        {
            Ship ship = new Ship();
            ship.addFeature("is_template", "none");
            assertEquals(0, ship.getWarnings().size());
            Verifier.verify(ship);
            assertEquals(1, ship.getWarnings().size());
        }
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Registry() {
        log.info("testVerify_Registry");
        Registry registry = new Registry();
        assertEquals(0, registry.getWarnings().size());
        Verifier.verify(registry);
        assertEquals(0, registry.getWarnings().size());
    }
    
    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Registry2() { // test expansion
        log.info("testVerify_Registry2");
        Registry registry = new Registry();
        Expansion expansion = new Expansion("identifier");
        expansion.setTitle("title");
        expansion.setCategory("n");
        expansion.setAuthor("n");
        expansion.setVersion("n");
        expansion.setTags("n");
        expansion.setRequiredOoliteVersion("n");
        expansion.setMaximumOoliteVersion("n");
        expansion.setRequiresOxps(new ArrayList<>());
        expansion.setOptionalOxps(new ArrayList<>());
        expansion.setConflictOxps(new ArrayList<>());
        expansion.setInformationUrl("n");
        expansion.addScript("myscript1", "normal");
        expansion.addScript("myscript2", " fetch ");
        expansion.addScript("myscript3", " url ");
        expansion.addScript("myscript4", " xmlhttprequest ");
        registry.addExpansion(expansion);
        assertEquals(0, registry.getWarnings().size());
        
        Verifier.verify(registry);
        log.info("warnings: {}", registry.getWarnings());
        assertEquals(12, registry.getWarnings().size());
        assertEquals("Identifier mismatch between OXP Manifest and Expansion Manager at character position 0001 (LATIN SMALL LETTER I vs LATIN SMALL LETTER N)", registry.getWarnings().get(0));
        assertEquals("Title mismatch between OXP Manifest and Expansion Manager at character position 0001 (LATIN SMALL LETTER T vs LATIN SMALL LETTER N)", registry.getWarnings().get(1));
        assertEquals("Category mismatch between OXP Manifest and Expansion Manager string length at character position 1", registry.getWarnings().get(2));
        assertEquals("Author mismatch between OXP Manifest and Expansion Manager string length at character position 1", registry.getWarnings().get(3));
        assertEquals("Version mismatch between OXP Manifest and Expansion Manager string length at character position 1", registry.getWarnings().get(4));
        assertEquals("Tags mismatch between OXP Manifest and Expansion Manager compare with undefined value('n' vs 'null')", registry.getWarnings().get(5));
        assertEquals("Required Oolite Version mismatch between OXP Manifest and Expansion Manager string length at character position 1", registry.getWarnings().get(6));
        assertEquals("Maximum Oolite Version mismatch between OXP Manifest and Expansion Manager compare with undefined value('n' vs 'null')", registry.getWarnings().get(7));
        assertEquals("Information URL mismatch between OXP Manifest and Expansion Manager string length at character position 1", registry.getWarnings().get(8));
        assertEquals("JavaScript with fetch", registry.getWarnings().get(9));
        assertEquals("JavaScript with url", registry.getWarnings().get(10));
        assertEquals("JavaScript with xmlhttprequest", registry.getWarnings().get(11));
    }
    
    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Registry3() { // test equipment
        log.info("testVerify_Registry3");
        Registry registry = new Registry();
        Expansion expansion = new Expansion("identifier");
        registry.addExpansion(expansion);
        Equipment equipment = new Equipment("identifier");
        equipment.setExpansion(expansion);
        expansion.addEquipment(equipment);
        registry.addEquipment(equipment);
        assertEquals(0, registry.getWarnings().size());
        
        Verifier.verify(registry);
        log.info("warnings: {}", registry.getWarnings());
        assertEquals(1, registry.getWarnings().size());
        assertEquals("Identifier mismatch between OXP Manifest and Expansion Manager at character position 0001 (LATIN SMALL LETTER I vs LATIN SMALL LETTER N)", registry.getWarnings().get(0));
    }
    
    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    public void testVerify_Registry4() {
        log.info("testVerify_Registry3");
        Registry registry = new Registry();
        Expansion expansion = new Expansion("identifier");
        registry.addExpansion(expansion);
        Ship ship = new Ship("identifier");
        ship.setExpansion(expansion);
        expansion.addShip(ship);
        registry.addShip(ship);
        assertEquals(0, registry.getWarnings().size());
        
        Verifier.verify(registry);
        log.info("warnings: {}", registry.getWarnings());
        assertEquals(1, registry.getWarnings().size());
        assertEquals("Identifier mismatch between OXP Manifest and Expansion Manager at character position 0001 (LATIN SMALL LETTER I vs LATIN SMALL LETTER N)", registry.getWarnings().get(0));
    }
    
    @Test
    public void testDescribeCodePoint() {
        log.info("testDescribeCodePoint");
        assertEquals("NULL", Verifier.describeCodePoint(0));
        assertEquals("START OF HEADING", Verifier.describeCodePoint(1));
        assertEquals("START OF TEXT", Verifier.describeCodePoint(2));
        assertEquals("END OF TEXT", Verifier.describeCodePoint(3));
        assertEquals("END OF TRANSMISSION", Verifier.describeCodePoint(4));
        assertEquals("ENQUIRY", Verifier.describeCodePoint(5));
        assertEquals("ACKNOWLEDGE", Verifier.describeCodePoint(6));
        assertEquals("BEL", Verifier.describeCodePoint(7));
        assertEquals("BACKSPACE", Verifier.describeCodePoint(8));
        assertEquals("CHARACTER TABULATION", Verifier.describeCodePoint(9));
        assertEquals("LINE FEED (LF)", Verifier.describeCodePoint(10));
        assertEquals("LINE TABULATION", Verifier.describeCodePoint(11));
        assertEquals("FORM FEED (FF)", Verifier.describeCodePoint(12));
        assertEquals("CARRIAGE RETURN (CR)", Verifier.describeCodePoint(13));
        assertEquals("SHIFT OUT", Verifier.describeCodePoint(14));
        assertEquals("SHIFT IN", Verifier.describeCodePoint(15));
    }

    @Test
    public void testDescribeCodePoint2() {
        log.info("testDescribeCodePoint2");
        assertEquals("DATA LINK ESCAPE", Verifier.describeCodePoint(16));
        assertEquals("DEVICE CONTROL ONE", Verifier.describeCodePoint(17));
        assertEquals("DEVICE CONTROL TWO", Verifier.describeCodePoint(18));
        assertEquals("DEVICE CONTROL THREE", Verifier.describeCodePoint(19));
        assertEquals("DEVICE CONTROL FOUR", Verifier.describeCodePoint(20));
        assertEquals("NEGATIVE ACKNOWLEDGE", Verifier.describeCodePoint(21));
        assertEquals("SYNCHRONOUS IDLE", Verifier.describeCodePoint(22));
        assertEquals("END OF TRANSMISSION BLOCK", Verifier.describeCodePoint(23));
        assertEquals("CANCEL", Verifier.describeCodePoint(24));
        assertEquals("END OF MEDIUM", Verifier.describeCodePoint(25));
        assertEquals("SUBSTITUTE", Verifier.describeCodePoint(26));
        assertEquals("ESCAPE", Verifier.describeCodePoint(27));
        assertEquals("INFORMATION SEPARATOR FOUR", Verifier.describeCodePoint(28));
        assertEquals("INFORMATION SEPARATOR THREE", Verifier.describeCodePoint(29));
        assertEquals("INFORMATION SEPARATOR TWO", Verifier.describeCodePoint(30));
        assertEquals("INFORMATION SEPARATOR ONE", Verifier.describeCodePoint(31));
        assertEquals("SPACE", Verifier.describeCodePoint(32));
        assertEquals("EXCLAMATION MARK", Verifier.describeCodePoint(33));
        assertEquals("QUOTATION MARK", Verifier.describeCodePoint(34));
    }
    
    @Test
    public void testVerifyDependency() {
        log.info("testVerifyDependency");
        
        try {
            Verifier.verifyDependency(null, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("dependency must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }
    
    @Test
    public void testVerifyDependency2() {
        log.info("testVerifyDependency2");
        
        Expansion.Dependency dependency = new Expansion.Dependency();
        try {
            Verifier.verifyDependency(dependency, null, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("expansion must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }
    
    @Test
    public void testVerifyDependency3() {
        log.info("testVerifyDependency3");
        
        Expansion.Dependency dependency = new Expansion.Dependency();
        Expansion expansion = new Expansion();
        try {
            Verifier.verifyDependency(dependency, expansion, null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("registry must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }
    
    @Test
    public void testVerifyDependency4() {
        log.info("testVerifyDependency4");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion();
        Expansion.Dependency dependency = new Expansion.Dependency();
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("No version in dependency reference to null:null", expansion.getWarnings().get(0));
        
    }
    
    @Test
    public void testVerifyDependency5() {
        log.info("testVerifyDependency5");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("No version in dependency reference to null:null", expansion.getWarnings().get(0));
        
    }
    
    @Test
    public void testVerifyDependency6() {
        log.info("testVerifyDependency6");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("No version in dependency reference to newExpansion:null", expansion.getWarnings().get(0));
        
    }
    
    @Test
    public void testVerifyDependency7() {
        log.info("testVerifyDependency7");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        dependency.setVersion("0");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(0, expansion.getWarnings().size());
        
    }
    
    @Test
    public void testVerifyDependency8() {
        log.info("testVerifyDependency8");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        expansion.setVersion("1.0.0");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        dependency.setVersion("0");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(0, expansion.getWarnings().size());
        
    }

    @Test
    public void testVerifyDependency9() {
        log.info("testVerifyDependency9");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        expansion.setVersion("1.0.0");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        dependency.setVersion("1.0.0");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(0, expansion.getWarnings().size());
    }

    @Test
    public void testVerifyDependency10() {
        log.info("testVerifyDependency10");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("newExpansion");
        expansion.setVersion("1.0.0");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        dependency.setVersion("1.0.1");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("Unresolved dependency reference to newExpansion:1.0.1", expansion.getWarnings().get(0));
    }

    @Test
    public void testVerifyDependency11() {
        log.info("testVerifyDependency11");
        
        Registry registry = new Registry();
        Expansion expansion = new Expansion("otherExpansion");
        expansion.setVersion("1.0.0");
        registry.addExpansion(expansion);
        Expansion.Dependency dependency = new Expansion.Dependency();
        dependency.setIdentifier("newExpansion");
        dependency.setVersion("1.0.0");
        
        assertEquals(0, expansion.getWarnings().size());
        Verifier.verifyDependency(dependency, expansion, registry);
        assertEquals(1, expansion.getWarnings().size());
        assertEquals("Unresolved dependency reference to newExpansion:1.0.0", expansion.getWarnings().get(0));
    }
}
