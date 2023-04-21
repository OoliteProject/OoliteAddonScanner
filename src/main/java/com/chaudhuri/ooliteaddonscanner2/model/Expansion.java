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
    
    /**
     * Creates a new Expansion.
     */
    public Expansion() {
        this.warnings = new ArrayList<>();
        
        this.equipment = new TreeMap<>();
        this.ships = new TreeMap<>();
        this.manifest = new ExpansionManifest();
        this.readmes = new TreeMap<>();
        this.scripts = new TreeMap<>();
    }
    
    /**
     * Creates a new Expansion with identifier.
     */
    public Expansion(String identifier) {
        this();
        this.identifier = identifier;
    }

    /**
     * Returns the expansion this expansion is connected with.
     * Well, it is not, therefore it always returns null.
     * 
     * @return null
     */
    public Expansion getExpansion() {
        return null;
    }
    
    /**
     * Adds Equipment to this Expansion.
     * 
     * @param e the Equipment to add
     */
    public void addEquipment(Equipment e) {
        if (e.getIdentifier() == null) {
            throw new IllegalArgumentException("Equipment must have an identifier.");
        }
        equipment.put(e.getIdentifier(), e);
    }
    
    /**
     * Adds a Ship to this Expansion.
     * 
     * @param s the Shipto add
     */
    public void addShip(Ship s) {
        if (s.getIdentifier()==null) {
            throw new IllegalArgumentException("The ship must have an identifier.");
        }
        ships.put(s.getIdentifier(), s);
    }
    
    /**
     * Adds a Readme file to this Expansion.
     * 
     * @param filename name of the file
     * @param content content of the file
     */
    public void addReadme(String filename, String content) {
        readmes.put(filename, content);
    }
    
    /**
     * Returns a String representation of this Expansion.
     * 
     * @return the string
     */
    public String toString() {
        return identifier;
    }
    
    /**
     * Returns the equipment in this expansion.
     * 
     * @return the list of equipment
     */
    public List<Equipment> getEquipment() {
        return new ArrayList<>(equipment.values());
    }
    
    /**
     * Returns the ships in this expansion.
     * 
     * @return the list of ships
     */
    public List<Ship> getShips() {
        return new ArrayList<>(ships.values());
    }
    
    /**
     * Returns the Readme files in this expansion.
     * 
     * @return the map of readme names/readme content
     */
    public Map<String, String> getReadmes() {
        return new TreeMap<>(readmes);
    }
    
    /**
     * Sets the identifier for this Expansion.
     * 
     * @param identifier the identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * Returns the identifier for this Expansion.
     * 
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the author of this expansion.
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of this expansion.
     * 
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the category for this project.
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category for this project.
     * 
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the conflicting OXPs.
     * 
     * @return the oxp specifier
     */
    public String getConflictOxps() {
        return conflictOxps;
    }

    /**
     * Sets the conflicting OXPs.
     * 
     * @param conflictOxps the oxp specifier
     */
    public void setConflictOxps(String conflictOxps) {
        this.conflictOxps = conflictOxps;
    }

    /**
     * Returns the version of this expansion.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version of this expansion.
     * 
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the upload date of this expansion.
     * 
     * @return the date
     */
    public String getUploadDate() {
        return uploadDate;
    }

    /**
     * Sets the upload date of this expansion.
     * 
     * @param uploadDate the date
     */
    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Returns the description of this expansion.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this expansion.
     * 
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the download url for this expansion.
     * 
     * @return the url
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Sets the download url for this expansion.
     * 
     * @param downloadUrl the url
     */
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
        if (title != null) {
            return title;
        } else {
            return identifier;
        }
    }

    /** Sets the title aka the name of the OXP.
     * 
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the tags of this expansion.
     * 
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the tags of this expansion.
     * 
     * @param tags the tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Returns the file size of this expansion.
     * 
     * @return the size
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * Sets the file size of this expansion.
     * 
     * @param fileSize the size
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Returns the information url for this expansion.
     * 
     * @return the url
     */
    public String getInformationUrl() {
        return informationUrl;
    }

    /**
     * Sets the information url for this expansion.
     * 
     * @param informationUrl the url
     */
    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }

    /**
     * Returns the license for this expansion.
     * 
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the license for this expansion.
     * 
     * @param license the license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Returns the required Oolite version.
     * 
     * @return the version
     */
    public String getRequiredOoliteVersion() {
        return requiredOoliteVersion;
    }

    /**
     * Sets the required Oolite version.
     * 
     * @param requiredOoliteVersion the version
     */
    public void setRequiredOoliteVersion(String requiredOoliteVersion) {
        this.requiredOoliteVersion = requiredOoliteVersion;
    }

    /**
     * Returns the required OXPs.
     * 
     * @return the oxp specifier
     */
    public String getRequiresOxps() {
        return requiresOxps;
    }

    /**
     * Sets the required OXPs.
     * 
     * @param requiresOxps the oxp specifier
     */
    public void setRequiresOxps(String requiresOxps) {
        this.requiresOxps = requiresOxps;
    }

    /**
     * Returns the optional OXPs.
     * 
     * @return the oxp specifier
     */
    public String getOptionalOxps() {
        return optionalOxps;
    }

    /**
     * Sets the optional OXPs.
     * 
     * @param optionalOxps the oxp specifier
     */
    public void setOptionalOxps(String optionalOxps) {
        this.optionalOxps = optionalOxps;
    }

    /**
     * Returns the maximum Oolite version.
     * 
     * @return the version
     */
    public String getMaximumOoliteVersion() {
        return maximumOoliteVersion;
    }

    /**
     * Sets the maximum Oolite version.
     * 
     * @param maximumOoliteVersion the version
     */
    public void setMaximumOoliteVersion(String maximumOoliteVersion) {
        this.maximumOoliteVersion = maximumOoliteVersion;
    }

    /**
     * Returns the manifest.
     * 
     * @return the manifest
     */
    public ExpansionManifest getManifest() {
        return manifest;
    }

    /**
     * Sets the manifest.
     * 
     * @param manifest the manifest
     */
    public void setManifest(ExpansionManifest manifest) {
        this.manifest = manifest;
    }

    /**
     * Returns the wiki page url.
     * 
     * @return the url
     */
    public String getAsWikipage() {
        return asWikipage;
    }

    /**
     * Sets the wiki page url.
     * 
     * @param asWikipage the url
     */
    public void setAsWikipage(String asWikipage) {
        this.asWikipage = asWikipage;
    }
    
    /**
     * Returns the type of this expansion.
     * 
     * @return always "Expansion"
     */
    public String getType() {
        return "Expansion";
    }
    
    /**
     * Adds a script to this expansion.
     * 
     * @param script the script name
     * @param content the script content
     */
    public void addScript(String script, String content) {
        scripts.put(script, content);
    }

    /**
     * Returns the scripts in this expansion.
     * 
     * @return the map of script name/script content
     */
    public Map<String, String> getScripts() {
        return new TreeMap<>(scripts);
    }
    
    /**
     * Adds a warning to this expansion.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning) {
        warnings.add(warning);
    }

    /**
     * Returns the list of warnings in this expansion.
     * 
     * @return the warnings
     */
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
