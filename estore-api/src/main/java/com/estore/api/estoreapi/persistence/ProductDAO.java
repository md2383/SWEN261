package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Review;

public interface ProductDAO {
    Product[] getProducts() throws IOException;

    Product getProductById(int id) throws IOException;

    Product[] searchForProduct(String name) throws IOException;

    Product createProduct(Product product) throws IOException;

    Product updateProduct(Product product) throws IOException;

    boolean deleteProduct(int id) throws IOException;

    Color[] getAllColors() throws IOException;

    Color[] addToAllColors(Color color) throws IOException;

    Color[] removeFromAllColors(Color color) throws IOException;

    Color[] setAllColors(Color[] colors) throws IOException;

    Color[] getProductColors(int productid) throws IOException;

    Color[] addProductColor(int productid, Color color) throws IOException;

    Color[] removeProductColor(int productid, Color color) throws IOException;

    Color[] setProductColors(int productid, Color[] colors) throws IOException;

    Review[] getAllReviews() throws IOException;

    Review[] getReviewsByProduct(int productid) throws IOException;

    Review[] getReviewsByUser(int userid) throws IOException;

    Review createReview(Review review) throws IOException;

    // Review removeReview(Review review) throws IOException;

}
