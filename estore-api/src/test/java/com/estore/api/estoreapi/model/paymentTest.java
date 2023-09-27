package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.sql.Date;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class paymentTest {
    Payment dudePayment = new Payment("Jack", "1234567890", "4/45/86", 111);


    @Test
    public void testCardHolder() {
        String expectedCardHolder = "Jack";
        assertEquals(expectedCardHolder, dudePayment.getCardHolder());
    }

    @Test
    public void testCardNumber() {
        String expectedCardNumber = "1234567890";
        assertEquals(expectedCardNumber, dudePayment.getCardNumber());
    }

    @Test
    public void testCVV() {
        int expectedCVV = 111;
        assertEquals(expectedCVV, dudePayment.getCvv());
    }

    @Test
    public void testExpDate() {
        String expectedDate = "4/45/86";
        assertEquals(expectedDate, dudePayment.getExpDate());
    }
}