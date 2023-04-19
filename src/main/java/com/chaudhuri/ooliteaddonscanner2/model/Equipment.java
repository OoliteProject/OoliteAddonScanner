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
public class Equipment implements Wikiworthy {
    private Expansion expansion;
    
    private String techlevel;
    private String cost;
    private String name;
    private String identifier;
    private String description;
    private final HashMap<String, String> features;
    
    private final List<String> warnings;
    
    private String asWikipage;
    
    /**
     * Creates a new equipment.
     */
    public Equipment() {
        features = new HashMap<>();
        warnings = new ArrayList<>();
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
     * Returns the tech level.
     * 
     * @return the tech level
     */
    public String getTechlevel() {
        return techlevel;
    }

    /**
     * Sets the tech level.
     * 
     * @param techlevel the tech level
     */
    public void setTechlevel(String techlevel) {
        this.techlevel = techlevel;
    }

    /**
     * Returns the cost.
     * 
     * @return the cost
     */
    public String getCost() {
        return cost;
    }

    /**
     * Sets the cost.
     * 
     * @param cost the cost
     */
    public void setCost(String cost) {
        this.cost = cost;
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
     * Returns the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * 
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the name.
     * 
     * @return the name - or, if not set the identifier
     */
    public String getName() {
        if (name == null || "null".equals(name) || "".equals(name)) {
            return identifier;
        } else {
            return name;
        }
    }

    /**
     * Sets the name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns all features.
     * 
     * @return the map with name/value of features
     */
    public Map<String, String> getFeatures() {
        return new TreeMap<>(features);
    }
    
    /**
     * Adds a feature.
     * 
     * @param key name of feature
     * @param value value of feature
     */
    public void putFeature(String key, String value) {
        features.put(key, value);
    }

    /**
     * Returns a string representation of this class.
     * 
     * @return the string
     */
    public String toString() {
        return getClass().getName() + "(expansion=" + expansion + ", identifier=" + identifier + ")";
    }

    /**
     * Returns the url to wiki page.
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
     * Returns the type of this Wikiworthy.
     * 
     * @return alwazs "Equipment"
     */
    public String getType() {
        return "Equipment";
    }
    
    /**
     * Adds a warning to this equipment.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    /**
     * Returns the whole list of warnings.
     * 
     * @return the list
     */
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

    /**
     * Returns the visibility feature.
     * @return true if and only if the equipment is visible
     */
    public boolean isVisible() {
        String s = features.get("visible");
        if (s==null) {
            return true;
        }
        
        return !("false".equals(s) || "no".equals(s));
    }
    
    /**
     * Checks whether the equipment is primeable.
     * This boils down to the question whether the equipment has a script.
     * 
     * @return true if and only if the equipment is primeable
     */
    public boolean isPrimeable() {
        return features.containsKey("script");
    }
}
