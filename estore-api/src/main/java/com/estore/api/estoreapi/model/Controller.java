package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Controller extends Product {
    /**
     * Constructor for Controller
     * 
     */
    public Controller(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product",
                imageURL);
    }

    @JsonCreator
    public Controller(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }

    /**
     * Private Constructor for Controller
     * 
     */
    private Controller(Controller c, int id) {
        this(id, c.getAllColors(), c.getName(), c.getQuantity(), c.getPrice(), c.getImageURL());
    }

    /**
     * get ProductType
     * 
     * @return a ProductType
     * 
     */
    @Override
    public ProductType getProductType() {
        return ProductType.CONTROLLER;
    }

    /**
     * Copies another controller
     * 
     * @param id int id
     * @return a new Product Controller
     */
    @Override
    public Product copy(int id) {
        return new Controller(this, id);
    }

    /**
     * compares controller
     * 
     * @param other object controller
     * @return boolean if equal
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Controller) {
            Controller o = (Controller) other;
            return super.equals(other);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, name, quantity, price, imageURL, Objects.hashCode(colors));
    }

}
