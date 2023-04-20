/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private static final String testDownload = "https://dlcdn.apache.org//commons/cli/binaries/commons-cli-1.5.0-bin.tar.gz";
    private static final String testDownloadPath = "dlcdn.apache.org//commons/cli/binaries/commons-cli-1.5.0-bin.tar.gz";
    
    public ExpansionCacheTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        TreeMap<String, String> props = new TreeMap(System.getProperties());
        for (String key: props.keySet()) {
            log.info("{}->{}", key, System.getProperty(key));
        }
        log.info("cwd: {}", new File(".").getAbsolutePath());

        tempCacheDir = new File("target/testCacheDir");
        tempCacheDir.mkdirs();
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
    public void testCreateExpansionCache() throws IOException {
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
        System.out.println("getOoliteManifest");
        
        ExpansionCache cache = new ExpansionCache();
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
        System.out.println("update");
        
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
    }

    /**
     * Test of update method, of class ExpansionCache.
     */
    @Test
    public void testUpdate_String() throws Exception {
        log.info("testUpdate_String");

        File testCache = File.createTempFile("testCache", ".dir", tempCacheDir);
        testCache.delete();
        testCache.mkdirs();

        File downloaded = new File(testCache, testDownloadPath);
        
        ExpansionCache cache = new ExpansionCache(testCache);

        assertFalse(downloaded.exists());
        cache.update(testDownload);
        assertTrue(downloaded.exists());
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
        System.out.println("invalidate");

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
    
}
