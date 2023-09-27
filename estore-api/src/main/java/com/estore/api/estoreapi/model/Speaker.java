package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Speaker extends Product {
    /**
     * Constructor for Speaker 
     * 
     */
    public Speaker(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product", imageURL);
    }
    
    @JsonCreator
    public Speaker(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }
    
    /**
     * private Constructor for Speaker 
     * 
     */
    private Speaker(Speaker s, int id) {
        this(id, s.getAllColors(), s.getName(), s.getQuantity(), s.getPrice(), s.getImageURL());
    }

    @Override
    public ProductType getProductType() {
        return ProductType.SPEAKER;
    }

    @Override
    public Product copy(int id) {
		return new Speaker(this, id);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Speaker) {
			Speaker o = (Speaker) other;
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
