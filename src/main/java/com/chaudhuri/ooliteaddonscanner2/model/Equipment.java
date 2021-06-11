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
    
    public Equipment() {
        features = new HashMap<>();
        warnings = new ArrayList<>();
    }

    public Expansion getExpansion() {
        return expansion;
    }

    public void setExpansion(Expansion expansion) {
        this.expansion = expansion;
    }

    public String getTechlevel() {
        return techlevel;
    }

    public void setTechlevel(String techlevel) {
        this.techlevel = techlevel;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getName() {
        if (name == null || "null".equals(name) || "".equals(name)) {
            return identifier;
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Map<String, String> getFeatures() {
        return new TreeMap<String, String>(features);
    }
    
    public void putFeature(String key, String value) {
        features.put(key, value);
    }
    
    public String toString() {
        return getClass().getName() + "(expansion=" + expansion + ", identifier=" + identifier + ")";
    }

    public String getAsWikipage() {
        return asWikipage;
    }

    public void setAsWikipage(String asWikipage) {
        this.asWikipage = asWikipage;
    }
    
    public String getType() {
        return "Equipment";
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        return new ArrayList<String>(warnings);
    }
    
    public boolean isVisible() {
        String s = features.get("visible");
        if (s==null) {
            return true;
        }
        
        return !("false".equals(s) || "no".equals(s));
    }
}
