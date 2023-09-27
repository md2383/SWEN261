package com.estore.api.estoreapi.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Mouse extends Product {
    public Mouse(int id, List<Color> colors, String name, int quantity, double price, String imageURL) {
        super(id, colors, name, quantity, price, ProductType.MOUSE, "This is a description of the product", imageURL);
    }

    @JsonCreator
    public Mouse(
            @JsonProperty("color") List<Color> colors,
            @JsonProperty("name") String name) {
        this(-1, colors, name, 0, 0, "");
    }

    private Mouse(Mouse m, int id) {
        this(id, m.getAllColors(), m.getName(), m.getQuantity(), m.getPrice(), m.getImageURL());
    }

    /**
     * retrieve the product type 
     * @return ProductType of Mouse 
     */
    @Override
    public ProductType getProductType() {
        return ProductType.MOUSE;
    }

    /**
     * copies the Mouse  
     * @param id int id 
     * @return Product of a Mouse  
     */
	@Override
	public Product copy(int id) {
		return new Mouse(this, id);
	}

    /**
     * compares Mouse  
     * @param other object of Mouse  
     * @return boolean of comparison 
     */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Mouse) {
			Mouse o = (Mouse) other;
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
