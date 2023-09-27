package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Controller controller;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        controller = new Controller(1, colors, "Test Controller", 5, 49.99, "https://example.com/controller.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = controller.getProductType();

        // Assert
        assertEquals(ProductType.CONTROLLER, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Controller copiedController;

        // Act
        copiedController = (Controller) controller.copy(2);

        // Assert
        assertNotNull(copiedController);
        assertEquals(2, copiedController.getId());
        assertEquals(controller.getName(), copiedController.getName());
        assertEquals(controller.getQuantity(), copiedController.getQuantity());
        assertEquals(controller.getPrice(), copiedController.getPrice());
        assertEquals(controller.getImageURL(), copiedController.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Controller sameController = new Controller(controller.getAllColors(), controller.getName());
        Controller differentController = new Controller(controller.getAllColors(), "Different Controller");

        // Assert
        assertTrue(controller.equals(sameController));
        assertFalse(controller.equals(differentController));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(controller.equals(null));
    }

    @Test
    public void testHashCode() {
        List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
        List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
        Controller controller1 = new Controller(1, colors1, "Controller 1", 5, 49.99, "https://example.com/controller1.jpg");
        Controller controller2 = new Controller(2, colors2, "Controller 2", 10, 99.99, "https://example.com/controller2.jpg");
        Controller controller3 = new Controller(3, colors1, "Controller 3", 3, 29.99, "https://example.com/controller3.jpg");

        // Act
        int hash1 = controller1.hashCode();
        int hash2 = controller2.hashCode();
        int hash3 = controller3.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }
}
