package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Keyboard extends Product {
    /**
     * Constructor for keyboard 
     * 
     */
    public Keyboard(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product", imageURL);
    }
    
    @JsonCreator
    public Keyboard(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }

    /**
     * Private Constructor for keyboard 
     * 
     */
    private Keyboard(Keyboard k, int id) {
        this(id, k.getAllColors(), k.getName(), k.getQuantity(), k.getPrice(), k.getImageURL());
    }

     /**
     * retrieve the product type 
     * @return ProductType of keyboard 
     */
    @Override
    public ProductType getProductType() {
        return ProductType.KEYBOARD;
    }

    /**
     * copies the keyboard  
     * @param id int id 
     * @return Product of a keyboard  
     */
    @Override
    public Product copy(int id) {
		return new Keyboard(this, id);
	}

     /**
     * compares keyboard  
     * @param other object of keyboard  
     * @return boolean of comparison 
     */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Keyboard) {
			Keyboard o = (Keyboard) other;
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
