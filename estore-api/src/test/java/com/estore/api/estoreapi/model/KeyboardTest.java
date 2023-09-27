package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KeyboardTest {
    private Keyboard keyboard;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        keyboard = new Keyboard(1, colors, "Test Keyboard", 5, 49.99, "https://example.com/keyboard.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = keyboard.getProductType();

        // Assert
        assertEquals(ProductType.KEYBOARD, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Keyboard copiedKeyboard;

        // Act
        copiedKeyboard = (Keyboard) keyboard.copy(2);

        // Assert
        assertNotNull(copiedKeyboard);
        assertEquals(2, copiedKeyboard.getId());
        assertEquals(keyboard.getName(), copiedKeyboard.getName());
        assertEquals(keyboard.getQuantity(), copiedKeyboard.getQuantity());
        assertEquals(keyboard.getPrice(), copiedKeyboard.getPrice());
        assertEquals(keyboard.getImageURL(), copiedKeyboard.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Keyboard sameKeyboard = new Keyboard(keyboard.getAllColors(), keyboard.getName());
        Keyboard differentKeyboard = new Keyboard(keyboard.getAllColors(), "Different Keyboard");

        // Assert
        assertTrue(keyboard.equals(sameKeyboard));
        assertFalse(keyboard.equals(differentKeyboard));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(keyboard.equals(null));
    }
}
