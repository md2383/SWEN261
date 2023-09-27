package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MouseTest {
    private Mouse mouse;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        mouse = new Mouse(1, colors, "Test Mouse", 10, 19.99, "https://example.com/mouse.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = mouse.getProductType();

        // Assert
        assertEquals(ProductType.MOUSE, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Mouse copiedMouse;

        // Act
        copiedMouse = (Mouse) mouse.copy(2);

        // Assert
        assertNotNull(copiedMouse);
        assertEquals(2, copiedMouse.getId());
        assertEquals(mouse.getName(), copiedMouse.getName());
        assertEquals(mouse.getQuantity(), copiedMouse.getQuantity());
        assertEquals(mouse.getPrice(), copiedMouse.getPrice());
        assertEquals(mouse.getImageURL(), copiedMouse.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Mouse sameMouse = new Mouse(mouse.getAllColors(), mouse.getName());
        Mouse differentMouse = new Mouse(mouse.getAllColors(), "Different Mouse");

        // Assert
        assertTrue(mouse.equals(sameMouse));
        assertFalse(mouse.equals(differentMouse));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(mouse.equals(null));
    }
}
