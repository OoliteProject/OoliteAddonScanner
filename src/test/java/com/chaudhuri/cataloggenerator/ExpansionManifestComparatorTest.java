/*
 */

package com.chaudhuri.cataloggenerator;

import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
 
/**
 *
 * @author hiran
 */
public class ExpansionManifestComparatorTest {
    private static final Logger log = LogManager.getLogger();

     public ExpansionManifestComparatorTest() {
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
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        ExpansionManifest em1 = null;
        ExpansionManifest em2 = null;
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(0, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare2() {
        System.out.println("compare2");
        ExpansionManifest em1 = new ExpansionManifest();
        ExpansionManifest em2 = null;
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare3() {
        System.out.println("compare3");
        ExpansionManifest em1 = null;
        ExpansionManifest em2 = new ExpansionManifest();
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(1, instance.compare(em1, em2));
    }

}