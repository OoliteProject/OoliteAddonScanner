/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hiran
 */
public class ExpansionManifest {
    private String author;
    private String category;
    private String conflictOxps;
    private String description;
    private String downloadUrl;
    private String fileSize;
    private String identifier;
    private String informationUrl;
    private String license;
    private String maximumOoliteVersion;
    private String optionalOxps;
    private String requiredOoliteVersion;
    private String requiresOxps;
    private String tags;
    private String title;
    private String version;
    
    private List<String> warnings;
    
    public ExpansionManifest() {
        warnings = new ArrayList<>();
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getInformationUrl() {
        return informationUrl;
    }

    public void setInformationUrl(String informationUrl) {
        this.informationUrl = informationUrl;
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getConflictOxps() {
        return conflictOxps;
    }

    public void setConflictOxps(String conflictOxps) {
        this.conflictOxps = conflictOxps;
    }

    public String getMaximumOoliteVersion() {
        return maximumOoliteVersion;
    }

    public void setMaximumOoliteVersion(String maximumOoliteVersion) {
        this.maximumOoliteVersion = maximumOoliteVersion;
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
