package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WebcamTest {
    private Webcam webcam;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        webcam = new Webcam(1, colors, "Test Webcam", 10, 49.99, "https://example.com/webcam.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = webcam.getProductType();

        // Assert
        assertEquals(ProductType.WEBCAM, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Webcam copiedWebcam;

        // Act
        copiedWebcam = (Webcam) webcam.copy(2);

        // Assert
        assertNotNull(copiedWebcam);
        assertEquals(2, copiedWebcam.getId());
        assertEquals(webcam.getName(), copiedWebcam.getName());
        assertEquals(webcam.getQuantity(), copiedWebcam.getQuantity());
        assertEquals(webcam.getPrice(), copiedWebcam.getPrice());
        assertEquals(webcam.getImageURL(), copiedWebcam.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Webcam sameWebcam = new Webcam(webcam.getAllColors(), webcam.getName());
        Webcam differentWebcam = new Webcam(webcam.getAllColors(), "Different Webcam");

        // Assert
        assertTrue(webcam.equals(sameWebcam));
        assertFalse(webcam.equals(differentWebcam));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(webcam.equals(null));
    }

    @Test
    public void testHashCode() {
        List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
        List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
        Webcam webcam1 = new Webcam(1, colors1, "Webcam 1", 5, 49.99, "https://example.com/Webcam1.jpg");
        Webcam webcam2 = new Webcam(2, colors2, "Webcam 2", 10, 99.99, "https://example.com/Webcam2.jpg");
        Webcam webcam3 = new Webcam(3, colors1, "Webcam 3", 3, 29.99, "https://example.com/Webcam3.jpg");

        // Act
        int hash1 = webcam1.hashCode();
        int hash2 = webcam2.hashCode();
        int hash3 = webcam3.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }
}
