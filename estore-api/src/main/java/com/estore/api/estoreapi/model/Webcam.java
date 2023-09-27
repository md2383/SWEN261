package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Webcam extends Product {
    /**
     * Constructor for webcam 
     * 
     */
    public Webcam(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.KEYBOARD, "This is a description of the product", imageURL);
    }
    
    @JsonCreator
    public Webcam(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }

    /**
     * private Constructor for webcam 
     * 
     */
    private Webcam(Webcam w, int id) {
        this(id, w.getAllColors(), w.getName(), w.getQuantity(), w.getPrice(), w.getImageURL());
    }

    @Override
    public ProductType getProductType() {
        return ProductType.WEBCAM;
    }

    @Override
    public Product copy(int id) {
		return new Webcam(this, id);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Webcam) {
			Webcam o = (Webcam) other;
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
