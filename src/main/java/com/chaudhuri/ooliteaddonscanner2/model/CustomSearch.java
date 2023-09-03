/*
 */

package com.chaudhuri.ooliteaddonscanner2.model;

import com.chaudhuri.ooliteaddonscanner2.XMLPlistParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A search that can be added ad-hoc to find specific configuration or code
 * snippets.
 * 
 * If a filename matches includePattern and does not match excludePattern
 * it's content shall be scanned for pattern.
 * 
 * @author hiran
 */
public class CustomSearch {
    private static final Logger log = LogManager.getLogger();
    
    public static class Hit {
        private String expansionId;
        private String relPath;
        private int lineNumber;
        private String line;
        private int start;
        private int stop;

        public Hit(String expansionId, String relPath, int lineNumber, String line, int start, int stop) {
            this.expansionId = expansionId;
            this.relPath = relPath;
            this.lineNumber = lineNumber;
            this.line = line;
            this.start = start;
            this.stop = stop;
        }

        public String getExpansionId() {
            return expansionId;
        }

        public void setExpansionId(String expansionId) {
            this.expansionId = expansionId;
        }

        public String getRelPath() {
            return relPath;
        }

        public void setRelPath(String relPath) {
            this.relPath = relPath;
        }

        public int getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getStop() {
            return stop;
        }

        public void setStop(int stop) {
            this.stop = stop;
        }
        
        public String getLeader() {
            return line.substring(0, start);
        }
        
        public String getMatch() {
            return line.substring(start, stop);
        }
        
        public String getTrailer() {
            return line.substring(stop);
        }
    }

    private String name;
    
    /** Pattern to search for in the file content. */
    private Pattern pattern;
    
    /** Pattern for filenames to be skipped. */
    private Pattern excludePattern;
    
    /** Pattern for filenames to be searched. */
    private Pattern includePattern;
    
    private List<Hit> results;

    /**
     * Creates a new instance of CustomSearch.
     */
    public CustomSearch(String name) {
        this.name = name;
    }
    
    public void init() {
        if (results != null) {
            throw new IllegalStateException("This search has already run?");
        }

        results = new ArrayList<>();
    }

    /**
     * Creates a new initialized instance of CustomSearch.
     * @param pattern
     * @param excludePattern
     * @param includePattern 
     */
    public CustomSearch(String name, Pattern pattern, Pattern excludePattern, Pattern includePattern) {
        this.name = name;
        this.pattern = pattern;
        this.excludePattern = excludePattern;
        this.includePattern = includePattern;
    }

    /**
     * Returns the pattern to be searched.
     * 
     * @return the pattern
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the pattern to be searched.
     * 
     * @param pattern the pattern
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the pattern for filenames to be excluded.
     * 
     * @return the pattern
     */
    public Pattern getExcludePattern() {
        return excludePattern;
    }

    /**
     * Sets the pattern for filenames to be excluded.
     * 
     * @param excludePattern the pattern
     */
    public void setExcludePattern(Pattern excludePattern) {
        this.excludePattern = excludePattern;
    }

    /**
     * Returns the pattern for filenames to be included.
     * 
     * @return the pattern
     */
    public Pattern getIncludePattern() {
        return includePattern;
    }

    /**
     * Sets the pattern for filenames to be included.
     * 
     * @param includePattern the pattern
     */
    public void setIncludePattern(Pattern includePattern) {
        this.includePattern = includePattern;
    }

    /**
     * Check if the filename matches this search.
     * 
     * @param filename the filename to search
     * @return true if this search wants to engage
     */
    public boolean willSearch(String filename) {
        if (includePattern!=null && !includePattern.matcher(filename).matches()) {
            return false;
        }
        if (excludePattern != null && excludePattern.matcher(filename).matches()) {
            return false;
        }
        return true;
    }
    
    /**
     * Searches the inputstream for hits.
     * 
     * @param expansionId, the expansion id, for better hit reporting
     * @param relPath, the path within the expansion, for better hit reporting
     * @param content the content to search
     * @return the list of hits found
     * @throws IOException something went wrong
     */
    public List<Hit> search(String expansionId, String relPath, InputStream content) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(content);
                BufferedReader reader = new BufferedReader(isr)) {
            
            List<Hit> result = new ArrayList<>();
            int lineNo = 0;
            
            while(reader.ready()) {
                String line = reader.readLine();
                lineNo++;
                
                result.addAll(searchLine(expansionId, relPath, lineNo, line));
            }
            
            return result;
        }
    }
    
    /**
     * Searches one line of content for hits.
     * 
     * @param expansionId the expansion identifier, for better hit reporting
     * @param relPath the file within the expansion, for better hit reporting
     * @param lineNo the line number, for better hit reporting
     * @param line the line to search
     * @return the list of hits found
     */
    public List<Hit> searchLine(String expansionId, String relPath, int lineNo, String line) {
        Matcher m = pattern.matcher(line);
        m.results().forEach((t) -> {
            log.debug("Found match {}!{}[{}:{}] -- {}", expansionId, relPath, lineNo, t.start(), t.group());
            if (t.start() == t.end()) {
                throw new IllegalStateException("Are we moving forward??");
            }
            
            Hit hit = new Hit(expansionId, relPath, lineNo, line, t.start(), t.end());
            results.add(hit);
        });
        return null;
    }
    
    /**
     * Return the hits found by this custom search.
     * 
     * @return the list of hits
     */
    public List<Hit> getResults() {
        return results;
    }

    /**
     * Returns this search's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CustomSearch{" + "name=" + name + ", pattern=" + pattern + ", excludePattern=" + excludePattern + ", includePattern=" + includePattern + '}';
    }

    /**
     * Parses an XML search file and returns the CustomSearches found.
     * 
     * @param searchFilename
     * @return
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public static List<CustomSearch> parseSearchFilename(String searchFilename) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        File searchFile = new File(searchFilename);
        if (!searchFile.exists()) {
            throw new FileNotFoundException(searchFile.getAbsolutePath());
        }
        
        Document doc = XMLPlistParser.parseInputStream(new FileInputStream(searchFile), null);
        XPath xpath = XPathFactory.newDefaultInstance().newXPath();

        List<CustomSearch> result = new ArrayList<>();
        
        NodeList nl = (NodeList)xpath.evaluate("/Searches/Search", doc, XPathConstants.NODESET);
        if (nl == null || nl.getLength() == 0) {
            throw new IllegalArgumentException("Document does not contain searches.");
        }
        
        for (int i=0; i<nl.getLength();i++) {
            Element search = (Element)nl.item(i);

            CustomSearch cs = new CustomSearch(searchFilename + "[" + (i+1) + "]");
            if (search.hasAttribute("pattern")) {
                cs.setPattern(Pattern.compile(search.getAttribute("pattern")));
            }
            if (search.hasAttribute("include")) {
                cs.setIncludePattern(Pattern.compile(search.getAttribute("include")));
            }
            if (search.hasAttribute("exclude")) {
                cs.setExcludePattern(Pattern.compile(search.getAttribute("exclude")));
            }

            result.add(cs);
        }
        
        return result;
    }

}
