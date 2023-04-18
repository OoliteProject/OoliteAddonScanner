/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
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
    public void testGetVersion() {
        System.out.println("getVersion");
    }

    /**
     * Test of setVersion method, of class ExpansionManifest.
     */
    @Test
    public void testSetVersion() {
        System.out.println("setVersion");
    }

    /**
     * Test of getDescription method, of class ExpansionManifest.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
    }

    /**
     * Test of setDescription method, of class ExpansionManifest.
     */
    @Test
    public void testSetDescription() {
        System.out.println("setDescription");
    }

    /**
     * Test of getRequiresOxps method, of class ExpansionManifest.
     */
    @Test
    public void testGetRequiresOxps() {
        System.out.println("getRequiresOxps");
    }

    /**
     * Test of setRequiresOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetRequiresOxps() {
        System.out.println("setRequiresOxps");
    }

    /**
     * Test of getOptionalOxps method, of class ExpansionManifest.
     */
    @Test
    public void testGetOptionalOxps() {
        System.out.println("getOptionalOxps");
    }

    /**
     * Test of setOptionalOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetOptionalOxps() {
        System.out.println("setOptionalOxps");
    }

    /**
     * Test of getLicense method, of class ExpansionManifest.
     */
    @Test
    public void testGetLicense() {
        System.out.println("getLicense");
    }

    /**
     * Test of setLicense method, of class ExpansionManifest.
     */
    @Test
    public void testSetLicense() {
        System.out.println("setLicense");
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
    public void testGetInformationUrl() {
        System.out.println("getInformationUrl");
    }

    /**
     * Test of setInformationUrl method, of class ExpansionManifest.
     */
    @Test
    public void testSetInformationUrl() {
        System.out.println("setInformationUrl");
    }

    /**
     * Test of getTags method, of class ExpansionManifest.
     */
    @Test
    public void testGetTags() {
        System.out.println("getTags");
    }

    /**
     * Test of setTags method, of class ExpansionManifest.
     */
    @Test
    public void testSetTags() {
        System.out.println("setTags");
    }

    /**
     * Test of getFileSize method, of class ExpansionManifest.
     */
    @Test
    public void testGetFileSize() {
        System.out.println("getFileSize");
    }

    /**
     * Test of setFileSize method, of class ExpansionManifest.
     */
    @Test
    public void testSetFileSize() {
        System.out.println("setFileSize");
    }

    /**
     * Test of getDownloadUrl method, of class ExpansionManifest.
     */
    @Test
    public void testGetDownloadUrl() {
        System.out.println("getDownloadUrl");
    }

    /**
     * Test of setDownloadUrl method, of class ExpansionManifest.
     */
    @Test
    public void testSetDownloadUrl() {
        System.out.println("setDownloadUrl");
    }

    /**
     * Test of getConflictOxps method, of class ExpansionManifest.
     */
    @Test
    public void testGetConflictOxps() {
        System.out.println("getConflictOxps");
    }

    /**
     * Test of setConflictOxps method, of class ExpansionManifest.
     */
    @Test
    public void testSetConflictOxps() {
        System.out.println("setConflictOxps");
    }

    /**
     * Test of getMaximumOoliteVersion method, of class ExpansionManifest.
     */
    @Test
    public void testGetMaximumOoliteVersion() {
        System.out.println("getMaximumOoliteVersion");
    }

    /**
     * Test of setMaximumOoliteVersion method, of class ExpansionManifest.
     */
    @Test
    public void testSetMaximumOoliteVersion() {
        System.out.println("setMaximumOoliteVersion");
    }

    /**
     * Test of addWarning method, of class ExpansionManifest.
     */
    @Test
    public void testAddWarning() {
        System.out.println("addWarning");
    }

    /**
     * Test of getWarnings method, of class ExpansionManifest.
     */
    @Test
    public void testGetWarnings() {
        System.out.println("getWarnings");
    }

    /**
     * Test of hasWarnings method, of class ExpansionManifest.
     */
    @Test
    public void testHasWarnings() {
        System.out.println("hasWarnings");
    }
    
}
