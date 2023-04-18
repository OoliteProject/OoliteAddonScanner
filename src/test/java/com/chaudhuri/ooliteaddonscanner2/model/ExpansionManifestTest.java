/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.List;
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
public class ExpansionManifestTest {
    
    public ExpansionManifestTest() {
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
     * Test of getAuthor method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetAuthor() {
        System.out.println("testSetGetAuthor");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getAuthor());
        manifest.setAuthor("some");
        assertEquals("some", manifest.getAuthor());
    }

    /**
     * Test of getCategory method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetCategory() {
        System.out.println("getSetCategory");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getCategory());
        manifest.setCategory("some");
        assertEquals("some", manifest.getCategory());
    }

    /**
     * Test of getIdentifier method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetIdentifier() {
        System.out.println("setGetIdentifier");

        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getIdentifier());
        manifest.setIdentifier("some");
        assertEquals("some", manifest.getIdentifier());
    }

    /**
     * Test of getTitle method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetTitle() {
        System.out.println("testSetGetTitle");

        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getTitle());
        manifest.setTitle("some");
        assertEquals("some", manifest.getTitle());
    }

    /**
     * Test of getVersion method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetVersion() {
        System.out.println("testSetGetVersion");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getVersion());
        
        manifest.setVersion("blah");
        assertEquals("blah", manifest.getVersion());
    }

    /**
     * Test of getDescription method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetDescription() {
        System.out.println("testSetGetDescription");

        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getDescription());
        
        manifest.setDescription("blah");
        assertEquals("blah", manifest.getDescription());
    }

    /**
     * Test of getRequiresOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetRequiresOxps() {
        System.out.println("testSetGetRequiresOxps");

        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getRequiresOxps());
        
        manifest.setRequiresOxps("blah");
        assertEquals("blah", manifest.getRequiresOxps());
    }

    /**
     * Test of getOptionalOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetOptionalOxps() {
        System.out.println("testSetGetOptionalOxps");

        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getOptionalOxps());
        
        manifest.setOptionalOxps("blah");
        assertEquals("blah", manifest.getOptionalOxps());
    }

    /**
     * Test of getLicense method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetLicense() {
        System.out.println("testSetGetLicense");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getLicense());
        
        manifest.setLicense("blah");
        assertEquals("blah", manifest.getLicense());
    }

    /**
     * Test of getRequiredOoliteVersion method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetRequiredOoliteVersion() {
        System.out.println("getRequiredOoliteVersion");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getRequiredOoliteVersion());
        
        manifest.setRequiredOoliteVersion("some");
        assertEquals("some", manifest.getRequiredOoliteVersion());
    }

    /**
     * Test of getInformationUrl method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetInformationUrl() {
        System.out.println("testSetGetInformationUrl");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getInformationUrl());
        
        manifest.setInformationUrl("some");
        assertEquals("some", manifest.getInformationUrl());
    }

    /**
     * Test of getTags method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetTags() {
        System.out.println("testSetGetTags");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getTags());
        
        manifest.setTags("some");
        assertEquals("some", manifest.getTags());
    }
    
    /**
     * Test of getFileSize method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetFileSize() {
        System.out.println("testSetGetFileSize");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getFileSize());
        
        manifest.setFileSize("some");
        assertEquals("some", manifest.getFileSize());
    }

    /**
     * Test of getDownloadUrl method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetDownloadUrl() {
        System.out.println("testSetGetDownloadUrl");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getDownloadUrl());
        
        manifest.setDownloadUrl("some");
        assertEquals("some", manifest.getDownloadUrl());
    }

    /**
     * Test of getConflictOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetConflictOxps() {
        System.out.println("testSetGetConflictOxps");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getConflictOxps());
        
        manifest.setConflictOxps("some");
        assertEquals("some", manifest.getConflictOxps());
    }

    /**
     * Test of getMaximumOoliteVersion method, of class ExpansionManifest.
     */
    @Test
    public void testSetGetMaximumOoliteVersion() {
        System.out.println("testSetGetMaximumOoliteVersion");
        
        ExpansionManifest manifest = new ExpansionManifest();
        assertNull(manifest.getMaximumOoliteVersion());
        
        manifest.setMaximumOoliteVersion("some");
        assertEquals("some", manifest.getMaximumOoliteVersion());
    }

    /**
     * Test of addWarning method, of class ExpansionManifest.
     */
    @Test
    public void testAddGetHasWarning() {
        System.out.println("testAddGetHasWarning");

        ExpansionManifest manifest = new ExpansionManifest();
        assertEquals(0, manifest.getWarnings().size());
        assertFalse(manifest.hasWarnings());
        
        manifest.addWarning("blah");
        assertEquals(1, manifest.getWarnings().size());
        assertEquals("blah", manifest.getWarnings().get(0));
        assertTrue(manifest.hasWarnings());
    }

}
