/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

/**
 * Represents an item that (should) have a wiki page.
 *
 * @author hiran
 */
public interface Wikiworthy {

    /**
     * Returns the url to the wiki page.
     * 
     * @return the url
     */
    public String getAsWikipage();

    /**
     * Sets the url to the wiki page.
     * 
     * @param asWikipage the wiki page
     */
    public void setAsWikipage(String asWikipage);

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName();
    
    /**
     * Returns the identifier.
     * 
     * @return the identifier
     */
    public String getIdentifier();

    /**
     * Returns the type of the WikiWorthy.
     * 
     * @return the type
     */
    public String getType();
    
    /**
     * Returns the expansion.
     * 
     * @return the expansion
     */
    public Expansion getExpansion();
    
    /**
     * Adds a warning to the warning list.
     * 
     * @param warning the warning
     */
    public void addWarning(String warning);
}
