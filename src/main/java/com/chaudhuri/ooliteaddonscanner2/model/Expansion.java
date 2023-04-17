/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author hiran
 */
public class Expansion implements Wikiworthy {
    private String identifier;
    private String requiredOoliteVersion;
    private String title;
    private String version;
    private String category;
    private String description;
    private String downloadUrl;
    private String author;
    private String fileSize;
    private String informationUrl;
    private String license;
    private String uploadDate;
    private String tags;
    private String requiresOxps;
    private String optionalOxps;
    private String conflictOxps;
    private String maximumOoliteVersion;
    private ExpansionManifest manifest;
    private final TreeMap<String, Equipment> equipment;
    private final TreeMap<String, Ship> ships;
    private final TreeMap<String, String> readmes;
    private final TreeMap<String, String> scripts;
    
    private String asWikipage;
    
    private List<String> warnings;
    
    public Expansion() {
        this.warnings = new ArrayList<>();
        
        this.equipment = new TreeMap<>();
        this.ships = new TreeMap<>();
        this.manifest = new ExpansionManifest();
        this.readmes = new TreeMap<>();
        this.scripts = new TreeMap<>();
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
        return new ArrayList<>(equipment.values());
    }
    
    public List<Ship> getShips() {
        return new ArrayList<>(ships.values());
    }
    
    public Map<String, String> getReadmes() {
        return new TreeMap<>(readmes);
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

    public String getConflictOxps() {
        return conflictOxps;
    }

    public void setConflictOxps(String conflictOxps) {
        this.conflictOxps = conflictOxps;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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
        return getTitle();
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

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getRequiredOoliteVersion() {
        return requiredOoliteVersion;
    }

    public void setRequiredOoliteVersion(String requiredOoliteVersion) {
        this.requiredOoliteVersion = requiredOoliteVersion;
    }

    public String getRequiresOxps() {
        return requiresOxps;
    }

    public void setRequiresOxps(String requiresOxps) {
        this.requiresOxps = requiresOxps;
    }

    public String getOptionalOxps() {
        return optionalOxps;
    }

    public void setOptionalOxps(String optionalOxps) {
        this.optionalOxps = optionalOxps;
    }

    public String getMaximumOoliteVersion() {
        return maximumOoliteVersion;
    }

    public void setMaximumOoliteVersion(String maximumOoliteVersion) {
        this.maximumOoliteVersion = maximumOoliteVersion;
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
    
    public void addScript(String script, String content) {
        scripts.put(script, content);
    }
    
    public Map<String, String> getScripts() {
        return new TreeMap<>(scripts);
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        List<String> result = new ArrayList<>(warnings);
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
    
}
