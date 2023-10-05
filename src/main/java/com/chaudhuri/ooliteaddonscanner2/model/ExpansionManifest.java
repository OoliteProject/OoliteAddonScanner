/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Resembles an expansion's manifest.
 *
 * @author hiran
 */
public class ExpansionManifest implements Warnable {
    private String author;
    private String category;
    private List<Expansion.Dependency> conflictOxps = new ArrayList<>();
    private String description;
    private String downloadUrl;
    private String fileSize;
    private String identifier;
    private String informationUrl;
    private String license;
    private String maximumOoliteVersion;
    private List<Expansion.Dependency> optionalOxps = new ArrayList<>();
    private String requiredOoliteVersion;
    private List<Expansion.Dependency> requiresOxps = new ArrayList<>();
    private String tags;
    private String title;
    private String version;
    private Long uploadDate;
    
    private List<String> warnings;
    
    /**
     * Creates a new ExpansionManifest.
     */
    public ExpansionManifest() {
        warnings = new ArrayList<>();
    }

    public Long getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Long uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Returns the author.
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author.
     * 
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the category.
     * 
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category.
     * 
     * @param category the category
     */
    public void setCategory(String category) {
        this.category = category;
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
     * Returns the title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     * 
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the version.
     * 
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version.
     * 
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
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
     * Returns the OXPs required by this expansion.
     * 
     * @return the OXP specifier
     */
    public List<Expansion.Dependency> getRequiresOxps() {
        return requiresOxps;
    }

    /**
     * Sets the OXPs required by this expansion.
     * 
     * @param requiresOxps the OXP specifier
     */
    public void setRequiresOxps(List<Expansion.Dependency> requiresOxps) {
        this.requiresOxps = requiresOxps;
    }

    /**
     * Returns the OXPs optionallz used by this expansion.
     * 
     * @return the OXP specifier.
     */
    public List<Expansion.Dependency> getOptionalOxps() {
        return optionalOxps;
    }

    /**
     * Sets the OXPs optionallz used by this expansion.
     * 
     * @param optionalOxps the OXP specifier.
     */
    public void setOptionalOxps(List<Expansion.Dependency> optionalOxps) {
        this.optionalOxps = optionalOxps;
    }

    /**
     * Returns the license.
     * 
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the license.
     * 
     * @param license the license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * returns the required Oolite version.
     * 
     * @return the version specifier
     */
    public String getRequiredOoliteVersion() {
        return requiredOoliteVersion;
    }

    /**
     * Sets the required Oolite version.
     * 
     * @param requiredOoliteVersion the version specifier
     */
    public void setRequiredOoliteVersion(String requiredOoliteVersion) {
        this.requiredOoliteVersion = requiredOoliteVersion;
    }

    /**
     * Returns the information url.
     * 
     * @return the url
     */
    public String getInformationUrl() {
        return informationUrl;
    }

    /**
     * Sets the information url.
     * 
     * @param informationUrl the url
     */
    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
    }

    /**
     * Returns the tags.
     * 
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the tags.
     * 
     * @param tags the tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Returns the file size.
     * 
     * @return the size in bytes
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * Sets the file size.
     * 
     * @param fileSize the size in bytes
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Returns the download url.
     * 
     * @return the url
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Sets the download url.
     * 
     * @param downloadUrl the url
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * Returns the conflicting OXPs.
     * 
     * @return the oxp specifier
     */
    public List<Expansion.Dependency> getConflictOxps() {
        return conflictOxps;
    }

    /**
     * Sets the conflicting OXPs.
     * 
     * @param conflictOxps the OXP specifier
     */
    public void setConflictOxps(List<Expansion.Dependency> conflictOxps) {
        this.conflictOxps = conflictOxps;
    }

    /**
     * Returns the maximum Oolite version.
     * 
     * @return the version specifier
     */
    public String getMaximumOoliteVersion() {
        return maximumOoliteVersion;
    }

    /**
     * Sets the maximum Oolite version.
     * 
     * @param maximumOoliteVersion the version specifier
     */
    public void setMaximumOoliteVersion(String maximumOoliteVersion) {
        this.maximumOoliteVersion = maximumOoliteVersion;
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
     * Returns whether this ExpansionManifest has warnings.
     * 
     * @return true and only true if there are warnings
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
