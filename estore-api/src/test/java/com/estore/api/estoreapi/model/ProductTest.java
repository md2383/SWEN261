package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Keyboard keyboard;

    @BeforeEach
    void setUp() {
        keyboard = new Keyboard(1, Arrays.asList(new Color("Black")), "Keyboard 1", 10, 50.0, "http://example.com/keyboard1.jpg");
    }

    @Test
    void testGetters() {
        assertEquals(1, keyboard.getId());
        assertEquals("Keyboard 1", keyboard.getName());
        assertEquals(10, keyboard.getQuantity());
        assertEquals(50.0, keyboard.getPrice());
        assertEquals(ProductType.KEYBOARD, keyboard.getProductType());
        assertEquals("http://example.com/keyboard1.jpg", keyboard.getImageURL());
    }

    @Test
    void testSetters() {
        keyboard.setName("New Name");
        assertEquals("New Name", keyboard.getName());

        keyboard.setQuantity(5);
        assertEquals(5, keyboard.getQuantity());

        keyboard.setImageURL("http://example.com/new_keyboard1.jpg");
        assertEquals("http://example.com/new_keyboard1.jpg", keyboard.getImageURL());

        keyboard.setDescription("New description");
        assertEquals("New description", keyboard.getDescription());

        keyboard.setAllColors(new Color[] { new Color("White"), new Color("Black") });
        assertEquals(Arrays.asList(new Color("White"), new Color("Black")), keyboard.getAllColors());

        keyboard.addColor(new Color("Red"));
        assertTrue(keyboard.getAllColors().contains(new Color("Red")));

        keyboard.removeColor(new Color("White"));
        assertFalse(keyboard.getAllColors().contains(new Color("White")));

        assertNotNull(keyboard.getColor("Black"));
        assertNull(keyboard.getColor("Blue"));

        Review review = new Review(5, 5, 5, "Great keyboard!");
        keyboard.addReview(review);
        assertTrue(keyboard.getReviews().contains(review));

    }

    @Test
    void testCopy() {
        Product copy = keyboard.copy(2);
        assertEquals(2, copy.getId());
        assertEquals(keyboard.getName(), copy.getName());
        assertEquals(keyboard.getQuantity(), copy.getQuantity());
        assertEquals(keyboard.getPrice(), copy.getPrice());
        assertEquals(keyboard.getProductType(), copy.getProductType());
        assertEquals(keyboard.getImageURL(), copy.getImageURL());
        assertEquals(keyboard.getAllColors(), copy.getAllColors());
        assertEquals(keyboard.getReviews(), copy.getReviews());
        assertEquals(keyboard.getDescription(), copy.getDescription());
        assertFalse(keyboard == copy);
    }

    @Test
    void testEquals(){
        Product p1 = new Keyboard(1, Arrays.asList(new Color("Black")), "Keyboard 1", 10, 50.0, "http://example.com/keyboard1.jpg");
        Product p2 = new Keyboard(1, Arrays.asList(new Color("Blue")), "Keyboard 2", 10, 50.0, "http://example.com/keyboard1.jpg");
        Product p3 = new Keyboard(1, Arrays.asList(new Color("Black")), "Keyboard 1", 10, 50.0, "http://example.com/keyboard1.jpg");

        // test equality with the same object
        assertTrue(p1.equals(p1));

        // test equality with different objects but same name
        assertTrue(p1.equals(p3));
        assertTrue(p3.equals(p1));
 
        // test inequality with different objects and different name
        assertFalse(p1.equals(p2));
        assertFalse(p2.equals(p1));

        // test inequality with a non-Product object
        Color c = new Color("Black");
        assertFalse(p1.equals(c));
    }

    @Test
    public void testConstructor() {
        // create a concrete subclass of Product
        Product product = new Product(69, Arrays.asList(new Color("RED"), new Color("BLUE")), "Test Product", 10, 9.99,
                ProductType.MOUSE, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.MOUSE;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };

        // verify that the instance was constructed with the expected values
        assertEquals(69, product.getId());
        List<Color> expectedColors = Arrays.asList(new Color("RED"), new Color("BLUE"));
        assertNotEquals(expectedColors, product.getAllColors());
        assertEquals("Test Product", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(9.99, product.getPrice(), 0.001);
        assertEquals(ProductType.MOUSE, product.getProductType());
        assertEquals("Test description", product.getDescription());
        assertEquals("testimage.jpg", product.getImageURL());
        assertEquals(0, product.getReviews().size());
    }
    
    @Test
    public void testDefaultConstructor() {
        // create a concrete subclass of Product
        Product product = new Product() {

            @Override
            public ProductType getProductType() {
                return null;
            }

            @Override
            public Product copy(int id) {
                return null;
            }

            @Override
            public List<Color> getAllColors() {
                return null;
            }
        };

        // verify that the instance was constructed with the expected values
        assertEquals(0, product.getId());
        List<Color> expectedColors = null;
        assertEquals(expectedColors, product.getAllColors());
        assertEquals("", product.getName());
        assertEquals(0, product.getQuantity());
        assertEquals(0, product.getPrice(), 0.001);
        assertEquals(null, product.getProductType());
        assertEquals("", product.getDescription());
        assertEquals("", product.getImageURL());
        assertEquals(0, product.getReviews().size());
    }
    
    @Test
    public void testEqualProduct() {
        // create a concrete subclass of Product
        Product product = new Product(69, Arrays.asList(new Color("RED"), new Color("BLUE")), "Test Product", 10, 9.99,
                ProductType.MOUSE, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.MOUSE;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };

        // create a second concrete subclass of Product
        Product product2 = new Product(69, Arrays.asList(new Color("RED"), new Color("BLUE")), "Test Product", 10, 9.99,
                ProductType.MOUSE, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.MOUSE;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };

        // verify that the two instances are equal
        assertTrue(product.equals(product2));
        assertFalse(product.equals(null));
    }

    @Test
    public void testHashCode() {
        List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
        List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
        Product product1 = new Product(69, colors1, "Test Product", 10, 9.99,
                ProductType.KEYBOARD, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.KEYBOARD;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };
        Product product2 = new Product(34, colors2, "Test Product", 7, 55.66,
                ProductType.CONTROLLER, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.CONTROLLER;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };
        Product product3 = new Product(123, colors1, "Test Product", 100, 99.99,
                ProductType.HEADSET, "Test description", "testimage.jpg") {
            @Override
            public ProductType getProductType() {
                return ProductType.HEADSET;
            }

            @Override
            public Product copy(int id) {
                return null;
            }
        };

        // Act
        int hash1 = product1.hashCode();
        int hash2 = product2.hashCode();
        int hash3 = product3.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }
}
