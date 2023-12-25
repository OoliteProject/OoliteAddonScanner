/*
 */

package com.chaudhuri.cataloggenerator;

import com.chaudhuri.ooliteaddonscanner2.model.ExpansionManifest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipInputStream;
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
public class GeneratorTest {
    private static final Logger log = LogManager.getLogger();

     public GeneratorTest() {
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
     * Test of setter and getter for InputFile.
     */
    @Test
    public void testSetGetInputFile() {
        log.info("testSetGetInputFile");

        Generator generator = new Generator();
        assertNull(generator.getInputPath());
        generator.setInputPath(FileSystems.getDefault().getPath("src","test", "data", "expansionUrls.txt"));
        assertNotNull(generator.getInputPath());
        generator.setInputPath(null);
        assertNull(generator.getInputPath());
    }

    /**
     * Test of setter and getter for OutputFile.
     */
    @Test
    public void testSetGetOutputFile() {
        log.info("testSetGetOutputFile");

        Generator generator = new Generator();
        assertNull(generator.getOutputPath());
        generator.setOutputPath(FileSystems.getDefault().getPath("target","test", "data", "manifest.plist"));
        assertNotNull(generator.getOutputPath());
        generator.setOutputPath(null);
        assertNull(generator.getOutputPath());
    }

    /**
     * Test of setter and getter for OutputFormat.
     */
    @Test
    public void testSetGetOutputFormat() {
        System.out.println("testSetGetOutputFormat");

        Generator generator = new Generator();
        assertNull(generator.getOutputFormat());
        generator.setOutputFormat("xyz");
        assertNotNull(generator.getOutputFormat());
        generator.setOutputFormat(null);
        assertNull(generator.getOutputFormat());
    }

    /**
     * Test of setter and getter for TempDir.
     */
    @Test
    public void testSetGetTempDir() {
        log.info("testSetGetTempDir");

        Generator generator = new Generator();
        assertNull(generator.getTempDir());
        generator.setTempDir(new File(".").toPath());
        assertNotNull(generator.getTempDir());
        generator.setTempDir(null);
        assertNull(generator.getTempDir());
    }

    /**
     * Test of setter and getter for ThreadCount.
     */
    @Test
    public void testSetGetThreadCount() {
        log.info("testSetGetThreadCount");
        
        Generator generator = new Generator();
        assertEquals(0, generator.getThreadCount());
        generator.setThreadCount(5);
        assertEquals(5, generator.getThreadCount());
        generator.setThreadCount(0);
        assertEquals(0, generator.getThreadCount());
    }

    /**
     * Test of run method, of class Generator.
     */
    @Test
    public void testCall() throws IOException {
        log.info("testCall");
        Generator instance = new Generator();
        try {
            instance.call();
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("inputPath must not be null.", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }

    /**
     * Test of run method, of class Generator.
     */
    @Test
    public void testCall2() throws IOException {
        log.info("testCall2()");
        
        Generator instance = new Generator();
        instance.setInputPath(FileSystems.getDefault().getPath("src","test", "data", "expansionUrls.txt"));

        try {
            instance.call();
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("outputFormat must not be null.", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of run method, of class Generator.
     */
    @Test
    public void testCall3() throws IOException {
        log.info("testCall3()");
        
        Generator instance = new Generator();
        instance.setInputPath(FileSystems.getDefault().getPath("src","test", "data", "expansionUrls.txt"));
        instance.setOutputFormat("xml");

        try {
            instance.call();
            fail("expected exception");
        } catch (IllegalStateException e) {
            assertEquals("outputPath must not be null.", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    /**
     * Test of run method, of class Generator.
     */
    @Test
    public void testCall4() throws IOException {
        log.info("testCall4()");
        
        Generator instance = new Generator();
        instance.setInputPath(FileSystems.getDefault().getPath("src","test", "data", "expansionUrls.txt"));
        instance.setOutputFormat("xml,json,html,plist");
        Path outputPath = FileSystems.getDefault().getPath("target","test", "data", "catalog.xml");
        instance.setOutputPath(outputPath);

        instance.call();
        
        Path outputPathJson = outputPath.resolveSibling("catalog.xml.json");
        Path outputPathHtml = outputPath.resolveSibling("catalog.xml.html");
        Path outputPathPlist = outputPath.resolveSibling("catalog.xml.plist");
        
        assertTrue(Files.exists(outputPath, LinkOption.NOFOLLOW_LINKS), "Output file not found at " + outputPath.toAbsolutePath());
        assertTrue(Files.exists(outputPathJson, LinkOption.NOFOLLOW_LINKS), "Output file not found at " + outputPathJson.toAbsolutePath());
        assertTrue(Files.exists(outputPathHtml, LinkOption.NOFOLLOW_LINKS), "Output file not found at " + outputPathHtml.toAbsolutePath());
        assertTrue(Files.exists(outputPathPlist, LinkOption.NOFOLLOW_LINKS), "Output file not found at " + outputPathPlist.toAbsolutePath());
    }
    
    @Test
    public void testGetManifestFromUrl() {
        log.info("testGetManifestFromUrl");
        
        Generator instance = new Generator();
        instance.init();
        ExpansionManifest em = instance.getManifestFromUrl(null);
        assertNull(em);
    }
    
    @Test
    public void testGetManifestFromUrl2() {
        log.info("testGetManifestFromUrl2");
        
        Generator instance = new Generator();
        instance.init();
        File f = new File("src/test/data/ThargornThreat_1.5.2.oxz");
        ExpansionManifest em = instance.getManifestFromUrl(f.toURI().toString());
        assertNotNull(em);
    }

    @Test
    public void testGetManifestFromOXZ() throws FileNotFoundException {
        log.info("testGetManifestFromOXZ");
        
        Generator instance = new Generator();
        File f = new File("src/test/data/ThargornThreat_1.5.2.oxz");
        ZipInputStream zin = new ZipInputStream(new FileInputStream(f));
        
        ExpansionManifest em = instance.getManifestFromOXZ(zin, f.toString());
        
        assertNotNull(em);
        assertEquals("oolite.oxp.ArexackHeretic.ThargornThreat", em.getIdentifier());
        assertEquals("1.5.2", em.getVersion());
        assertEquals("Thargorn Threat", em.getTitle());
    }
    
    @Test
    public void testIsDistinct() {
        log.info("testIsDistinct");
        
        Generator instance = new Generator();
        try {
            instance.isDistinct(null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("list must not be null", e.getMessage());
            log.debug("caught expected exception", e);
        }
    }
    
    @Test
    public void testIsDistinct1() {
        log.info("testIsDistinct1");
        
        List<String> urls = Arrays.asList(new String[]{});
        
        Generator instance = new Generator();
        instance.isDistinct(urls);
    }
    
    @Test
    public void testIsDistinct2() {
        log.info("testIsDistinct2");
        
        List<String> urls = Arrays.asList(new String[]{"a", "ab", "b"});
        
        Generator instance = new Generator();
        assertTrue(instance.isDistinct(urls));
    }
    
    @Test
    public void testIsDistinct3() {
        log.info("testIsDistinct3");
        
        List<String> urls = Arrays.asList(new String[]{"a", "ab", "b", "ab"});
        
        Generator instance = new Generator();
        assertFalse(instance.isDistinct(urls));
    }
}