package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "productType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Mouse.class, name = "MOUSE"),
        @JsonSubTypes.Type(value = Keyboard.class, name = "KEYBOARD"),
        @JsonSubTypes.Type(value = Headset.class, name = "HEADSET"),
        @JsonSubTypes.Type(value = Mic.class, name = "MIC"),
        @JsonSubTypes.Type(value = Controller.class, name = "CONTROLLER"),
        @JsonSubTypes.Type(value = Speaker.class, name = "SPEAKER"),
        @JsonSubTypes.Type(value = Webcam.class, name = "WEBCAM"),
})


public abstract class Product {
    protected List<Color> colors;
    protected int id;
    public String name;
    protected int quantity;
    protected double price;
    private String description;
    protected String imageURL;
    private List<Review> reviews;

    public Product() {
        this.id = 0;
        this.name = "";
        this.quantity = 0;
        this.price = 0.0;
        this.description = "";
        this.imageURL = "";
        this.reviews = new ArrayList<>();
        this.colors = new ArrayList<>();
    }

    /**
     * Create a product with an id, color, name, quantity, price, productType, description and imageUrl 
     * @param id  the id of the product
     * @param color  the color of the product
     * @param name  the name of the product
     * @param quantity  the quantity of the product
     * @param price  the price of the product
     * @param productType  the productType of the product
     * @param description  the description of the product
     * @param imageUrl  the imageUrl of the product
     */
    public Product(int id, List<Color> colors, String name, int quantity, double price, ProductType productType,
            String description, String imageURL) {
        this.id = id;
        this.name = name;
        this.colors = colors;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.imageURL = imageURL;
        this.reviews = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public List<Color> getAllColors() {
        return colors;
    }

    /**
     * setting up all of the colors 
     * @param colors list of Color 
     */
    public void setAllColors(Color[] colors) {
        this.colors.clear();
        for (Color c : colors) {
            this.colors.add(c);
        }
    }

    public void addColor(Color color) {
        this.colors.add(color);
    }

    public void removeColor(Color color) {
        this.colors.remove(color);
    }

    /**
     * get the color if exist  
     * @param s name of color 
     * @return the Color of that color 
     */
    public Color getColor(String s) {
        for (Color c : colors) {
            if (c.getName().equals(s)) {
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return this.imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDescription() {
        return this.description;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    // public void removeReview(Review review) {
    //     for (Review r : this.reviews) {
    //         if (r.userid == review.userid && r.productid == review.productid && r.rating == review.rating
    //                 && r.review.equals(review.review)) {
    //             this.reviews.remove(r);
    //             return;
    //         }
    //     }
    // }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Get the product type
     *
     * @return the type of product
     */
    public abstract ProductType getProductType();

    /**
     * Creates a copy of this product, and sets id equal to the one passed as an argument
     *
     * @param id - The id to copy this product with
     * @return a copy of this product
     */
    public abstract Product copy(int id);

    @Override
    public boolean equals(Object other) {
        if (other instanceof Product) {
            Product o = (Product) other;
            return o.name.equals(this.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, price, imageURL, Objects.hashCode(colors));
    }
    
}
