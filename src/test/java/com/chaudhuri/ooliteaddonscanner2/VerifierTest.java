/*
 */
package com.chaudhuri.ooliteaddonscanner2;

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
    
    public void testFindDiffereringPosition() {
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
    void testVerify_Expansion() {
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    void testVerify_Equipment() {
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    void testVerify_Ship() {
    }

    /**
     * Test of verify method, of class Verifier.
     */
    @Test
    void testVerify_Registry() {
    }
    
}
