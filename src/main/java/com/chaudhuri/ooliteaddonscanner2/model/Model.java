/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

/**
 * Represents a ship model.
 * 
 * @author hiran
 */
public class Model {

    private String name;
    private int nvertexes;
    private int nfaces;

    /**
     * Creates a new Model.
     */
    public Model() {
    }

    /**
     * Creates a new Model.
     * 
     * @param name name of the model
     * @param nvertexes number of vertexes
     * @param nfaces number of faces
     */
    public Model(String name, int nvertexes, int nfaces) {
        this.name = name;
        this.nvertexes = nvertexes;
        this.nfaces = nfaces;
    }

    /**
     * Returns the number of vertexes.
     * 
     * @return the number
     */
    public int getNvertexes() {
        return nvertexes;
    }

    /**
     * Sets the number of vertexes.
     * 
     * @param nvertexes the number
     */
    public void setNvertexes(int nvertexes) {
        this.nvertexes = nvertexes;
    }

    /**
     * Returns the number of faces.
     * 
     * @return the number
     */
    public int getNfaces() {
        return nfaces;
    }

    /**
     * Sets the number of faces.
     * 
     * @param nfaces the number
     */
    public void setNfaces(int nfaces) {
        this.nfaces = nfaces;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model{" + "name=" + name + "nvertexes=" + nvertexes + ", nfaces=" + nfaces + '}';
    }
    
    
}
