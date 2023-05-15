/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A model of the Ship metadata.
 * 
 * @author hiran
 */
public class Ship implements Wikiworthy, Warnable {
    private Expansion expansion;
    private String identifier;
    private HashMap<String, String> features;
    
    private List<String> warnings;
    
    private String asWikipage;
    
    /**
     * Creates a new Ship.
     */
    public Ship() {
        features = new HashMap<>();
        warnings = new ArrayList<>();
    }

    /**
     * Creates a new Ship with identifier.
     */
    public Ship(String identifier) {
        this();
        this.identifier = identifier;
    }
    
    /**
     * Returns the expansion.
     * 
     * @return the expansion
     */
    public Expansion getExpansion() {
        return expansion;
    }

    /**
     * Sets the expansion.
     * 
     * @param expansion the expansion
     */
    public void setExpansion(Expansion expansion) {
        this.expansion = expansion;
    }

    /**
     * Returns the identifier.
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier.
     * 
     * @param identifier the identifier
     */
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
    
    /**
     * Check whether a feature is contained.
     * 
     * @param name name of the feature
     * @return true and only true if the feature is contained
     */
    public boolean hasFeature(String name) {
        return features.containsKey(name);
    }
    
    /**
     * Returns a feature.
     * 
     * @param name name of the feature
     * @return the feature
     */
    public String getFeature(String name) {
        return features.get(name);
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        if (features.containsKey("name")) {
            return features.get("name");
        } else {
            return identifier;
        }
    }
    
    /**
     * Sets the name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        features.put("name", name);
    }

    /**
     * Returns all features.
     * 
     * @return the map of feature name/value
     */
    public Map<String, String> getFeatures() {
        return new TreeMap<>(features);
    }

    /**
     * Returns the url to the wiki page.
     * 
     * @return the url
     */
    public String getAsWikipage() {
        return asWikipage;
    }

    /**
     * Sets the url to the wiki page.
     * 
     * @param asWikipage the url
     */
    public void setAsWikipage(String asWikipage) {
        this.asWikipage = asWikipage;
    }
    
    /**
     * Returns the type of this WikiWorthy.
     * 
     * @return always "Ship"
     */
    public String getType() {
        return "Ship";
    }

    /**
     * Adds a warning to the list of warnings.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning) {
        warnings.add(warning);
    }

    /**
     * Returns the list of warnings.
     * 
     * @return the list
     */
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    /**
     * Returns whether this ship is a template.
     * 
     * @return true and only true if the ship is a template
     */
    public boolean isTemplate() {
        if (!features.containsKey("is_template")) {
            return false;
        }
        
        String s = String.valueOf(features.get("is_template"));
        return ("1".equals(s) || "yes".equals(s) || "true".equals(s));
    }
}
