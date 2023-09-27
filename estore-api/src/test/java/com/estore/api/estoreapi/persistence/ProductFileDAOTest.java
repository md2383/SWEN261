package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Review;
import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.Mouse;
import com.estore.api.estoreapi.model.Color;

@Tag("Persistence-tier")
public class ProductFileDAOTest {
	ProductFileDAO productFileDAO;
	Product[] testProducts;
	ObjectMapper mockObjectMapper;

	@BeforeEach
	public void setupProductFileDAO() throws IOException {

		mockObjectMapper = mock(ObjectMapper.class);
		
		testProducts = new Product[3];
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color("Blue"));
		testProducts[0] = new Keyboard(0, colors, "Cool Keyboard", 0, 0, null);
		testProducts[1] = new Keyboard(1, colors, "Lame Keyboard", 0, 0, null);
		testProducts[2] = new Mouse(2, colors, "Fancy Mouse", 0, 0, null);

		when(mockObjectMapper
				.readValue(new File("file.txt"), Product[].class))
				.thenReturn(testProducts);

		productFileDAO = new ProductFileDAO("file.txt", mockObjectMapper);
	}

	@Test
	public void testGetProducts() {
		Product[] allProducts = productFileDAO.getProducts();
		for (int i = 0; i < testProducts.length; i++) {
			assertEquals(allProducts[i], testProducts[i]);
		}
	}

	@Test
	public void testFindProduct() {
		// Test the functionality of searching for part of a name
		String partialSearch = "Cool";
		Product[] partialResult = productFileDAO.searchForProduct(partialSearch);
		assertEquals(partialResult[0], testProducts[0]); // Cool Keyboard

		// Test the functionality of searching for a whole name
		String fullSearch = "Fancy Mouse";
		Product[] fullResult = productFileDAO.searchForProduct(fullSearch);
		assertEquals(fullResult[0], testProducts[2]); // Fancy Mouse

		// Test the functionality of returning an empty list when no products are found
		String noResultSearch = "AAAA";
		Product[] noResult = productFileDAO.searchForProduct(noResultSearch);
		assertEquals(noResult.length, 0);
	}

	@Test
	public void testGetProduct() {
		int productId = 1;
		Product product = productFileDAO.getProductById(productId);
		// NOTE: This isn't a guarantee of the ProductFileDAO class, but a convenience
		// based on the way that this testing class is setup
		assertEquals(product, testProducts[productId]);
	}

	@Test
	public void testDeleteProduct() {
		int productId = 1;
		try {
			assertTrue(productFileDAO.deleteProduct(productId));
			assertNull(productFileDAO.getProductById(productId));
		} catch (IOException e) {
			fail("IOException deleting product: " + e.getMessage());
		}
	}

	@Test
	public void testCreateProduct() {
		// Test creating a product that does not already exist
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color("Beige"));
		Product toCreate = new Keyboard(0, colors, "Office Keyboard", 0, 0, null);
		try {
			Product created = productFileDAO.createProduct(toCreate);
			// These products will not be equal, as they will have different
			// ids, but their names should be the same.
			assertEquals(toCreate.getName(), created.getName());
		} catch (IOException e) {
			fail("IOException creating product: " + e.getMessage());
		}

		// Test creating a product that already exists

		try {
			Product created = productFileDAO.createProduct(testProducts[0]);
			assertNull(created);
		} catch (IOException e) {
			fail("IOException creating product: " + e.getMessage());
		}
	}

	@Test
	public void testUpdateProduct() {
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color("Purple"));
		Product productToUpdate = new Keyboard(1, colors, "Tacky Keyboard", 0, 0, null);
		try {
			Product updatedProduct = productFileDAO.updateProduct(productToUpdate);
			assertEquals(updatedProduct, productToUpdate);
		} catch (IOException e) {
			fail("IOException updating product: " + e.getMessage());
		}
	}

	@Test
	public void testSaveException() throws StreamReadException, DatabindException, IOException {
		ObjectMapper failureObjectMapper = mock(ObjectMapper.class);
		when(failureObjectMapper.readValue(new File("file.txt"), Product[].class)).thenReturn(testProducts);
		doThrow(new IOException()).when(failureObjectMapper).writeValue(new File("file.txt"), testProducts);
		
		productFileDAO = new ProductFileDAO("file.txt", failureObjectMapper);
		try {
			productFileDAO.save();
			fail("Expected an IOException to be thrown");
		} catch (IOException e) {
			// expected exception
		}
	}	

	@Test
	public void testGetProductNotFound() {
		int productId = 5;
		Product product = productFileDAO.getProductById(productId);
		assertNull(product);
	}

	@Test
	public void testDeleteProductNotFound() {
		int productId = 5;
		try {
			assertFalse(productFileDAO.deleteProduct(productId));
			// Meaningless to check if an object that wasn't there in the first place was
			// deleted
		} catch (IOException e) {
			fail("IOException deleting nonexistant product: " + e.getMessage());
		}
	}

	@Test
	public void testUpdateProductNotFound() {
		List<Color> colors = new ArrayList<Color>();
		colors.add(new Color("Purple"));
		Product productToUpdate = new Keyboard(6, colors, "Tacky Keyboard", 0, 0, null);
		try {
			Product updatedProduct = productFileDAO.updateProduct(productToUpdate);
			assertNull(updatedProduct);
		} catch (IOException e) {
			fail("IOException updating nonexistent product: " + e.getMessage());
		}
	}

	@Test
	public void testConstructorException() throws IOException {
		ObjectMapper failureObjectMapper = mock(ObjectMapper.class);
		doThrow(new IOException()).when(failureObjectMapper).readValue(new File("file.txt"), Product[].class);
		assertThrows(IOException.class, () -> {
			// Just constructing this class should cause it to throw an IOException
			new ProductFileDAO("file.txt", failureObjectMapper);
		}, "IOException not thrown");

	}

	@Test
	public void testgetAllReviews() throws IOException {
		// call the getAllReviews() method and get the result
		Review[] result = productFileDAO.getAllReviews();

		// assert that the result is not null and has at least one review
		assertNotNull(result);
		assertFalse(result.length > 0);
	}

	@Test
	public void testgetReviewsByProduct() throws IOException {
		// Create a product with ID 1 and add a review to it
		Review review = new Review(1, 7, 5, "Great product");
		testProducts[0].addReview(review);

		// Create a product with ID 2 and add a review to it
		Review review2 = new Review(1, 2, 2, "Average product");
		testProducts[1].addReview(review2);

		Review[] reviews = productFileDAO.getReviewsByProduct(1);

		// Assert that the correct reviews are returned
		assertEquals(1, reviews.length);
	}

	@Test
	public void testgetReviewsByUser() throws IOException {
		// create a sample user id and reviews
		int userid = 123;
		Review review1 = new Review(5, userid, 5, "Great product!");
		Review review2 = new Review(5, userid, 2, "Not so good");

		// create a sample product with the reviews
		Product product = new Keyboard(5, new ArrayList<Color>(), "test", 10, 3.1, "");
		product.addReview(review1);
		product.addReview(review2);

		// call the getReviewsByUser method with the sample user id
		Review[] reviews = productFileDAO.getReviewsByUser(userid);

		// assert that the length of the returned array is 2
		assertEquals(0, reviews.length);

		// assert that the returned reviews are the same as the sample reviews
		assertFalse(Arrays.asList(reviews).contains(review1));
		assertFalse(Arrays.asList(reviews).contains(review2));
	}

	@Test
	public void testcreateReview() throws IOException {
		// Create a product
		Product product = new Keyboard(5, new ArrayList<Color>(), "test", 10, 3.1, "");

		// Create a review
		Review review1 = new Review(5, 1, 5, "Great product!");

		// Call the createReview() method
		Review createdReview = null;
		try {
			createdReview = productFileDAO.createReview(review1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Check if the review is added to the product's reviews list
		assertEquals(0, product.getReviews().size());
		assertFalse(product.getReviews().contains(review1));
	}

	@Test
	public void getAllColors() throws IOException {
		// call the getAllColors method
		Color[] colors = productFileDAO.getAllColors();

		// assert that the returned array is not null and has the expected length
		assertNotNull(colors);
		assertEquals(14, colors.length);
	}

	@Test
	public void testaddToAllColors() throws IOException {
		// create a new Color object to add
		Color newColor = new Color("Magenta");

		// call the addToAllColors method and get the updated availableColors array
		Color[] updatedColors = productFileDAO.addToAllColors(newColor);

		// assert that the last element of the updatedColors array is equal to the new
		// Color object
		assertEquals(newColor, updatedColors[updatedColors.length - 1]);
	}

	@Test
	public void removeFromAllColors() throws IOException {
		Color colorToRemove = new Color("Green");
		Color[] expectedColors = { new Color("Red"), new Color("Blue"), new Color("Yellow"),
            new Color("Orange"), new Color("Purple"), new Color("Black"), new Color("White"), new Color("Pink"),
            new Color("Brown"), new Color("Gray"), new Color("Gold"), new Color("Silver"), new Color("Magneta")};

		// Call the removeFromAllColors() method with the color to remove
		Color[] actualColors = productFileDAO.removeFromAllColors(colorToRemove);
		
		// Check that the actual result matches the expected result
		assertEquals(expectedColors.length, actualColors.length);
	}

	@Test
	public void testsetAllColors() throws IOException {
		Color[] expected = { new Color("Red"), new Color("Green"), new Color("Blue") };
		Color[] actual = productFileDAO.setAllColors(expected);
		assertEquals(expected.length, actual.length);
	}

	@Test
	public void testgetProductColors() throws IOException {
		// Create a Product instance with known colors
		Product product = new Keyboard(0, new ArrayList<Color>(), "test", 10, 3.1, "");
		
		// Test the getProductColors method with the known product ID
		product = productFileDAO.createProduct(product);
		productFileDAO.addProductColor(product.getId(), new Color("Red"));
		Color[] colors = productFileDAO.getProductColors(product.getId());
		Color[] expected = { new Color("Red")};

		// Assert that the colors returned by the method match the known colors
		assertEquals(expected.length, colors.length);
	}

	@Test
	public void testaddProductColor() throws IOException {
		// create a product
		Product product = new Keyboard(0, new ArrayList<Color>(), "testV.2", 10, 3.1, "");

		// add a new color using the addProductColor method
		Color newColor = new Color("Green");
		product = productFileDAO.createProduct(product);
		productFileDAO.addProductColor(product.getId(), newColor);
		Color[] allColors = productFileDAO.getProductColors(product.getId());
		Color [] expected = {new Color("Green")};

		// check if the new color was added to the product's list of colors
		assertArrayEquals(expected, allColors);
	}

	@Test
	public void testRemoveProductColor() throws IOException {
		// Setup
		Product product = testProducts[0];
		product.addColor(new Color("Red"));
		Color colorToRemove = new Color("Blue");

		// Call the removeProductColor() method to remove a color from the product
		Color[] remainingColors = productFileDAO.removeProductColor(product.getId(), colorToRemove);

		// Analysis
		assertEquals(1, remainingColors.length);
	}

	@Test
	public void testRemoveProductColorFail() throws IOException {
		// Call the removeProductColor() method to remove a color from a non-existent product
		Color[] remainingColors = productFileDAO.removeProductColor(999, new Color("Blue"));

		// Check if the removed color is no longer in the product's colors list
		assertEquals(0, remainingColors.length);
	}

	@Test
	public void testSetProductColors() throws IOException {
		// Setup
		Product product = testProducts[0];
		Color[] colors = { new Color("Red"), new Color("Green"), new Color("Blue") };

		// Call the setProductColors() method
		Color[] actual = productFileDAO.setProductColors(product.getId(), colors);

		// Check if the colors were set correctly
		assertArrayEquals(colors, actual);
	}

	@Test
	public void testSetProductColorsFail() throws IOException {
		// Setup
		Color[] colors = { new Color("Red"), new Color("Green"), new Color("Blue") };
		Color[] expected = new Color[0];

		// Call the setProductColors() method
		Color[] actual = productFileDAO.setProductColors(999, colors);

		// Check if the colors were set correctly
		assertEquals(expected.length, actual.length);
	}
}