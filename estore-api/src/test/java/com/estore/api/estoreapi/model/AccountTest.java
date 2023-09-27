package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class AccountTest {
    private final Account account = new Account("username","email", "password", "firstName", "lastName", 0, "");

    @Test
    public void testGetShoppingCart(){
        // Setup
        ShoppingCart expected = new ShoppingCart();

        // Invoke
        ShoppingCart result = account.getShoppingCart();

        // Analysis
        assertEquals(expected.getProductQuan().length, result.getProductQuan().length);
    }

    @Test
    public void testSetShoppingCart(){
        // Setup
        ShoppingCart expected = new ShoppingCart();

        // Invoke
        account.setShoppingCart(expected);
        ShoppingCart result = account.getShoppingCart();

        // Analysis
        assertEquals(expected, result);
    }

    @Test  
    public void testUpdatePaymentInfo(){
        // Setup
        Payment expected = new Payment("a", "1", "2", 3);

        // Invoke
        account.updatePaymentInfo("a", "1", 3, "2");
        Payment result = account.getPayment();

        // Analysis
        assertEquals(expected.getCardHolder(), result.getCardHolder());
    }

    @Test
    public void testUpdateAddressInfo(){
        // Setup
        Address expected = new Address("a", "b", "c", "d", 1);

        // Invoke
        account.updateAddressInfo("a", "b", "c", "d", 1);
        Address result = account.getAddress();

        // Analysis
        assertEquals(expected.getCity(), result.getCity());
    }

    @Test
    public void testGetID() {
        // Setup
        int expected = 0;

        // Invoke
        int result = account.getId();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetID() {
        // Setup
        int expected = 1;

        // Invoke
        account.setId(expected);
        int result = account.getId();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetSessionID() {
        // Setup
        int expected = 0;

        // Invoke
        int result = account.getSessionID();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetUsername() {
        // Setup
        String expected = "username";

        // Invoke
        String result = account.getUsername();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetUsername() {
        // Setup
        String expected = "newUsername";

        // Invoke
        account.setUsername(expected);
        String result = account.getUsername();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetEmail() {
        // Setup
        String expected = "email";

        // Invoke
        String result = account.getEmail();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetFirstName() {
        // Setup
        String expected = "firstName";

        // Invoke
        String result = account.getFirstName();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetLastName() {
        // Setup
        String expected = "lastName";

        // Invoke
        String result = account.getLastName();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetAddress() {
        // Setup
        Address expected = new Address();

        // Invoke
        Address result = account.getAddress();

        // Analysis
        assertEquals(expected.getCity(), result.getCity());
    }

    @Test
    public void testSetAddress(){
        // Setup
        Address expected = new Address("a", "b", "c", "d", 1);

        // Invoke
        account.setAddress(expected);
        Address result = account.getAddress();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetPayment() {
        // Setup
        Payment expected = new Payment();

        // Invoke
        Payment result = account.getPayment();

        // Analysis
        assertEquals(expected.getCardHolder(), result.getCardHolder());
    }
    
    @Test
    public void testSetPayment(){
        // Setup
        Payment expected = new Payment("a", "b", "c", 1);

        // Invoke
        account.setPayment(expected);
        Payment result = account.getPayment();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetPassword() {
        // Setup
        String expected = "password";

        // Invoke
        String result = account.getPassword();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetPassword() {
        // Setup
        String expected = "newPassword";

        // Invoke
        account.setPassword(expected);
        String result = account.getPassword();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetIsLoggedIn() {
        // Setup
        boolean expected = false;

        // Invoke
        boolean result = account.getIsLoggedIn();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetIsLoggedIn() {
        // Setup
        boolean expected = true;

        // Invoke
        account.setIsLoggedIn(expected);
        boolean result = account.getIsLoggedIn();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testGetProfilePicture() {
        // Setup
        String expected = "";

        // Invoke
        String result = account.getProfilePicture();

        // Analysis
        assertEquals(expected, result);
    }

    @Test
    public void testSetProfilePicture() {
        // Setup
        String expected = "newProfilePicture";

        // Invoke
        account.setProfilePicture(expected);
        String result = account.getProfilePicture();

        // Analysis
        assertEquals(expected, result);
    }
}
