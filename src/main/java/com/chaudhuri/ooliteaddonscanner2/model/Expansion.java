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
public class Expansion implements Wikiworthy {
    private String identifier;
    private String required_oolite_version;
    private String title;
    private String version;
    private String category;
    private String description;
    private String download_url;
    private String author;
    private String file_size;
    private String information_url;
    private String license;
    private String upload_date;
    private String tags;
    private String requires_oxps;
    private String optional_oxps;
    private String conflict_oxps;
    private String maximum_oolite_version;
    private ExpansionManifest manifest;
    private final TreeMap<String, Equipment> equipment;
    private final TreeMap<String, Ship> ships;
    private final TreeMap<String, String> readmes;
    private final List<String> scripts;
    
    private String asWikipage;
    
    private List<String> warnings;
    
    public Expansion() {
        this.warnings = new ArrayList<String>();
        
        this.equipment = new TreeMap<>();
        this.ships = new TreeMap<>();
        this.manifest = new ExpansionManifest();
        this.readmes = new TreeMap<>();
        this.scripts = new ArrayList<>();
    }
    
    public Expansion getExpansion() {
        return null;
    }
    
    public void addEquipment(Equipment e) {
        equipment.put(e.getIdentifier(), e);
    }
    
    public void addShip(Ship s) {
        ships.put(s.getIdentifier(), s);
    }
    
    public void addReadme(String filename, String content) {
        readmes.put(filename, content);
    }
    
    public String toString() {
        return identifier;
    }
    
    public List<Equipment> getEquipment() {
        return new ArrayList<Equipment>(equipment.values());
    }
    
    public List<Ship> getShips() {
        return new ArrayList<Ship>(ships.values());
    }
    
    public Map<String, String> getReadmes() {
        return new TreeMap<String, String>(readmes);
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return identifier;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConflict_oxps() {
        return conflict_oxps;
    }

    public void setConflict_oxps(String conflict_oxps) {
        this.conflict_oxps = conflict_oxps;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    /** Returns the title aka the name of the OXP.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /** Returns the title aka the name of the OXP.
     * 
     * @return the title
     */
    public String getName() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getInformation_url() {
        return information_url;
    }

    public void setInformation_url(String information_url) {
        this.information_url = information_url;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getRequired_oolite_version() {
        return required_oolite_version;
    }

    public void setRequired_oolite_version(String required_oolite_version) {
        this.required_oolite_version = required_oolite_version;
    }

    public String getRequires_oxps() {
        return requires_oxps;
    }

    public void setRequires_oxps(String requires_oxps) {
        this.requires_oxps = requires_oxps;
    }

    public String getOptional_oxps() {
        return optional_oxps;
    }

    public void setOptional_oxps(String optional_oxps) {
        this.optional_oxps = optional_oxps;
    }

    public String getMaximum_oolite_version() {
        return maximum_oolite_version;
    }

    public void setMaximum_oolite_version(String maximum_oolite_version) {
        this.maximum_oolite_version = maximum_oolite_version;
    }

    public ExpansionManifest getManifest() {
        return manifest;
    }

    public void setManifest(ExpansionManifest manifest) {
        this.manifest = manifest;
    }

    public String getAsWikipage() {
        return asWikipage;
    }

    public void setAsWikipage(String asWikipage) {
        this.asWikipage = asWikipage;
    }
    
    public String getType() {
        return "Expansion";
    }
    
    public void addScript(String script) {
        if (!scripts.contains(script)) {
            scripts.add(script);
        }
    }
    
    public List<String> getScripts() {
        return new ArrayList<>(scripts);
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        List<String> result = new ArrayList<String>(warnings);
        if (manifest != null) {
            result.addAll(manifest.getWarnings());
        }
        for (Equipment e: equipment.values()) {
            result.addAll(e.getWarnings());
        }
        for (Ship s: ships.values()) {
            result.addAll(s.getWarnings());
        }
        return result;
    }
    
//    public boolean hasWarnings() {
//        if (!warnings.isEmpty())
//            return true;
//        
//        if (manifest != null) {
//            return manifest.hasWarnings();
//        }
//        
//        return false;
//    }
}
