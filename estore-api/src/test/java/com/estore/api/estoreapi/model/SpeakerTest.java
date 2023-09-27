package com.estore.api.estoreapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpeakerTest {
    private Speaker speaker;

    @BeforeEach
    public void setUp() {
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        colors.add(new Color("White"));
        speaker = new Speaker(1, colors, "Test Speaker", 5, 99.99, "https://example.com/speaker.jpg");
    }

    @Test
    public void testGetProductType() {
        // Act
        ProductType productType = speaker.getProductType();

        // Assert
        assertEquals(ProductType.SPEAKER, productType);
    }

    @Test
    public void testCopy() {
        // Arrange
        Speaker copiedSpeaker;

        // Act
        copiedSpeaker = (Speaker) speaker.copy(2);

        // Assert
        assertNotNull(copiedSpeaker);
        assertEquals(2, copiedSpeaker.getId());
        assertEquals(speaker.getName(), copiedSpeaker.getName());
        assertEquals(speaker.getQuantity(), copiedSpeaker.getQuantity());
        assertEquals(speaker.getPrice(), copiedSpeaker.getPrice());
        assertEquals(speaker.getImageURL(), copiedSpeaker.getImageURL());
    }

    @Test
    public void testEquals() {
        // Arrange
        Speaker sameSpeaker = new Speaker(speaker.getAllColors(), speaker.getName());
        Speaker differentSpeaker = new Speaker(speaker.getAllColors(), "Different Speaker");

        // Assert
        assertTrue(speaker.equals(sameSpeaker));
        assertFalse(speaker.equals(differentSpeaker));
    }

    @Test
    public void testEqualsWithNull() {
        // Assert
        assertFalse(speaker.equals(null));
    }

    @Test
    public void testHashCode() {
        List<Color> colors1 = Arrays.asList(new Color("Black"), new Color("Silver"));
        List<Color> colors2 = Arrays.asList(new Color("White"), new Color("Gold"));
        Speaker speaker1 = new Speaker(1, colors1, "Speaker 1", 5, 49.99, "https://example.com/speaker1.jpg");
        Speaker speaker2 = new Speaker(2, colors2, "Speaker 2", 10, 99.99, "https://example.com/speaker2.jpg");
        Speaker speaker3 = new Speaker(3, colors1, "Speaker 3", 3, 29.99, "https://example.com/speaker3.jpg");

        // Act
        int hash1 = speaker1.hashCode();
        int hash2 = speaker2.hashCode();
        int hash3 = speaker3.hashCode();

        // Assert
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash2, hash3);
        assertNotEquals(hash1, hash3);
    }
}
