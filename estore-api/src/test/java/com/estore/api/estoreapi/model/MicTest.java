package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MicTest {
    private Mic mic;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        mic = new Mic(1, colors, "Test Keyboard", 5, 49.99, "https://example.com/keyboard.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = mic.getProductType();

        // Assert
        assertEquals(ProductType.MIC, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Mic copiedMic;

        // Act
        copiedMic = (Mic) mic.copy(2);

        // Assert
        assertNotNull(copiedMic);
        assertEquals(2, copiedMic.getId());
        assertEquals(mic.getName(), copiedMic.getName());
        assertEquals(mic.getQuantity(), copiedMic.getQuantity());
        assertEquals(mic.getPrice(), copiedMic.getPrice());
        assertEquals(mic.getImageURL(), copiedMic.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Mic sameMic = new Mic(mic.getAllColors(), mic.getName());
        Mic differentMic = new Mic(mic.getAllColors(), "Different Mic");

        // Assert
        assertTrue(mic.equals(sameMic));
        assertFalse(mic.equals(differentMic));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(mic.equals(null));
    }

    @Test
public void testActualHashCode() {
    // Setup
    List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
    List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
    Mic mic1 = new Mic(1, colors1, "Mic 1", 5, 49.99, "https://example.com/mic1.jpg");
    Mic mic2 = new Mic(2, colors2, "Mic 2", 10, 99.99, "https://example.com/mic2.jpg");
    Mic mic3 = new Mic(3, colors1, "Mic 3", 3, 29.99, "https://example.com/mic3.jpg");

    // Act
    int hash1 = mic1.hashCode();
    int hash2 = mic2.hashCode();
    int hash3 = mic3.hashCode();

    // Assert
    assertNotEquals(hash1, hash2);
    assertNotEquals(hash2, hash3);
    assertNotEquals(hash1, hash3);
}

}
