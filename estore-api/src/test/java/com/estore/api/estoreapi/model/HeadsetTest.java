package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HeadsetTest {
    private Headset headset;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        headset = new Headset(1, colors, "Test Headset", 5, 99.99, "https://example.com/headset.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = headset.getProductType();

        // Assert
        assertEquals(ProductType.HEADSET, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Headset copiedHeadset;

        // Act
        copiedHeadset = (Headset) headset.copy(2);

        // Assert
        assertNotNull(copiedHeadset);
        assertEquals(2, copiedHeadset.getId());
        assertEquals(headset.getName(), copiedHeadset.getName());
        assertEquals(headset.getQuantity(), copiedHeadset.getQuantity());
        assertEquals(headset.getPrice(), copiedHeadset.getPrice());
        assertEquals(headset.getImageURL(), copiedHeadset.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Headset sameHeadset = new Headset(headset.getAllColors(), headset.getName());
        Headset differentHeadset = new Headset(headset.getAllColors(), "Different Headset");

        // Assert
        assertTrue(headset.equals(sameHeadset));
        assertFalse(headset.equals(differentHeadset));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(headset.equals(null));
    }

    @Test
    public void testHashCode() {
        List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
        List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
        Headset headset1 = new Headset(1, colors1, "Headset 1", 5, 49.99, "https://example.com/Headset1.jpg");
        Headset headset2 = new Headset(2, colors2, "Headset 2", 10, 99.99, "https://example.com/Headset2.jpg");
        Headset headset3 = new Headset(3, colors1, "Headset 3", 3, 29.99, "https://example.com/Headset3.jpg");

        // Act
        int hash1 = headset1.hashCode();
        int hash2 = headset2.hashCode();
        int hash3 = headset3.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }
}
