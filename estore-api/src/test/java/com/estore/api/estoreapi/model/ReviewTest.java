package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class ReviewTest {
    private final Review review = new Review(1, 1, 1, "review");

    @Test
    public void testGetProductid() {
        // Setup
        int expected = 1;

        // Invoke
        int result = review.getProductid();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetUserid() {
        // Setup
        int expected = 1;

        // Invoke
        int result = review.getUserid();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetReview() {
        // Setup
        String expected = "review";

        // Invoke
        String result = review.getReview();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetRating() {
        // Setup
        int expected = 1;

        // Invoke
        int result = review.getRating();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetProductid() {
        // Setup
        int expected = 2;

        // Invoke
        review.setProductid(2);
        int result = review.getProductid();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetUserid() {
        // Setup
        int expected = 2;

        // Invoke
        review.setUserid(2);
        int result = review.getUserid();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetReview() {
        // Setup
        String expected = "review2";

        // Invoke
        review.setReview("review2");
        String result = review.getReview();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetRating() {
        // Setup
        int expected = 2;

        // Invoke
        review.setRating(2);
        int result = review.getRating();

        // Analysis
        assertEquals(expected, result);
    }

}
