/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

/**
 *
 * @author hiran
 */
public interface Wikiworthy {

    public String getAsWikipage();

    public void setAsWikipage(String asWikipage);
    
    public String getName();
    
    public String getIdentifier();

    public String getType();
    
    public Expansion getExpansion();
    
    public void addWarning(String warning);
}
