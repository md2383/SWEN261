package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class ColorTest {

    Color red = new Color("Red");
    Color blue = new Color("Blue");
    Color green = new Color("Green");

    @Test
    public void testColor() {
        String expectedRed = "Red";
        String expectedBlue = "Blue";
        String expectedGreen = "Green";

        assertEquals(expectedRed, red.getName());
        assertEquals(expectedBlue, blue.getName());
        assertEquals(expectedGreen, green.getName());
    }

    @Test
    public void testEqualsWithNull() {
        assertFalse(red.equals(null));
    }
}
