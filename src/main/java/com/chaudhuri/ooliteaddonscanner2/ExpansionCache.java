/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.owlike.genson.Genson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author hiran
 */
public class ExpansionCache {
    private static final Logger log = LogManager.getLogger(ExpansionCache.class);

    public static final File DEFAULT_CACHE_DIR = new File(System.getProperty("user.home")+"/.Oolite/expansion_cache");
    
    protected File cacheDIR;
    
    /** Time after which we try to update the cache entry. 
     */
    private static final Duration MAX_AGE = Duration.parse("P7d"); // 7 days ago
    
    /** Time after which we remove files from the cache. */
    private static final Instant THRESHOLD = Instant.now().minus(180, ChronoUnit.DAYS);
    
    /** Base url to download releases from. Can be overridden for unit tests. */
    private String baseUrl = "https://api.github.com/repos";
    
    /**
     * Creates a new ExpansionCache.
     */
    public ExpansionCache() {
        this(DEFAULT_CACHE_DIR);
    }
    
    /**
     * Creates a new ExpansionCache.
     */
    public ExpansionCache(File cacheDir) {
        cacheDIR = cacheDir;
        
        if (!cacheDIR.exists()) {
            log.info("Creating cache directory {}", cacheDIR);
            cacheDIR.mkdirs();
        }
        
        // on startup clean too old files
        try {
            cleanCache(cacheDIR);
        } catch (IOException e) {
            log.info("Could not cleanup cache.", e);
        }
    }
    
    /**
     * Purges old files from the cache folder.
     * 
     * @param f the cache folder
     * @throws IOException something went wrong
     */
    private void deleteIfOlderThanTHRESHOLD(File f) throws IOException {
        log.debug("deleteIfOlderThanTHRESHOLD({})", f);
        
        Instant lastModified = Instant.ofEpochMilli(f.lastModified());

        BasicFileAttributes attrs = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
        FileTime time = attrs.lastModifiedTime();
        Instant lastAccessed = time.toInstant();

        if (lastModified.isBefore(THRESHOLD) && lastAccessed.isBefore(THRESHOLD)) {
            log.trace("deleting file {}", f);
            Files.delete(f.toPath());
        }
    }

    /** 
     * Removes files that have been last accessed before THRESHOLD.
     * Also removes empty subdirectories.
     * 
     * @param dir the directory to clean
     * @throws IOException 
     */
    private void cleanCache(File dir) throws IOException {
        log.debug("cleanCache({})", dir);
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
            log.info("Remove empty directory {}", dir.getAbsolutePath());
            try {
                Files.delete(dir.toPath());
            } catch (DirectoryNotEmptyException e) {
                log.info("Could not delete {} due to contents: {}", dir, dir.listFiles(), e);
            }
        }
    }
    
    /**
     * Downloads a manifest from the github Oolite repository and returns
     * the parsed JSON.
     * 
     * @param tag the tag to search for
     * @return the manifest found, as it is returned by Genson
     * @throws IOException something went wrong
     */
    public Map<String, Object> getOoliteManifest(String tag) throws IOException {
        log.debug("getOoliteManifest({})", tag);
        String repository = "OoliteProject/oolite";
        String urlStr = baseUrl + "/" + repository + "/releases/" + tag;

        log.debug("Reading {}", urlStr);
        URL url = new URL(urlStr);
        try ( InputStream in = url.openStream()) {
            return (Map<String, Object>) new Genson().deserialize(url.openStream(), Object.class);
        }
    }
    
    /** 
     * Returns the download url for the latest Oolite release.
     * 
     * @param manifest the manifest to evaluate the url from
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
    
    /**
     * Returns the file that should be caching the requested URL.
     * 
     * @param url the requested URL
     * @return the file
     * @throws MalformedURLException something went wrong
     */
    public File getCachedFile(String url) throws MalformedURLException {
        URL u = new URL(url);
        return new File(cacheDIR, u.getHost() + File.separator + u.getFile());
    }
    
    /**
     * Populates the cache from the given url list.
     * 
     * @param urls the urls to download
     * @throws IOException something went wrong
     */
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
        if (u == null) {
            throw new IllegalArgumentException("u must not be null");
        }
        
        switch(u.getProtocol()) {
            case "http":
            case "https":
                doDownloadHttp(u, local);
                break;
            case "file":
                doDownloadFile(u, local);
                break;
            default:
                throw new UnsupportedOperationException(String.format("Cannot download %s", u));
        }
    }
    
    /**
     * Downloads from file:// urls (which is a local copy).
     * @param u the url from which to download
     * @param local the file where to store the data
     * @throws IOException something went wrong
     */
    private void doDownloadFile(URL u, File local) throws IOException {
        log.debug("doDownloadFile(...)");
        if (u == null) {
            throw new IllegalArgumentException("u must not be null");
        }
        if (local == null) {
            throw new IllegalArgumentException("local must not be null");
        }
        
        local.getParentFile().mkdirs();
        try (InputStream in = u.openStream(); OutputStream out = new FileOutputStream(local)) {
            IOUtils.copy(in, out);
        }
    }
    
    private void doDownloadHttp(URL u, File local) throws IOException {
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
     * Performs a HTTP HEAD request to check when the URL was last modified.
     * 
     * Headers for URL http://wiki.alioth.net/img_auth.php/6/68/DTT_Atlas_1.1.oxz.
     * 
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
     * @param u the url to check
     * @return the last modified date
     * @throws IOException something went wrong
     */
    private Date doCheckLastModified(URL u, int followRedirectsCount) throws IOException {
        URLConnection urlconnection = u.openConnection();
        
        if (urlconnection instanceof HttpURLConnection) {
            HttpURLConnection con = (HttpURLConnection)urlconnection;
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
        } else {
            log.warn("cannot check lastModified for url {}", u);
            return new Date();
        }
    }
    
    /** 
     * Ensure we have the resource  in the cache and it is recent enough.
     * Recent enough means the age is less than MAX_AGE.
     * 
     * @param url
     * @throws MalformedURLException
     * @throws IOException 
     */
    public void update(String url) throws IOException {
        log.debug("update({})", url);
        
        URL u = new URL(url);
        File localFile = getCachedFile(url);
        
        Duration age = Duration.between(Instant.ofEpochMilli(localFile.lastModified()), Instant.now());
        
        if (localFile.exists() && (MAX_AGE.compareTo(age) < 0 ) ) {
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
    
    /**
     * Return the plugin's input stream.
     * If the file is not cached or too old it will be automatically downloaded.
     * 
     * @param url the download url of the file
     * @return the inputstream (to the cached file on disk)
     * @throws IOException something went wrong
     */
    public InputStream getPluginInputStream(String url) throws IOException {
        log.debug("getPluginInputStream({})", url);
        
        update(url);

        File cached = getCachedFile(url);
        return new FileInputStream(cached);
    }
    
    public Instant getLastModified(String url) throws IOException {
        log.debug("getLastModified({})", url);
        if (url == null) {
            throw new IllegalArgumentException("url must not be null");
        }
        
        // run a custom HTTP request - see https://www.baeldung.com/java-http-request
        URL u = new URL(url);
        URLConnection uc = u.openConnection();
        if (uc instanceof HttpURLConnection) {
            HttpURLConnection con = (HttpURLConnection)uc;
            try {
                con.setRequestMethod("HEAD");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.connect();

                log.warn("received status {}: {}", con.getResponseCode(), con.getResponseMessage());
                Optional<Map.Entry<String, List<String>>> x = con.getHeaderFields().entrySet().stream()
                        .filter(entry -> "Last-Modified".equals(entry.getKey()))
                        .findFirst();

                if (x.isPresent()) {
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                    try {
                        Date d = format.parse(x.get().getValue().get(0));
                        log.trace("last modified {}", d);
                        return d.toInstant();
                    } catch (ParseException ex) {
                        log.error("could not parse header {}", x.get().getValue(), ex);
                    }
                }

                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        log.warn("received line {}", line);
                    }
                }

            } finally {
                con.disconnect();
            }
        }
        
        return null;
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
    
    /**
     * Sets the baseUrl from which to download Oolite resources. Needed for
     * unit testing.
     * 
     * @param baseUrl the new url
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
