/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.io.FileUtils;
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
public class ExpansionCacheTest {
    private static final Logger log = LogManager.getLogger();
    
    private static File tempCacheDir;
    
    private static final String testDownload = "https://dlcdn.apache.org//commons/cli/binaries/commons-cli-1.7.0-bin.tar.gz";
    private static final String testDownloadPath = "dlcdn.apache.org//commons/cli/binaries/commons-cli-1.7.0-bin.tar.gz";
    
    public ExpansionCacheTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        log.debug("setUpClass()");

        TreeMap<String, String> props = new TreeMap(System.getProperties());
        for (String key: props.keySet()) {
            log.info("{}->{}", key, System.getProperty(key));
        }
        log.info("cwd: {}", new File(".").getAbsolutePath());

        tempCacheDir = new File("target/testCacheDir");
        tempCacheDir.mkdirs();
    }
    
    @AfterAll
    public static void tearDownClass() throws IOException {
        log.debug("tearDownClass()");
        
        if (tempCacheDir != null) {
            FileUtils.deleteDirectory(tempCacheDir);
        }
    }
    
    @BeforeEach
    public void setUp() {
        log.debug("setUp()");
    }
    
    @AfterEach
    public void tearDown() {
        log.debug("tearDown()");
    }
    
    @Test
    public void testCreateExpansionCache() throws IOException {
        log.info("testCreateExpansionCache");

        File testDir = new File("target/test/ExpansionCacheTest");
        testDir.mkdirs();
        
        File cacheDir = File.createTempFile("expansionCache", ".dir", testDir);
        cacheDir.delete();
        
        assertFalse(cacheDir.exists());
        ExpansionCache cache = new ExpansionCache(cacheDir);
        assertTrue(cacheDir.exists());
    }

    /**
     * Test of getOoliteManifest method, of class ExpansionCache.
     */
    @Test
    public void testGetOoliteManifest() throws Exception {
        log.info("testGetOoliteManifest");

        File testDir = new File("target/test/ExpansionCacheTest");
        testDir.mkdirs();

        ExpansionCache cache = new ExpansionCache(testDir);
        cache.setBaseUrl(new File("src/test/data/").toURI().toURL().toString());
        Map<String, Object> m = cache.getOoliteManifest("latest");
        assertNotNull(m);
        assertEquals(18, m.size());
        assertNull(m.get("blah"));

        // this includes testGetOoliteDownloadUrl()
        String url = cache.getOoliteDownloadUrl(m);
        assertEquals("https://github.com/OoliteProject/oolite/releases/download/1.90/oolite-1.90.zip", url);

        log.debug("map has keys {}", m.keySet());
        /*
        created_at, body, url, assets_url, assets, prerelease, html_url, target_commitish, draft, zipball_url, name, upload_url, id, published_at, tarball_url, node_id
        */
        assertEquals("java.util.HashMap", m.get("author").getClass().getName());
        assertEquals("1.90", m.get("tag_name"));
    }

    /**
     * Test of update method, of class ExpansionCache.
     */
    @Test
    public void testUpdate_List() throws Exception {
        log.info("update");
        
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);
        List<String> urls = new ArrayList<>();
        urls.add(testDownload);


        assertFalse(downloaded.exists(), "Unexpected file " + downloaded.getAbsolutePath());
        cache.update(urls);
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        
        // now check if we can update the file
        downloaded.setLastModified(Instant.now().minus(300, ChronoUnit.DAYS).getEpochSecond());
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        cache.update(urls);
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        Instant lastModified = Instant.ofEpochMilli(downloaded.lastModified());
        assertTrue(Duration.between(lastModified, Instant.now()).toSeconds() < 2);
    }

    /**
     * Test of update method, of class ExpansionCache.
     */
    @Test
    public void testUpdate_String() throws Exception {
        log.info("testUpdate_String2");
        
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);

        try {
            cache.update((String)null);
        } catch (IllegalArgumentException e) {
            assertEquals("url must not be null", e.getMessage());
            log.debug("Caught expected exception", e);
        }
    }
    
    /**
     * Test of update method, of class ExpansionCache.
     */
    @Test
    public void testUpdate_String2() throws Exception {
        log.info("testUpdate_String2");

        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);

        assertFalse(downloaded.exists());
        cache.update(testDownload);
        assertTrue(downloaded.exists());

        // now check if we can update the file.
        // TODO: We may want to place an absolute date here
        downloaded.setLastModified(Instant.parse("2021-10-27T11:47:30.00Z").minus(700, ChronoUnit.DAYS).toEpochMilli());
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        cache.update(testDownload);
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        Instant lastModified = Instant.ofEpochMilli(downloaded.lastModified());
        Duration age = Duration.between(lastModified, Instant.now());
        assertTrue(age.toSeconds() < 10, String.format("File %s has age %s, we expected it to be younger", downloaded, age));
        
        // try to download again - it should not happen
        cache.update(testDownload);
        assertTrue(downloaded.exists(), "Expected file " + downloaded.getAbsolutePath());
        Instant lastModified2 = Instant.ofEpochMilli(downloaded.lastModified());
        age = Duration.between(lastModified, lastModified2);
        assertTrue(age.toSeconds() < 3, String.format("File age differrence is %s. File %s was expected not to be downloaded again", age, downloaded));
        
    }

    /**
     * Test of getPluginInputStream method, of class ExpansionCache.
     */
    @Test
    public void testGetPluginInputStream() throws Exception {
        log.info("getPluginInputStream");

        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);
        assertFalse(downloaded.exists());
        // the file will be downloaded implicitly
        assertNotNull(cache.getPluginInputStream(testDownload));
        assertTrue(downloaded.exists());
        
    }

    /**
     * Test of invalidate method, of class ExpansionCache.
     */
    @Test
    public void testInvalidate() throws Exception {
        log.info("invalidate");

        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);
        assertFalse(downloaded.exists());
        // the file will be downloaded implicitly
        assertNotNull(cache.getPluginInputStream(testDownload));
        assertTrue(downloaded.exists());
        
        cache.invalidate(testDownload);
        assertFalse(downloaded.exists());
    }
    
    @Test
    public void testCleanCache() throws IOException {
        log.info("testCleanCache");
        
        // create cache structure with young and old file
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();
        log.info("cache in {} because of {}", testCache, tempCacheDir);

        File oldfile = new File(testCache, "server1/old.file");
        Files.createDirectories(oldfile.getParentFile().toPath());
        Files.createFile(oldfile.toPath());
        oldfile.setLastModified(Instant.now().minus(300, ChronoUnit.DAYS).toEpochMilli());
        
        File youngfile = new File(testCache, "server2/downloads/young.file");
        Files.createDirectories(youngfile.getParentFile().toPath());
        Files.createFile(youngfile.toPath());
        youngfile.setLastModified(Instant.now().minus(10, ChronoUnit.DAYS).toEpochMilli());
        
        File emptyDir = new File(testCache, "server3/downloads");
        Files.createDirectories(emptyDir.toPath());
        
        // check that both files are there
        assertTrue(oldfile.isFile());
        assertTrue(youngfile.isFile());
        assertTrue(emptyDir.isDirectory());

        // instantiate ExpansionCache - it will purge on startup
        ExpansionCache cache = new ExpansionCache(testCache);
        
        // check that only the young file is left
        assertFalse(oldfile.isFile(), "old file should be cleaned up but is not");
        assertTrue(youngfile.isFile(), "young file should still exist but does not");
        assertFalse(emptyDir.isDirectory(), "emtpy directory should have been cleaned away but is not");
    }
    
    @Test
    public void testGetLastModified() throws IOException {
        log.info("testGetLastModified");
        
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        ExpansionCache cache = new ExpansionCache(testCache);
        
        try {
            cache.getLastModified(null);
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertEquals("url must not be null", e.getMessage());
            log.debug("caught expected exception");
        }
    }
    
    @Test
    public void testGetLastModified2() throws IOException {
        log.info("testGetLastModified2");
        
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        ExpansionCache cache = new ExpansionCache(testCache);
        
        try {
            cache.getLastModified("hallo");
            fail("expected exception");
        } catch (MalformedURLException e) {
            assertEquals("no protocol: hallo", e.getMessage());
            log.debug("caught expected exception");
        }
    }
    
    @Test
    public void testGetLastModified3() throws IOException {
        log.info("testGetLastModified3");
        
        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        ExpansionCache cache = new ExpansionCache(testCache);
        
        Instant i = cache.getLastModified("https://cim.sotl.org.uk/games/files/oolite/Escort_Formations_1.1.oxz");
        assertEquals("2014-04-12T15:46:23Z", String.valueOf(i));
    }
}
