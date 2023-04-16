/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author hiran
 */
public class Ship implements Wikiworthy {
    private Expansion expansion;
    private String identifier;
    private HashMap<String, String> features;
    
    private List<String> warnings;
    
    private String asWikipage;
    
    public Ship() {
        features = new HashMap<>();
        warnings = new ArrayList<>();
    }

    public Expansion getExpansion() {
        return expansion;
    }

    public void setExpansion(Expansion expansion) {
        this.expansion = expansion;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Adds a feature to a ship.
     * Do we need to check for uniqueness?
     * 
     * @param name the feature name
     * @param value the feature value
     */
    public void addFeature(String name, String value) {
        
        features.put(name, value);
    }
    
    public boolean hasFeature(String name) {
        return features.containsKey(name);
    }
    
    public String getFeature(String name) {
        return features.get(name);
    }
    
    public String getName() {
        if (features.containsKey("name")) {
            return features.get("name");
        } else {
            return identifier;
        }
    }
    
    public Map<String, String> getFeatures() {
        return new TreeMap<>(features);
    }

    public String getAsWikipage() {
        return asWikipage;
    }

    public void setAsWikipage(String asWikipage) {
        this.asWikipage = asWikipage;
    }
    
    public String getType() {
        return "Ship";
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    public boolean isTemplate() {
        if (!features.containsKey("is_template")) {
            return false;
        }
        
        String s = String.valueOf(features.get("is_template"));
        return ("1".equals(s) || "yes".equals(s) || "true".equals(s));
    }
}
