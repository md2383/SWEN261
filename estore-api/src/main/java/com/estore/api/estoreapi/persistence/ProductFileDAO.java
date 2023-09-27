package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Review;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProductFileDAO implements ProductDAO {
    private static final Logger LOG = Logger.getLogger(ProductFileDAO.class.getName());
    private Map<Integer, Product> products = null;
    static Color[] availableColors = { new Color("Red"), new Color("Blue"), new Color("Green"), new Color("Yellow"),
            new Color("Orange"), new Color("Purple"), new Color("Black"), new Color("White"), new Color("Pink"),
            new Color("Brown"), new Color("Gray"), new Color("Gold"), new Color("Silver") };

    private ObjectMapper mapper;
    private String filename;

    private static int nextId;

    public ProductFileDAO(@Value("${product.file}") String filename, ObjectMapper mapper) throws IOException {
        LOG.info(filename);
        this.filename = filename;
        this.mapper = mapper;
        load();
    }

    /**
     * Get all products as an array
     *
     * @param containsText - If not null, acts as a filter for product names
     *
     * @return the list of products matching the filter
     */
    private Product[] getProductsArray(String containsText) {
        ArrayList<Product> productsArrayList = new ArrayList<>();

        for (Product product : products.values()) {
            if (containsText == null || product.getName().contains(containsText)) {
                productsArrayList.add(product);
            }
        }
        Product[] productArray = new Product[productsArrayList.size()];
        productsArrayList.toArray(productArray);
        return productArray;
    }

    /**
     * Serialize all products currently in memory to the disk
     *
     * @return true if the products were successfully serialized and written to the
     *         disk
     */
    boolean save() throws IOException {
        Product[] productArray = getProductsArray(null);

        mapper.writeValue(new File(filename), productArray);
        return true;
    }

    /**
     * Deserialize all products from the disk
     *
     * @return true if the products were successfully deserialized and read from the
     *         disk
     */
    boolean load() throws IOException {
        products = new TreeMap<>();
        nextId = 0;
        LOG.info(filename);

        Product[] productArray = mapper.readValue(new File(filename), Product[].class);
        for (Product product : productArray) {
            int id = product.getId();
            products.put(id, product);
            if (id > nextId) {
                nextId = id;
            }
        }
        nextId++;
        return true;
    }

    /**
     * Get the next available product id
     * 
     * @return the next id
     */
    private synchronized static int getNextId() {
        int id = nextId++;
        return id;
    }

    public int getNext() {
        return nextId++;
    }

    /**
     * @return list of all the product values in the map
     */
    @Override
    public Product[] getProducts() {
        synchronized (products) {
            return (new ArrayList<>(products.values())).toArray(new Product[0]);
        }
    }

    /**
     * @param id - the id of the product to get
     * 
     * @return the product with the given id
     */
    @Override
    public Product getProductById(int id) {
        synchronized (products) {
            return products.get(id);
        }
    }

    /**
     * @param name - the name of the product to get
     * 
     * @return the product with the given name
     */
    @Override
    public Product[] searchForProduct(String name) {
        synchronized (products) {
            List<Product> searches = new ArrayList<>();
            for (Product product : products.values()) {
                if (product.getName().toUpperCase().contains(name.toUpperCase())) {
                    searches.add(product);
                }
            }
            return searches.toArray(new Product[0]);
        }
    }

    /**
     * Persists a product to the filesystem
     *
     * @param product - The product to persist to the filesystem
     *
     * @return Product - The product that has been persisted,
     *         null if there is already a product with the same name
     */
    @Override
    public Product createProduct(Product product) throws IOException {
        synchronized (products) {
            int i = getNextId();
            Color black = ProductFileDAO.availableColors[6];
            Product newProduct = product.copy(i);
            newProduct.addColor(black);
            for (Product p : products.values()) {
                LOG.info(p.getName() + " " + newProduct.getName() + " " + newProduct.getId() + " " + i);
                if (p.getName().equals(product.getName())) {
                    return null;
                }
            }
            products.put(newProduct.getId(), newProduct);
            save();
            return product;
        }
    }

    /**
     * Updates a product if found
     *
     * @param product - The product to be updated
     *
     * @return product - the updated product, null if it is not found
     */
    @Override
    public Product updateProduct(Product product) throws IOException {
        synchronized (products) {
            if (products.containsKey(product.getId()) == false) {
                return null;
            }
            products.put(product.getId(), product);
            save();
            return product;
        }
    }

    /**
     * Deletes a product if found
     *
     * @param product - The product to delete
     *
     * @return boolean - true if product deleted, false if not deleted
     */
    @Override
    public boolean deleteProduct(int id) throws IOException {
        synchronized (products) {
            if (products.containsKey(id)) {
                products.remove(id);
                return save();
            } else
                return false;
        }
    }

    /**
     * Gets ALL reviews for EVERY product
     *
     */
    @Override
    public Review[] getAllReviews() {
        synchronized (products) {
            ArrayList<Review> reviews = new ArrayList<>();
            for (Product product : products.values()) {
                for (Review review : product.getReviews()) {
                    reviews.add(review);
                }
            }
            return reviews.toArray(new Review[0]);
        }
    }

    /**
     * get a list of reviews based on product
     *
     * @param proudctid - The id of the product
     *
     * @return review - the list of reviews
     */
    @Override
    public Review[] getReviewsByProduct(int productid) throws IOException {

        Product productToReview = getProductById(productid);
        if (productToReview == null) {
            return new Review[0];
        } else {
            return productToReview.getReviews().toArray(new Review[0]);
        }
    }

    /**
     * get a list of reviews based on user
     *
     * @param userid - The id of the user
     *
     * @return review - the list of reviews
     */
    @Override
    public Review[] getReviewsByUser(int userid) throws IOException {
        ArrayList<Review> reviews = new ArrayList<>();
        for (Product product : products.values()) {
            for (Review review : product.getReviews()) {
                if (review.getUserid() == userid) {
                    reviews.add(review);
                }
            }
        }
        return reviews.toArray(new Review[0]);
    }

    /**
     * Add review to product
     *
     * @param review - The created review
     *
     * @return review - the new review
     */
    @Override
    public Review createReview(Review review) throws IOException {

        Product productToReview = getProductById(review.getProductid());

        if (productToReview == null) {
            return null;
        } else {
            productToReview.addReview(review);
            save();
            return review;
        }
    }

    /**
     * remove a review of the product
     *
     * @param Reivew - The review to be removed
     *
     * @return review - the review
     */
    // @Override
    // public Review removeReview(Review review) throws IOException {

    // Product productToRemoveReview = getProductById(review.getProductid());
    // if (productToRemoveReview == null) {
    // return null;
    // } else {
    // productToRemoveReview.removeReview(review);

    // Review temp = new Review();
    // temp.setProductid(review.getProductid());
    // temp.setUserid(review.getUserid());
    // temp.setRating(review.getRating());
    // temp.setReview(review.getReview());

    // return temp;
    // }

    // }

    // get list of all colors
    @Override
    public Color[] getAllColors() throws IOException {
        return ProductFileDAO.availableColors;
    }

    /**
     * add color to color list
     *
     * @param color - the color to be added
     *
     * @return color - the list of color
     */
    @Override
    public Color[] addToAllColors(Color color) throws IOException {
        ProductFileDAO.availableColors = Arrays.copyOf(ProductFileDAO.availableColors,
                ProductFileDAO.availableColors.length + 1);
        ProductFileDAO.availableColors[ProductFileDAO.availableColors.length - 1] = color;
        return ProductFileDAO.availableColors;
    }

    /**
     * remove color from color list
     *
     * @param color - The color to be removed
     *
     * @return color - the list of color
     */
    @Override
    public Color[] removeFromAllColors(Color color) throws IOException {
        int index = -1;
        for (int i = 0; i < ProductFileDAO.availableColors.length; i++) {
            if (ProductFileDAO.availableColors[i].equals(color)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return Arrays.copyOf(ProductFileDAO.availableColors, ProductFileDAO.availableColors.length);
        }
        for (int i = index; i < ProductFileDAO.availableColors.length - 1; i++) {
            ProductFileDAO.availableColors[i] = ProductFileDAO.availableColors[i + 1];
        }
        ProductFileDAO.availableColors = Arrays.copyOf(ProductFileDAO.availableColors,
                ProductFileDAO.availableColors.length - 1);
        return Arrays.copyOf(ProductFileDAO.availableColors, ProductFileDAO.availableColors.length);
    }

    /**
     * set all colors avaliable
     *
     * @param colors - list of colors
     *
     * @return color - the list of colors
     */
    @Override
    public Color[] setAllColors(Color[] colors) throws IOException {
        ProductFileDAO.availableColors = colors != null ? colors : new Color[0];
        return ProductFileDAO.availableColors != null ? ProductFileDAO.availableColors : new Color[] {};
    }

    /**
     * get the list of colors of a product
     *
     * @param proudctid - The id of the product
     *
     * @return color - the list of color
     */
    @Override
    public Color[] getProductColors(int productid) throws IOException {
        synchronized (products) {
            for (Product product : products.values()) {
                if (product.getId() == productid) {
                    return product.getAllColors().toArray(new Color[0]);
                }
            }
            return new Color[0];
        }
    }

    /**
     * add a color to product color list
     *
     * @param proudctid - The id of the product, color the color to be added
     *
     * @return color - the list of color
     */
    @Override
    public Color[] addProductColor(int productid, Color color) throws IOException {
        synchronized (products) {
            for (Product product : products.values()) {
                if (product.getId() == productid) {
                    product.addColor(color);
                    save();
                    return product.getAllColors().toArray(new Color[0]);
                }
            }
            return new Color[0];
        }
    }

    /**
     * remove a color to product color list
     *
     * @param proudctid - The id of the product, color the color to be removed
     *
     * @return color - the list of color
     */
    @Override
    public Color[] removeProductColor(int productid, Color color) throws IOException {
        synchronized (products) {
            for (Product product : products.values()) {
                if (product.getId() == productid) {
                    product.removeColor(color);
                    save();
                    return product.getAllColors().toArray(new Color[0]);
                }
            }
            return new Color[0];
        }
    }

    /**
     * set product color list
     *
     * @param proudctid - The id of the product, color list of colors
     *
     * @return color - the list of color
     */
    @Override
    public Color[] setProductColors(int productid, Color[] colors) throws IOException {
        synchronized (products) {
            for (Product product : products.values()) {
                if (product.getId() == productid) {
                    product.setAllColors(colors);
                    save();
                    return product.getAllColors().toArray(new Color[0]);
                }
            }
            return new Color[0];
        }
    }

}
