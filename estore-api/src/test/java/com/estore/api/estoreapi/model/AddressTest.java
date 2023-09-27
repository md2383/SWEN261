package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class AddressTest {
    private final Address address = new Address("New York", "123 Real St", "NY", "56B", 10012);

    @Test
    public void testGetStreet() {
        // Setup
        String expected = "123 Real St";

        // Invoke
        String result = address.getStreet();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetHouseNumber() {
        // Setup
        String expected = "56B";

        // Invoke
        String result = address.getHouseNumber();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetZip() {
        // Setup
        int expected = 10012;

        // Invoke
        int result = address.getZip();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetCity(){
        // Setup
        String expected = "New York";

        // Invoke
        String result = address.getCity();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetState(){
        // Setup
        String expected = "NY";

        // Invoke
        String result = address.getState();

        // Analysis
        assertEquals(expected, result);
    }
}
