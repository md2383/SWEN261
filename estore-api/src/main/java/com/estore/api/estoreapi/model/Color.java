package com.estore.api.estoreapi.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Color {
    private String name;

    /**
     * Defines the color
     * 
     * @param name color name
     * 
     */
    @JsonCreator
    public Color(@JsonProperty("name") String name) {
        this.name = name;
    }

    /**
     * gets the name of color
     * 
     * @return string name
     * 
     */
    public String getName() {
        return this.name;
    }

    /**
     * compare color
     * 
     * @param Object other object color
     * @return boolean if equal
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Color) {
            Color o = (Color) other;
            return o.name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
