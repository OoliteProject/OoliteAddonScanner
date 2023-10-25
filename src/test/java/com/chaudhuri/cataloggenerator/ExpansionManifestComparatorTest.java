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
        log.info("testCompare");
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
        log.info("testCompare2");
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
        log.info("testCompare3");
        ExpansionManifest em1 = null;
        ExpansionManifest em2 = new ExpansionManifest();
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare4() {
        log.info("testCompare4");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        ExpansionManifest em2 = null;
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare5() {
        log.info("testCompare5");
        ExpansionManifest em1 = new ExpansionManifest();
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare6() {
        log.info("testCompare6");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(0, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare7() {
        log.info("testCompare7");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare8() {
        log.info("testCompare8");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("1");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(56, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare9() {
        log.info("testCompare9");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare10() {
        log.info("testCompare10");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        em1.setDownloadUrl("uri");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare11() {
        log.info("testCompare11");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        em2.setDownloadUrl("uri");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare12() {
        log.info("testCompare12");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        em1.setDownloadUrl("uri");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        em2.setDownloadUrl("uri");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(0, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare13() {
        log.info("testCompare13");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        em1.setDownloadUrl("uri1");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        em2.setDownloadUrl("uri2");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(-1, instance.compare(em1, em2));
    }

    /**
     * Test of compare method, of class ExpansionManifestComparator.
     */
    @Test
    public void testCompare14() {
        log.info("testCompare14");
        ExpansionManifest em1 = new ExpansionManifest();
        em1.setIdentifier("id");
        em1.setVersion("1");
        em1.setDownloadUrl("uri2");
        ExpansionManifest em2 = new ExpansionManifest();
        em2.setIdentifier("id");
        em2.setVersion("1");
        em2.setDownloadUrl("uri1");
        ExpansionManifestComparator instance = new ExpansionManifestComparator();
        assertEquals(1, instance.compare(em1, em2));
    }
}