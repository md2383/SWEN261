package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mic extends Product {
    /**
     * Constructor for Microphone  
     * 
     */
    public Mic(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product", imageURL);
    }
    
    @JsonCreator
    public Mic(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }
    /**
     * Private Constructor for Microphone  
     * 
     */
    private Mic(Mic m, int id) {
        this(id, m.getAllColors(), m.getName(), m.getQuantity(), m.getPrice(), m.getImageURL());
    }

    /**
     * retrieve the product type 
     * @return ProductType of Microphone 
     */
    @Override
    public ProductType getProductType() {
        return ProductType.MIC;
    }

    /**
     * copies the Microphone  
     * @param id int id 
     * @return Product of a Microphone  
     */
    @Override
    public Product copy(int id) {
		return new Mic(this, id);
	}

    /**
     * compares Microphone  
     * @param other object of Microphone  
     * @return boolean of comparison 
     */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Mic) {
			Mic o = (Mic) other;
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
