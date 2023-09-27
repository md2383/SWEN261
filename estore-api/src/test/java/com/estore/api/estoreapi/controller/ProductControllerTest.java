package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Mouse;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Review;
import com.estore.api.estoreapi.persistence.ProductDAO;

/**
 * Test the Product Controller class
 * 
 * @author SWEN Faculty
 */
@Tag("Controller-tier")
public class ProductControllerTest {
    private ProductController productController;
    private ProductDAO mockProductDAO;

    /**
     * Before each test, create a new ProductController object and inject
     * a mock Product DAO
     */
    @BeforeEach
    public void setupProductController() {
        mockProductDAO = mock(ProductDAO.class);
        productController = new ProductController(mockProductDAO);
    }

    @Test
    public void testGetProduct() throws IOException { // getProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null);
        // When the same id is passed in, our mock Product DAO will return the Product
        // object
        when(mockProductDAO.getProductById(mos.getId())).thenReturn(mos);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(mos.getId());

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mos, response.getBody());
    }

    @Test
    public void testGetProductNotFound() throws Exception { // createProduct may throw IOException
        // Setup
        int productId = 99;
        // When the same id is passed in, our mock Product DAO will return null,
        // simulating
        // no product found
        when(mockProductDAO.getProductById(productId)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(productId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetProductHandleException() throws Exception { // createProduct may throw IOException
        // Setup
        int productId = 99;
        // When getProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getProductById(productId);

        // Invoke
        ResponseEntity<Product> response = productController.getProduct(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all ProductController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateProduct() throws IOException { // createProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null);
        // when createProduct is called, return true simulating successful
        // creation and save
        when(mockProductDAO.createProduct(mos)).thenReturn(mos);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(mos);

        // Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mos, response.getBody());
    }

    @Test
    public void testCreateProductFailed() throws IOException { // createProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null);
        // when createProduct is called, return false simulating failed
        // creation and save
        when(mockProductDAO.createProduct(mos)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(mos);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateProductHandleException() throws IOException { // createProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null);
        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).createProduct(mos);

        // Invoke
        ResponseEntity<Product> response = productController.createProduct(mos);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateProduct() throws IOException { // updateProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null); // when updateProduct is called, return true
                                                                    // simulating successful
        // update and save
        when(mockProductDAO.updateProduct(mos)).thenReturn(mos);
        ResponseEntity<Product> response = productController.updateProduct(mos);
        mos.setName("Bolt");

        // Invoke
        response = productController.updateProduct(mos);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mos, response.getBody());
    }

    @Test
    public void testUpdateProductFailed() throws IOException { // updateProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null); // when updateProduct is called, return true
                                                                    // simulating successful
        // update and save
        when(mockProductDAO.updateProduct(mos)).thenReturn(null);

        // Invoke
        ResponseEntity<Product> response = productController.updateProduct(mos);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateProductHandleException() throws IOException { // updateProduct may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Mouse mos = new Mouse(99, colors, "red snake", 0, 0, null); // When updateProduct is called on the Mock Product
                                                                    // DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).updateProduct(mos);

        // Invoke
        ResponseEntity<Product> response = productController.updateProduct(mos);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetProductes() throws IOException { // getProductes may throw IOException
        // Setup
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Product[] productes = new Product[2];
        productes[0] = new Mouse(99, colors, "red snake", 0, 0, null);
        productes[1] = new Mouse(100, colors, "red panda", 0, 0, null);
        // When getProductes is called return the productes created above
        when(mockProductDAO.getProducts()).thenReturn(productes);

        // Invoke
        ResponseEntity<Product[]> response = productController.getAllProduct();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productes, response.getBody());
    }

    @Test
    public void testGetProductesHandleException() throws IOException { // getProductes may throw IOException
        // Setup
        // When getProductes is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getProducts();

        // Invoke
        ResponseEntity<Product[]> response = productController.getAllProduct();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchProductes() throws IOException { // findProductes may throw IOException
        // Setup
        String searchString = "red";
        Color red = new Color("Red");
        List<Color> colors = new ArrayList<Color>();
        colors.add(red);
        Product[] productes = new Product[2];
        productes[0] = new Mouse(99, colors, "red snake", 0, 0, null);
        productes[1] = new Mouse(100, colors, "red panda", 0, 0, null);
        // When findProductes is called with the search string, return the two
        /// productes above
        when(mockProductDAO.searchForProduct(searchString)).thenReturn(productes);

        // Invoke
        ResponseEntity<Product[]> response = productController.searchForProduct(searchString);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productes, response.getBody());
    }

    @Test
    public void testSearchProductesHandleException() throws IOException { // findProductes may throw IOException
        // Setup
        String searchString = "adfdafdafda";
        // When createProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).searchForProduct(searchString);

        // Invoke
        ResponseEntity<Product[]> response = productController.searchForProduct(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // when deleteProduct is called return true, simulating successful deletion
        when(mockProductDAO.deleteProduct(productId)).thenReturn(true);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProductNotFound() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // when deleteProduct is called return false, simulating failed deletion
        when(mockProductDAO.deleteProduct(productId)).thenReturn(false);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteProductHandleException() throws IOException { // deleteProduct may throw IOException
        // Setup
        int productId = 99;
        // When deleteProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).deleteProduct(productId);

        // Invoke
        ResponseEntity<Product> response = productController.deleteProduct(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testgetAllColors() throws IOException {
        Color[] colors = new Color[] { new Color("Red"), new Color("Green"), new Color("Blue") };
        when(mockProductDAO.getAllColors()).thenReturn(colors);

        ResponseEntity<Color[]> responseEntity = productController.getAllColors();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertArrayEquals(colors, responseEntity.getBody());
    }

    @Test
    public void testgetAllColorsHandleException() throws IOException {
        doThrow(new IOException()).when(mockProductDAO).getAllColors();

        ResponseEntity<Color[]> responseEntity = productController.getAllColors();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testSetAllColors() throws IOException {
        // Arrange
        Color[] colors = new Color[] { new Color("Red"), new Color("Green"), new Color("Blue") };
        when(mockProductDAO.setAllColors(colors)).thenReturn(colors);

        // Act
        ResponseEntity<Color[]> response = productController.setAllColors(colors);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(colors, response.getBody());
    }

    @Test
    public void testSetAllColorsHandleException() throws Exception {
        Color[] colors = new Color[] { new Color("Red"), new Color("Green"), new Color("Blue") };
        doThrow(new IOException()).when(mockProductDAO).setAllColors(colors);

        ResponseEntity<Color[]> responseEntity = productController.setAllColors(colors);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testaddColors() throws IOException {
        // Create a new color to be added
        Color color = new Color("orange");

        // Call the addColors method of the controller
        ResponseEntity<Color[]> response = productController.addColors(color);

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned array of colors is not null
        assertEquals(null, response.getBody());
    }
    
    @Test
    public void testaddColorsHandleException() throws IOException {
        // Create a new color to be added
        Color color = new Color("orange");

        // When addColors is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).addToAllColors(color);

        // Invoke
        ResponseEntity<Color[]> response = productController.addColors(color);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveColors() {
        // Create a new color to be removed
        Color color = new Color("orange");

        // Call the removeColors method of the controller
        ResponseEntity<Color[]> response = productController.removeColors(color);

        // Assert that the response status code is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the returned array of colors is not null
        assertEquals(null, response.getBody());
    }

    @Test
    public void testRemoveColorsHandleException() throws IOException {
        // Create a new color to be removed
        Color color = new Color("orange");

        // When removeColors is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).removeFromAllColors(color);

        // Invoke
        ResponseEntity<Color[]> response = productController.removeColors(color);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetColors() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};
        when(mockProductDAO.getProductColors(productId)).thenReturn(colors);

        // Invoke
        ResponseEntity<Color[]> response = productController.getColors(productId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(colors, response.getBody());
    }

    @Test
    public void testGetColorsHandleException() throws IOException {
        // Setup
        int productId = 123;

        // When getProductColors is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getProductColors(productId);

        // Invoke
        ResponseEntity<Color[]> response = productController.getColors(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSetProductColors() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};
        when(mockProductDAO.setProductColors(productId, colors)).thenReturn(colors);

        // Invoke
        ResponseEntity<Color[]> response = productController.setProductColors(productId, colors);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(colors, response.getBody());
    }

    @Test
    public void testSetProductColorsHandleException() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};

        // When setProductColors is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).setProductColors(productId, colors);

        // Invoke
        ResponseEntity<Color[]> response = productController.setProductColors(productId, colors);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddProductColors() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};
        when(mockProductDAO.addProductColor(productId, colors[0])).thenReturn(colors);

        // Invoke
        ResponseEntity<Color[]> response = productController.addProductColors(productId, colors[0]);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(colors, response.getBody());
    }

    @Test
    public void testAddProductColorsHandleException() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};

        // When addProductColor is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).addProductColor(productId, colors[0]);

        // Invoke
        ResponseEntity<Color[]> response = productController.addProductColors(productId, colors[0]);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testremoveProductColors() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};
        when(mockProductDAO.removeProductColor(productId, colors[0])).thenReturn(colors);

        // Invoke
        ResponseEntity<Color[]> response = productController.removeProductColors(productId, colors[0]);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(colors, response.getBody());
    }

    @Test
    public void testremoveProductColorsHandleException() throws IOException {
        // Setup
        int productId = 123;
        Color[] colors = new Color[]{new Color("red"), new Color("green"), new Color("blue")};

        // When removeProductColor is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).removeProductColor(productId, colors[0]);

        // Invoke
        ResponseEntity<Color[]> response = productController.removeProductColors(productId, colors[0]);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAllReviews() throws IOException {
        // Setup
        Review[] reviews = new Review[]{new Review(1, 1, 4, "review1"),new Review(1, 1, 4, "review2"), new Review(1, 1, 4, "review3")};
        when(mockProductDAO.getAllReviews()).thenReturn(reviews);

        // Invoke
        ResponseEntity<Review[]> response = productController.getAllReviews();

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    public void testGetAllReviewsHandleException() throws IOException {
        // Setup

        // When getAllReviews is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getAllReviews();

        // Invoke
        ResponseEntity<Review[]> response = productController.getAllReviews();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateReview() throws IOException {
        // Setup
        Review review = new Review(1, 1, 4, "review1");
        when(mockProductDAO.createReview(review)).thenReturn(review);

        // Invoke
        ResponseEntity<Review> response = productController.createReview(review);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(review, response.getBody());
    }

    @Test
    public void testCreateReviewConflict() throws IOException {
        // Setup
        Review review = new Review(1, 1, 4, "review1");
        when(mockProductDAO.createReview(review)).thenReturn(null);

        // Invoke
        ResponseEntity<Review> response = productController.createReview(review);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateReviewHandleException() throws IOException {
        // Setup
        Review review = new Review(1, 1, 4, "review1");

        // When createReview is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).createReview(review);

        // Invoke
        ResponseEntity<Review> response = productController.createReview(review);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetReviewByProductId() throws IOException {
        // Setup
        int productId = 123;
        Review[] reviews = new Review[]{new Review(1, 1, 4, "review1"),new Review(1, 1, 4, "review2"), new Review(1, 1, 4, "review3")};
        when(mockProductDAO.getReviewsByProduct(productId)).thenReturn(reviews);

        // Invoke
        ResponseEntity<Review[]> response = productController.getReviewByProductId(productId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    public void testGetReviewByProductIdNotFound() throws IOException {
        // Setup
        int productId = 123;
        Review[] reviews = new Review[]{};
        when(mockProductDAO.getReviewsByProduct(productId)).thenReturn(null);

        // Invoke
        ResponseEntity<Review[]> response = productController.getReviewByProductId(productId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetReviewByProductIdHandleException() throws IOException {
        // Setup
        int productId = 123;

        // When getReviewsByProduct is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getReviewsByProduct(productId);

        // Invoke
        ResponseEntity<Review[]> response = productController.getReviewByProductId(productId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetReviewByUserId() throws IOException {
        // Setup
        int userId = 123;
        Review[] reviews = new Review[]{new Review(1, 1, 4, "review1"),new Review(1, 1, 4, "review2"), new Review(1, 1, 4, "review3")};
        when(mockProductDAO.getReviewsByUser(userId)).thenReturn(reviews);

        // Invoke
        ResponseEntity<Review[]> response = productController.getReviewByUserId(userId);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    public void testGetReviewByUserIdHandleException() throws IOException {
        // Setup
        int userId = 123;

        // When getReviewsByUser is called on the Mock Product DAO, throw an IOException
        doThrow(new IOException()).when(mockProductDAO).getReviewsByUser(userId);

        // Invoke
        ResponseEntity<Review[]> response = productController.getReviewByUserId(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
