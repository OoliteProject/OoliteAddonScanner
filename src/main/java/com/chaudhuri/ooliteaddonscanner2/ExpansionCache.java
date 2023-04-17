/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.owlike.genson.Genson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class ExpansionCache {
    private static final Logger log = LogManager.getLogger(ExpansionCache.class);
    
    protected static File cacheDIR = new File(System.getProperty("user.home")+"/.Oolite/expansion_cache");
    
    /** Time after which we try to update the cache entry. */
    private static final long MAX_AGE = 30L * 86400L * 1000L; // 7 days ago
    
    /** Time after which we remove files from the cache. */
    private static final Instant THRESHOLD = Instant.now().minus(180, ChronoUnit.DAYS);
    
    public ExpansionCache() {
        if (!cacheDIR.exists()) {
            log.info("Creating cache directory {}", cacheDIR);
            cacheDIR.mkdirs();
        }
        
        // on startup clean too old files
        try {
            cleanCache(cacheDIR);
        } catch (IOException e) {
            log.warn("Could not cleanup cache.", e);
        }
    }
    
    private void deleteIfOlderThanTHRESHOLD(File f) throws IOException {
        Instant lastModified = Instant.ofEpochMilli(f.lastModified());

        BasicFileAttributes attrs = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        FileTime time = attrs.lastAccessTime();
        Instant lastAccessed = time.toInstant();

        if (lastModified.isBefore(THRESHOLD) && lastAccessed.isBefore(THRESHOLD)) {

            Files.delete(f.toPath());
        }
    }

    /** Removed files that have been last accessed before THRESHOLD.
     * Also removes empty subdirectories.
     * 
     * @param dir
     * @throws IOException 
     */
    private void cleanCache(File dir) throws IOException {
        if (dir == null) {
            throw new IllegalArgumentException("dir must not be null");
        }

        for (File f: dir.listFiles()) {
            if (".".equals(f.getName()) || "..".equals(f.getName())) {
                continue;
            }
            
            if (f.isFile()) {
                deleteIfOlderThanTHRESHOLD(f);
            } else if (f.isDirectory()) {
                cleanCache(f);
            }
        }
        
        if (!dir.getAbsolutePath().equals(cacheDIR.getAbsolutePath()) && dir.listFiles().length <= 2) {
            log.warn("Remove empty directory {}", dir.getAbsolutePath());
            Files.delete(dir.toPath());
        }
    }
    
    public Map<String, Object> getOoliteManifest(String tag) throws IOException {
        log.debug("getOoliteManifest({})", tag);
        String repository = "OoliteProject/oolite";
        String urlStr = "https://api.github.com/repos/" + repository + "/releases/" + tag;

        log.debug("Reading {}", urlStr);
        URL url = new URL(urlStr);
        try ( InputStream in = url.openStream()) {
            return (Map<String, Object>) new Genson().deserialize(url.openStream(), Object.class);
        }
    }
    
    /** Returns the download url for the latest Oolite release
     * 
     */
    public String getOoliteDownloadUrl(Map<String, Object> manifest) {
        log.debug("getOoliteDownloadUrl({})", manifest);

        Pattern pattern = Pattern.compile("oolite-\\d+\\.\\d+\\.zip");
        
        List<Object> assets = (List<Object>)manifest.get("assets");
        for (Object oasset: assets) {
            Map<String, Object> asset = (Map<String, Object>)oasset;
            log.trace("asset: {}: {}", asset.getClass(), asset);

            String name = String.valueOf(asset.get("name"));
            if (pattern.matcher(name).matches()) {
                String assetUrlStr = String.valueOf(asset.get("browser_download_url"));
                log.trace("selected asset {} from {}", name, assetUrlStr);
                return assetUrlStr;
            }
        }
        throw new IllegalStateException("Could not find file matching "+pattern.pattern());
    }
    
    private File getCachedFile(String url) throws MalformedURLException {
        URL u = new URL(url);
        return new File(cacheDIR, u.getHost() + File.separator + u.getFile());
    }
    
    public void update(List<String> urls) throws IOException {
        int count = 0;
        for (String u: urls) {
            update(u);
            count++;
            log.info("Downloaded {}/{}", count, urls.size());
        }
    }
    
    private void doDownload(URL u, File local) throws IOException {
        log.info("Downloading to {}", local);
        
        try {

            File parent = local.getParentFile();
            if(!parent.exists()) {
                parent.mkdirs();
            }

            HttpURLConnection conn = (HttpURLConnection)u.openConnection();
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            log.info("HTTP status for {}: {}", u, status);

            while (status != HttpURLConnection.HTTP_OK) {
                String newUrl = conn.getHeaderField("Location");
                conn = (HttpURLConnection)new URL(newUrl).openConnection();
                conn.setReadTimeout(5000);
                status = conn.getResponseCode();
                log.info("HTTP status for {}: {}", newUrl, status);
            }

            try ( InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(local) ) {
                byte[] buffer = new byte[8192];
                int read = in.read(buffer);
                long bytecount = 0;

                while (read >= 0) {
                    bytecount += read;
                    out.write(buffer, 0, read);
                    read = in.read(buffer);
                }

                log.debug("Downloaded {} bytes", bytecount);
            }
        } catch (Exception e) {
            throw new IOException("Could not download "+u, e);
        }
    }
    
    /**

     * Headers for URL http://wiki.alioth.net/img_auth.php/6/68/DTT_Atlas_1.1.oxz:
     *   Keep-Alive=[timeout=5, max=100], 
     *   null=[HTTP/1.1 200 OK], 
     *   Server=[Apache/2.4.38 (Debian)], 
     *   X-Content-Type-Options=[nosniff], 
     *   Connection=[Keep-Alive], 
     *   Last-Modified=[Sun, 17 Jan 2021 08:48:44 GMT], 
     *   Content-Length=[5595563], 
     *   Date=[Wed, 09 Jun 2021 06:37:05 GMT], 
     *   Content-Type=[application/zip]
     * 
     * @param u
     * @throws IOException 
     */
    private Date doCheckLastModified(URL u, int followRedirectsCount) throws IOException {
        HttpURLConnection con = (HttpURLConnection)u.openConnection();
        con.setRequestMethod("HEAD");
        con.connect();
        Map<String, List<String>> headers = con.getHeaderFields();
        
        // ensure we have a http status 200
        if (200 <= con.getResponseCode() && con.getResponseCode() < 300) {
            // then parse Last-Modified date
            return new Date(con.getLastModified());
        } else if (300 <= con.getResponseCode() && con.getResponseCode() < 400) {
            log.debug("Redirect {} with {}", u, headers);
            if (followRedirectsCount == 0)
                throw new IllegalStateException("Received redirect but cannot follow");
            return doCheckLastModified(new URL(headers.get("Location").get(0)), followRedirectsCount - 1);
        } else {
            throw new IOException("HEAD " + u + " resulted in "+con.getResponseCode() + " " + con.getResponseMessage());
        }
    }
    
    /** Ensure we have the resource  in the cache and it is recent enough.
     * 
     * @param url
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void update(String url) throws IOException {
        log.debug("update({})", url);
        
        URL u = new URL(url);
        File localFile = getCachedFile(url);
        
        long age = System.currentTimeMillis() - localFile.lastModified();
        
        if (localFile.exists() && (age < MAX_AGE) ) {
            // perform check if there is a newer version online
            Date online = doCheckLastModified(u, 5);
            Date local = new Date(localFile.lastModified());
            if (local.after(online)) {            
                log.debug("Already in cache: {}", localFile);
                return;
            }
        }

        doDownload(u, localFile);
    }
    
    public InputStream getPluginInputStream(String url) throws IOException {
        log.debug("getPluginInputStream({})", url);
        
        update(url);

        File cached = getCachedFile(url);
        return new FileInputStream(cached);
    }
    
    /** Removes the cached file.
     * 
     * @param url
     * @throws MalformedURLException 
     */
    public void invalidate(String url) throws IOException {
        log.debug("invalidate({})", url);
        File cached = getCachedFile(url);
        Files.delete(cached.toPath());
    }
}
