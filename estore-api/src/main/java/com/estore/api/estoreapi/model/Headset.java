package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Headset extends Product {
    /**
     * Constructor for Headset
     * 
     */
    public Headset(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product",
                imageURL);
    }

    @JsonCreator
    public Headset(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }

    /**
     * private Constructor for Controller
     * 
     */
    private Headset(Headset h, int id) {
        this(id, h.getAllColors(), h.getName(), h.getQuantity(), h.getPrice(), h.getImageURL());
    }

    /**
     * retrieve the product type
     * 
     * @return ProductType of headset
     */
    @Override
    public ProductType getProductType() {
        return ProductType.HEADSET;
    }

    /**
     * copies the headset
     * 
     * @param id int id
     * @return Product of a headset
     */
    @Override
    public Product copy(int id) {
        return new Headset(this, id);
    }

    /**
     * compares headset
     * 
     * @param other object of headset
     * @return boolean of comparison
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Headset) {
            Headset o = (Headset) other;
            return super.equals(other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, price, imageURL, Objects.hashCode(colors));
    }
}
