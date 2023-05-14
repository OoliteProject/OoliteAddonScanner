/*
 */
package com.chaudhuri.ooliteaddonscanner2.model;

/**
 *
 * @author hiran
 */
public class Model {

    private String name;
    private int nvertexes;
    private int nfaces;

    public Model() {
    }

    public Model(String name, int nvertexes, int nfaces) {
        this.name = name;
        this.nvertexes = nvertexes;
        this.nfaces = nfaces;
    }

    public int getNvertexes() {
        return nvertexes;
    }

    public void setNvertexes(int nvertexes) {
        this.nvertexes = nvertexes;
    }

    public int getNfaces() {
        return nfaces;
    }

    public void setNfaces(int nfaces) {
        this.nfaces = nfaces;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model{" + "name=" + name + "nvertexes=" + nvertexes + ", nfaces=" + nfaces + '}';
    }
    
    
}
