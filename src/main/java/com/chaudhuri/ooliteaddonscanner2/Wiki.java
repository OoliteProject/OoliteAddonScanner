/*
 */
package com.chaudhuri.ooliteaddonscanner2;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import com.chaudhuri.ooliteaddonscanner2.model.Wikiworthy;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hiran
 */
public class Wiki {
    private static final Logger log = LoggerFactory.getLogger(Wiki.class);
    
    private static Map<String, String> wikiPageCache = new TreeMap<>();
    private static Set<String> wikiPageMisses = new TreeSet<>();
    
    public static class WikiCheck implements Runnable {
        
        private Wikiworthy ww;
        
        public WikiCheck(Wikiworthy ww) {
            this.ww = ww;
        }

        @Override
        public void run() {
            //ww.setAsWikipage(Wiki.wikiPageFor(ww.getName()));
            checkWikiPage(ww);
        }
        
    }
    
    public static String getPageUrl(String name) {
        return "http://wiki.alioth.net/index.php/"
                + name.replace(" ", "%20")
                        .replace("\"", "%22");
    }
    
    public static void checkWikiPage(Wikiworthy wikiworthy) {
        log.debug("checkWikiPage({})", wikiworthy);

        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {    
            RequestBuilder builder = RequestBuilder.head(getPageUrl(wikiworthy.getName()));
            builder.setCharset(Charset.forName("UTF-8"));
            
            HttpUriRequest request = builder.build();
            try (final CloseableHttpResponse response = httpclient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    wikiworthy.setAsWikipage(request.getURI().toString());
                } else {
                    if (wikiworthy instanceof Expansion) {
                        wikiworthy.addWarning(String.format("%s -> %s %s", request.getURI(), response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
                        
                        // check whether we have a low-hanging fruit for Expansions
                        if (wikiworthy instanceof Expansion) {
                            Expansion expansion = (Expansion)wikiworthy;
                            
                            if (String.valueOf(expansion.getInformation_url()).contains("//wiki.alioth.net/index.php")) {
                            
                                RequestBuilder builder2 = RequestBuilder.head(expansion.getInformation_url());
                                builder2.setCharset(Charset.forName("UTF-8"));
                                HttpUriRequest request2 = builder2.build();
                                try (final CloseableHttpResponse response2 = httpclient.execute(request2)) {
                                    if (response2.getStatusLine().getStatusCode() == 200) {
                                        expansion.addWarning("Low hanging fuit: Information URL exists...");
                                    } else {
                                        expansion.addWarning("High hanging fruit");
                                    }
                                } catch (IOException e) {
                                    log.warn("Could not check for low hanging fruit", e);
                                }
                            
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            wikiworthy.addWarning(String.format("Wiki check failed: %s: %s", e.getClass().getName(), e.getMessage()));
        }
    }
    
    public static String wikiPageFor(String name) {
        log.debug("wikiPageFor({})", name);

        if (wikiPageMisses.contains(name)) {
            // we already found out this page is missing
            return null;
        }
        String urlStr = wikiPageCache.get(name);
        if (urlStr != null) {
            // we already got a positive response
            return urlStr;
        }
        
        // no positive response cached, neither as hit nor fail
        urlStr = getPageUrl(name);
        
        try {
            URL url = new URL(urlStr);
            InputStreamReader rin = new InputStreamReader(url.openStream());
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int read = read = rin.read(buffer);;
            while (read >= 0) {
                sb.append(buffer, 0, read);
                read = rin.read(buffer);
            }
            log.trace("wiki={}", sb.toString());
            wikiPageCache.put(name, urlStr);
            
            return urlStr;
        } catch (Exception e) {
            wikiPageMisses.add(name);
            log.trace("Cannot find url {}", urlStr, e);
            return null;
        }
        
    }
    
}
