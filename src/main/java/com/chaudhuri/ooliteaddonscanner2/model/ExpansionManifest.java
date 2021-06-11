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
    private String conflict_oxps;
    private String description;
    private String download_url;
    private String file_size;
    private String identifier;
    private String information_url;
    private String license;
    private String maximum_oolite_version;
    private String optional_oxps;
    private String required_oolite_version;
    private String requires_oxps;
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

    public String getInformation_url() {
        return information_url;
    }

    public void setInformation_url(String information_url) {
        this.information_url = information_url;
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

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getConflict_oxps() {
        return conflict_oxps;
    }

    public void setConflict_oxps(String conflict_oxps) {
        this.conflict_oxps = conflict_oxps;
    }

    public String getMaximum_oolite_version() {
        return maximum_oolite_version;
    }

    public void setMaximum_oolite_version(String maximum_oolite_version) {
        this.maximum_oolite_version = maximum_oolite_version;
    }
    
    public void addWarning(String warning) {
        warnings.add(warning);
    }
    
    public List<String> getWarnings() {
        return new ArrayList<String>(warnings);
    }
    
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
