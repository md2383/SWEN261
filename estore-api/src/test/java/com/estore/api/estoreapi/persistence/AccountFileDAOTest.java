package com.estore.api.estoreapi.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.AccountSession;
import com.estore.api.estoreapi.model.ActiveAccountSessions;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.Mouse;
import com.estore.api.estoreapi.model.Payment;
import com.estore.api.estoreapi.model.Address;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

@Tag("Persistence-tier")
public class AccountFileDAOTest {

    private AccountFileDAO accountFileDAO;
    private ObjectMapper mockObjectMapper;
    private Account[] testAccounts;

    @BeforeEach
    public void setupAccountFileDAO() throws IOException {
        // Create a mock ObjectMapper instance
        mockObjectMapper = mock(ObjectMapper.class);

        // Set up the test data
        testAccounts = new Account[3];
        testAccounts[0] = new Account("user1", "john.doe@example.com", "password", "John", "doe", 1, "token1");
        testAccounts[1] = new Account("user2", "jane.doe@example.com", "password", "jane", "doe", 2, "token2");
        testAccounts[2] = new Account("user3", "jack.doe@example.com", "password", "jack", "doe", 3, "token3");

        // Configure the mockObjectMapper to return the test data when readValue is
        // called
        when(mockObjectMapper.readValue(any(File.class), eq(Account[].class)))
                .thenReturn(testAccounts);

        // Create an instance of AccountFileDAO with the mockObjectMapper
        accountFileDAO = new AccountFileDAO("file.txt", mockObjectMapper);
    }

    // test class to get a specific account by id
    @Test
    public void testGetAccount() throws IOException {
        // Call the getAccount method
        Account account = accountFileDAO.getAccountById(1);

        // Verify that the correct account was returned
        assertEquals(testAccounts[0], account);
    }

    // test class to get all accounts
    @Test
    public void testGetAllAccounts() throws IOException {
        // Call the getAllAccounts method
        Account[] accounts = accountFileDAO.getAllAccounts();

        // Verify that the correct accounts were returned
        assertArrayEquals(testAccounts, accounts);
    }

    // test class to create a new account
    @Test
    public void testCreateAccount() throws IOException {
        // Create a test account
        Account testAccount = new Account("testuser", "testuser@example.com", "password123", "Test", "User", 1,
                "profile.jpg");
        Account createdAccount = accountFileDAO.createAccount(testAccount);

        // Verify that the created account is not null
        assertNotNull(createdAccount, "Created account should not be null");

        // Verify that the created account has the same username and email as the test
        // account
        assertEquals(testAccount.getUsername(), createdAccount.getUsername(),
                "Created account should have the same username");
        assertEquals(testAccount.getEmail(), createdAccount.getEmail(), "Created account should have the same email");

        // Verify that the created account has been added to the accounts map in
        // AccountFileDAO
        assertTrue(accountFileDAO.getAllAccounts().length == 4,
                "Created account should be added to accounts map in AccountFileDAO");

        // Attempt to create an account with an existing username
        Account user2 = new Account("testuser2", "testuser2@example.com", "password456", "Test2",
                "User2", 2, "profile2.jpg");
        Account duplicateUsernameAccount = new Account("testuser2", "testuser2@example.com", "password456", "Test2",
                "User2", 2, "profile2.jpg");
        accountFileDAO.createAccount(user2);
        Account result1 = accountFileDAO.createAccount(duplicateUsernameAccount);

        // Verify that createAccount() returns null for a duplicate username
        assertNull(result1, "createAccount() should return null for a duplicate username");

        // Attempt to create an account with an existing email
        Account duplicateEmailAccount = new Account("testuser3", "testuser@example.com", "password789", "Test3",
                "User3", 3, "profile3.jpg");
        Account result2 = accountFileDAO.createAccount(duplicateEmailAccount);

        // Verify that createAccount() returns null for a duplicate email
        assertNull(result2, "createAccount() should return null for a duplicate email");
    }

    // test class to login to an account
    @Test
    public void testLoginAccount() throws IOException {

        // Create a test account
        Account testAccount = testAccounts[0];
        // Test case 1: Correct username and password
        Account loggedAccount1 = accountFileDAO.loginAccount(testAccount.getUsername(), testAccount.getPassword());
        assertNotNull(loggedAccount1); // Assert that a non-null account is returned
        assertTrue(loggedAccount1.getIsLoggedIn()); // Assert that the account is logged in

        Account testAccount2 = testAccounts[1];
        // Test case 2: Incorrect password
        Account loggedAccount2 = accountFileDAO.loginAccount(testAccount2.getUsername(), "wrongpassword");
        assertNull(loggedAccount2); // Assert that null is returned

        // Test case 3: Non-existing username
        Account loggedAccount3 = accountFileDAO.loginAccount("nonexistinguser", "password");
        assertNull(loggedAccount3); // Assert that null is returned
    }

    // test class to logout of an account
    @Test
    public void testLogoutAccount() throws IOException {

        // Create test accounts
        Account testAccount1 = testAccounts[0];

        // Log in the accounts
        accountFileDAO.loginAccount(testAccount1.getUsername(), testAccount1.getPassword());

        // Test case 1: Correct username
        Account loggedOutAccount1 = accountFileDAO.logoutAccount(testAccount1.getUsername());
        assertNotNull(loggedOutAccount1); // Assert that a non-null account is returned
        assertFalse(loggedOutAccount1.getIsLoggedIn()); // Assert that the account is logged out

        // Test case 2: Non-existing username
        Account loggedOutAccount2 = accountFileDAO.logoutAccount("nonexistinguser");
        assertNull(loggedOutAccount2); // Assert that null is returned
    }

    // test class to delete an account
    @Test
    public void testDeleteAccount() throws IOException {
        // Create test accounts
        Account deleteAccount1 = new Account("deleteuser1", "user1todelete@example.com", "password1", "John", "Doe", 5,
                "profile1.jpg");
        Account deleteAccount2 = new Account("deleteuser2", "user2todelete@example.com", "password2", "Jane", "Smith",
                6, "profile2.jpg");

        // for loop to add new accounts into the testAccounts array
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = deleteAccount1;
            } else {
                tempAccounts[i] = deleteAccount2;
            }
        }
        testAccounts = tempAccounts;

        accountFileDAO.createAccount(deleteAccount1);
        accountFileDAO.createAccount(deleteAccount2);

        // Log in the accounts
        accountFileDAO.loginAccount(deleteAccount1.getUsername(), deleteAccount1.getPassword());
        accountFileDAO.loginAccount(deleteAccount2.getUsername(), deleteAccount2.getPassword());

        int deleteAccount1Id = -1;
        int deleteAccount2Id = -1;
        for (Account account : accountFileDAO.getAllAccounts()) {
            if (account.getUsername().equals(deleteAccount1.getUsername())) {
                deleteAccount1Id = account.getId();
            } else if (account.getUsername().equals(deleteAccount2.getUsername())) {
                deleteAccount2Id = account.getId();
            }
        }

        // Test case 1: Existing account and correct authentication
        Account deletedAccount1 = accountFileDAO.deleteAccount(deleteAccount1Id);
        assertNull(deletedAccount1); // Assert that null is returned

        // Test case 2: Non-existing account
        Account deletedAccount2 = accountFileDAO.deleteAccount(999);
        assertNull(deletedAccount2); // Assert that a non-null account is returned

        // Test case 3: Incorrect authentication
        Account deletedAccount3 = accountFileDAO.deleteAccount(deleteAccount2Id);
        assertNotNull(deletedAccount3); // Assert that a non-null account is returned
    }

    // test class to update an account
    @Test
    public void testUpdateAccount() throws IOException {
        // Create test accounts
        Account account1 = new Account("user1toupdate", "user1update@example.com", "password1", "John", "Doe", 7,
                "profile1.jpg");
        Account account2 = new Account("user2toupdate", "user2update@example.com", "password2", "Jane", "Smith", 8,
                "profile2.jpg");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Create the accounts in the DAO
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        int account1id = -1;
        int account2id = -1;
        for (Account account : accountFileDAO.getAllAccounts()) {
            if (account.getUsername().equals(account1.getUsername())) {
                account1id = account.getId();
            } else if (account.getUsername().equals(account2.getUsername())) {
                account2id = account.getId();
            }
        }

        // Log in the accounts
        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // test case 1: existing account and correct authentication
        assertTrue(account1.getIsLoggedIn());

        // Update account1 with new data
        account1.setPassword("newpassword");
        account1.setProfilePicture("newprofile.jpg");

        // Test case 1: Existing account and correct authentication
        accountFileDAO.updateAccount(account1);
        Account updatedAccount1 = accountFileDAO.getAccountById(account1id);
        assertEquals("newpassword", updatedAccount1.getPassword()); // Assert that a non-null account is returned
        accountFileDAO.logoutAccount(account1.getUsername());

        // Test case 2: Non-existing account
        Account updatedAccount2 = accountFileDAO.updateAccount(
                new Account("user3", "user3@example.com", "password3", "Alice", "Johnson", 3, "profile3.jpg"));
        assertNull(updatedAccount2); // Assert that null is returned

        // Test case 3: Incorrect authentication

        // Make sure account2 is logged out
        accountFileDAO.logoutAccount(account2.getUsername());

        // Attempt to update account2 with new data while logged out
        Account updatedAccount3 = accountFileDAO.updateAccount(account2);
        assertNull(updatedAccount3); // Assert that null is returned
    }

    // test class to get the current account by id
    @Test
    public void testGetCurrentAccount() throws IOException {
        // Create test accounts
        Account account1 = new Account("sessiontest1", "sessiontest1@example.com", "password1", "John", "Doe", 1,
                "profile1.jpg");
        Account account2 = new Account("sessiontest2", "sessiontest2@example.com", "password2", "Jane", "Smith", 2,
                "profile2.jpg");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Create the accounts in the DAO
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        // Log in the accounts
        Account a = accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        assertNotNull(accountFileDAO.getCurrentAccount(account1.getSessionID())); // Assert that a non-null account is
                                                                                  // returned
        assertEquals(account1, accountFileDAO.getCurrentAccount(account1.getSessionID())); // Assert that the returned
                                                                                           // account matches the
                                                                                           // logged-in account

        // log out the account
        accountFileDAO.logoutAccount(account1.getUsername());

        // Test case 2: Non-existing session ID
        Account currentAccount2 = accountFileDAO.getCurrentAccount(999);
        assertNull(currentAccount2); // Assert that null is returned

        // Log out the accounts
        accountFileDAO.logoutAccount(account2.getUsername());

        // Test case 3: Session ID of logged out user
        Account currentAccount3 = accountFileDAO.getCurrentAccount(account2.getSessionID());
        assertNull(currentAccount3); // Assert that null is returned
    }

    @Test
    public void testGetShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("carttest1", "carttest1@example.com", "password1", "John", "Doe", 1,
                "profile1.jpg");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 1];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            }
        }
        testAccounts = tempAccounts;

        // Create the accounts in the DAO
        accountFileDAO.createAccount(account1);

        // Log in the account
        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // Test case 1: User is logged in
        assertNotNull(accountFileDAO.getShoppingCart(account1.getId())); // Assert that a non-null cart is returned
        assertEquals(account1.getShoppingCart(), accountFileDAO.getShoppingCart(account1.getId())); // Assert that the
                                                                                                    // returned cart
                                                                                                    // matches the
                                                                                                    // logged-in
                                                                                                    // account's cart
    }

    @Test
    public void testGetShoppingCartEmpty() throws IOException {
        // Create test accounts
        Account account1 = new Account("carttest1", "carttest1@example.com", "password1", "John", "Doe", 1,
                "profile1.jpg");

        // Create a product
        List<Color> colors = new ArrayList<>();
        colors.add(new Color("Black"));
        Product product1 = new Keyboard(colors, "Razer Blackwidow");

        // Create an empty shopping cart

        ShoppingCart emptyCart = new ShoppingCart();

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 1];

        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            }
        }
        testAccounts = tempAccounts;

        // Create the accounts in the DAO
        accountFileDAO.createAccount(account1);

        // Add a product to the cart
        accountFileDAO.getShoppingCart(account1.getId()).addProductToShoppingCart(product1);

        // Test case 1: User is logged out
        accountFileDAO.logoutAccount(account1.getUsername());
        assertNull(accountFileDAO.getCurrentAccount(account1.getId())); // Assert that null is returned

        // Test case 3: Return empty cart if user is not logged in
        assertEquals(emptyCart.getProductsInCart().length,
                accountFileDAO.getShoppingCart(account1.getId()).getProductsInCart().length); // Assert that null is
                                                                                              // returned
    }

    @Test
    public void testGetShoppingCartUserNotLoggedIn() throws IOException {
        // Create test accounts
        Account account1 = new Account("carttest1", "carttest1@example.com", "password1", "John", "Doe", 1,
                "profile1.jpg");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 1];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            }
        }
        testAccounts = tempAccounts;

        // Create the accounts in the DAO
        accountFileDAO.createAccount(account1);

        // Add a product to the cart
        accountFileDAO.getShoppingCart(account1.getId()).addProductToShoppingCart(
                new Keyboard(Collections.singletonList(new Color("Black")), "Razer Blackwidow"));

        // Log out the user
        accountFileDAO.logoutAccount(account1.getUsername());

        // Test case 1: User is not logged in
        assertNull(accountFileDAO.getCurrentAccount(account1.getId())); // Assert that null is returned
        assertEquals(0, accountFileDAO.getShoppingCart(account1.getId()).getProductsInCart().length); // Assert that an
                                                                                                      // empty shopping
                                                                                                      // cart is
                                                                                                      // returned
    }

    // test class to add product to shopping cart
    @Test
    public void testAddProductToShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("user123", "user123@example.com", "password1", "John", "Doe", 1, "profile1.jpg");
        Account account2 = new Account("user456", "user456@example.com", "password2", "Jane", "Smith", 2,
                "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(2, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // Test case 1: Add product to shopping cart of an existing user
        ShoppingCart cart1 = accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertTrue(cart1.getProductsInCart()[0].equals(product1)); // Assert that the product is added to the shopping
                                                                   // cart

        accountFileDAO.logoutAccount(account2.getUsername());

        // Test case 2: Add product to shopping cart of a different user
        ShoppingCart cart2 = accountFileDAO.addProductToShoppingCart(account2.getId(), product2);
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductsInCart().length); // Assert that the product is not added to the shopping cart

        // Test case 3: Add product to shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.addProductToShoppingCart(account1.getId(), null); // Passing null as product
                                                                                              // to trigger an
        // exception
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductsInCart().length); // Assert that the product is not added to the shopping cart
    }

    @Test
    public void testAddColorToShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("user123456", "user123456@example.com", "password1", "John", "Doe", 1,
                "profile1.jpg");
        Account account2 = new Account("user789012", "user789012@example.com", "password2", "Jane", "Smith", 2,
                "profile2.jpg");

        // Create test colors
        Color color1 = new Color("Blue");
        Color color2 = new Color("Red");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // Test case 1: Add color to shopping cart of an existing user
        ShoppingCart cart1 = accountFileDAO.addColorToShoppingCart(account1.getId(), color1);
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertTrue(cart1.getListofColors()[0].equals(color1)); // Assert that the color is added to the shopping cart

        // Test case 2: Add color to shopping cart of a different user
        ShoppingCart cart2 = accountFileDAO.addColorToShoppingCart(99, color2);
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getListofColors().length); // Assert that the color is not added to the shopping cart

        // Test case 3: Add color to shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.addColorToShoppingCart(account2.getId(), null); // Passing null as color to
                                                                                            // trigger an
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart3.getListofColors().length); // Assert that the color is not added to the shopping cart
    }

    // test class to remove product from shopping cart
    @Test
    public void testRemoveProductFromShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("johndoe", "johndoe@example.com", "password1", "John", "Doe", 1, "profile1.jpg");
        Account account2 = new Account("janesmith", "janesmith@example.com", "password2", "Jane", "Smith", 2,
                "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(1, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // Add products to shopping carts
        accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        accountFileDAO.addProductToShoppingCart(account2.getId(), product2);

        accountFileDAO.addColorToShoppingCart(account1.getId(), colors.get(0));
        accountFileDAO.addColorToShoppingCart(account2.getId(), colors.get(1));

        // Test case 1: Remove product from shopping cart of an existing user
        ShoppingCart cart1 = accountFileDAO.removeProductFromShoppingCart(account1.getId(), 0); // Remove product at
                                                                                                // index 0
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart1.getProductsInCart().length); // Assert that the product is removed from the shopping cart
        assertEquals(0, cart1.getListofColors().length); // Assert that the color is removed from the shopping cart

        // Test case 2: Remove product from shopping cart of a different user
        ShoppingCart cart2 = accountFileDAO.removeProductFromShoppingCart(99, 0); // Remove product at index 0
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductsInCart().length); // Assert that the product is not removed from the shopping
                                                           // cart
        assertEquals(0, cart2.getListofColors().length);

        // Test case 3: Remove product from shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.removeProductFromShoppingCart(account2.getId(), -1); // Remove product at
                                                                                                 // index -1
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(1, cart3.getProductsInCart().length); // Assert that the product is not removed from the shopping
                                                           // cart
        assertEquals(1, cart3.getListofColors().length);
    }

    // test class to increment product quantity in shopping cart
    @Test
    public void testIncrementQuantity() throws IOException {
        // Create test accounts
        Account account1 = new Account("michaeljordan", "mjordan@example.com", "password1", "Michael", "Jordan", 1,
                "profile1.jpg");
        Account account2 = new Account("lebronjames", "ljames@example.com", "password2", "LeBron", "James", 2,
                "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(1, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());

        // Add products to shopping carts
        accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        accountFileDAO.addProductToShoppingCart(account2.getId(), product2);

        accountFileDAO.addColorToShoppingCart(account1.getId(), colors.get(0));
        accountFileDAO.addColorToShoppingCart(account2.getId(), colors.get(1));

        // Test case 1: Increment quantity of product in shopping cart of an existing
        // user
        ShoppingCart cart1 = accountFileDAO.incrementQuantity(account1.getId(), 0); // Increment quantity of product at
                                                                                    // index 0
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertEquals(2, cart1.getProductQuan()[0]); // Assert that the quantity of the product is incremented

        // Test case 2: Increment quantity of product in shopping cart of a different
        // user
        ShoppingCart cart2 = accountFileDAO.incrementQuantity(99, 0); // Increment quantity of product at index 0
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductQuan().length); // Assert that the quantity of the product is not incremented

        // Test case 3: Increment quantity of product in shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.incrementQuantity(account2.getId(), -1); // Increment quantity of product at
                                                                                     // index -1
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(1, cart3.getProductQuan()[0]); // Assert that the quantity of the product is not incremented
    }

    // test class to decrement product quantity in shopping cart
    @Test
    public void testDecrementQuantity() throws IOException {
        // Create test accounts
        Account account1 = new Account("katewilliams", "kwilliams@example.com", "password1", "Kate", "Williams", 1,
                "profile1.jpg");
        Account account2 = new Account("janedoe2", "jdoe2@example.com", "password2", "Jane", "Doe", 2, "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(1, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());
        accountFileDAO.loginAccount(account2.getUsername(), account2.getPassword());

        // Add products to shopping carts
        accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        accountFileDAO.addProductToShoppingCart(account2.getId(), product2);

        accountFileDAO.addColorToShoppingCart(account1.getId(), colors.get(0));
        accountFileDAO.addColorToShoppingCart(account2.getId(), colors.get(1));

        // Test case 1: Decrement quantity of product in shopping cart of an existing
        // user
        accountFileDAO.incrementQuantity(account1.getId(), 0); // Increment quantity of product at index 0; total
                                                               // quantity is 2
        ShoppingCart cart1 = accountFileDAO.decrementQuantity(account1.getId(), 0); // Decrement quantity of product at
                                                                                    // index 0; total quantity is 1
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertEquals(1, cart1.getProductQuan()[0]); // Assert that the quantity of the product is decremented
                                                    // to 0

        ShoppingCart cart2 = accountFileDAO.decrementQuantity(account1.getId(), 0); // Decrement quantity of product at
                                                                                    // index 0; total quantity is 0 so
                                                                                    // it should be removed from cart
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductsInCart().length); // Assert that the product is removed from the shopping cart
        assertEquals(0, cart2.getListofColors().length);
        assertEquals(0, cart2.getProductQuan().length);

        // Test case 3: Decrement quantity of product in shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.decrementQuantity(account2.getId(), -1); // Decrement quantity of product at
                                                                                     // index -1
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(1, cart3.getProductQuan()[0]); // Assert that the quantity of the product is not decremented
    }

    // test class to clear the shopping cart
    @Test
    public void testClearShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("mikesmith", "msmith@example.com", "password1", "Mike", "Smith", 1,
                "profile1.jpg");
        Account account2 = new Account("sarahjones", "sjones@example.com", "password2", "Sarah", "Jones", 2,
                "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(1, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        // Add products to shopping carts
        accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        accountFileDAO.addProductToShoppingCart(account2.getId(), product2);

        accountFileDAO.addColorToShoppingCart(account1.getId(), colors.get(0));
        accountFileDAO.addColorToShoppingCart(account2.getId(), colors.get(1));

        // Test case 1: Clear shopping cart of an existing user
        ShoppingCart cart1 = accountFileDAO.clearShoppingCart(account1.getId());
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart1.getProductsInCart().length); // Assert that the shopping cart is empty
        assertEquals(0, cart1.getListofColors().length);
        assertEquals(0, cart1.getProductQuan().length);
        assertNotEquals(0, cart1.getProductHistory().size());
        assertNotEquals(0, cart1.getProductHistoryQuan().length);

        // Test case 2: Clear shopping cart of a different user
        ShoppingCart cart2 = accountFileDAO.clearShoppingCart(account2.getId());
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart2.getProductsInCart().length); // Assert that the shopping cart is empty
        assertEquals(0, cart2.getListofColors().length);
        assertEquals(0, cart2.getProductQuan().length);
        assertNotEquals(0, cart2.getProductHistory().size());
        assertNotEquals(0, cart2.getProductHistoryQuan().length);

        // Test case 3: Clear shopping cart with exception
        ShoppingCart cart3 = accountFileDAO.clearShoppingCart(-1);
        assertNotNull(cart3); // Assert that a non-null shopping cart is returned
        assertEquals(0, cart3.getProductsInCart().length); // Assert that the shopping cart is empty
        assertEquals(0, cart3.getListofColors().length);
        assertEquals(0, cart3.getProductQuan().length);
        assertEquals(0, cart3.getProductHistory().size());
        assertEquals(0, cart3.getProductHistoryQuan().length);
    }

    // test class to update the shopping cart
    @Test
    public void testUpdateShoppingCart() throws IOException {
        // Create test accounts
        Account account1 = new Account("alexsmith", "alexsmith@example.com", "password1", "Mike", "Smith", 1,
                "profile1.jpg");
        Account account2 = new Account("emilyjohnson", "emilyjohnson@example.com", "password2", "Sarah", "Jones", 2,
                "profile2.jpg");

        // Create test products
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color("Blue"));
        colors.add(new Color("Red"));

        Product product1 = new Keyboard(0, colors, "Cool Keyboard", 0, 0, "coolkeyboard.png");
        Product product2 = new Mouse(1, colors, "Fancy Mouse", 0, 0, "coolmouse.png");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());
        accountFileDAO.loginAccount(account2.getUsername(), account2.getPassword());

        // Add products to shopping carts
        accountFileDAO.addProductToShoppingCart(account1.getId(), product1);
        accountFileDAO.addProductToShoppingCart(account2.getId(), product2);

        accountFileDAO.addColorToShoppingCart(account1.getId(), colors.get(0));
        accountFileDAO.addColorToShoppingCart(account2.getId(), colors.get(1));

        // Test case 1: Update shopping cart of an existing user
        ShoppingCart newCart = new ShoppingCart();
        newCart.addProductToShoppingCart(product1);
        newCart.addColorToShoppingCart(colors.get(0));
        newCart.addProductToShoppingCart(product2);
        newCart.addColorToShoppingCart(colors.get(1));

        ShoppingCart cart1 = accountFileDAO.updateShoppingCart(account1.getId(), newCart);
        assertNotNull(cart1); // Assert that a non-null shopping cart is returned
        assertEquals(newCart, cart1); // Assert that the returned shopping cart is equal to the new cart

        // Test case 2: Update shopping cart of a different user
        ShoppingCart cart2 = accountFileDAO.updateShoppingCart(account2.getId(), newCart);
        assertNotNull(cart2); // Assert that a non-null shopping cart is returned
        assertEquals(newCart, cart2); // Assert that the returned shopping cart is equal to the new cart

    }

    // Test case for invalid session ID
    @Test
    public void testGetPayment() throws IOException {
        // Create test accounts
        Account account1 = new Account("johnpeters", "johnpeters@example.com", "password1", "John", "Peters", 1,
                "profile1.jpg");
        Account account2 = new Account("katebrown", "katebrown@example.com", "password2", "Kate", "Brown", 2,
                "profile2.jpg");

        account1.updatePaymentInfo("johnpeters", "1234567890123456", 123, "01/2020");

        // Create a test payment
        Payment payment1 = new Payment();
        payment1.setCardNumber("1234567890123456");
        payment1.setExpDate("01/2020");
        payment1.setCvv(123);
        payment1.setCardHolder("John Peters");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account1;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account2;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account1);
        accountFileDAO.createAccount(account2);

        accountFileDAO.loginAccount(account1.getUsername(), account1.getPassword());
        accountFileDAO.updatePayment(account1.getId(), payment1);

        accountFileDAO.loginAccount(account2.getUsername(), account2.getPassword());

        // Test case 1: Get payment of an existing user
        Payment seeReturnedPayment = accountFileDAO.getPayment(account1.getId());

        // Assert that the returned payment is equal to the payment added
        assertNotNull(seeReturnedPayment);
        assertNotEquals(null, seeReturnedPayment);

        // Test case 2: Get payment of a different user where it is empty
        Payment seeReturnedPayment2 = accountFileDAO.getPayment(account2.getId());

        // Assert that the returned payment is equal to the payment added
        assertNotNull(seeReturnedPayment2);

        // Test case 3: Get payment of a user that doesn't exist
        Payment seeReturnedPayment3 = accountFileDAO.getPayment(-1);

        // Assert that the returned payment is equal to the payment added
        assertNull(seeReturnedPayment3);
    }
    
    // Test case for update payment
    @Test
    public void testUpdatePayment() throws IOException {
        // Create test accounts
        Account account3 = new Account("jasonlee", "jasonlee@example.com", "password3", "Jason", "Lee", 3,
                "profile3.jpg");
        Account account4 = new Account("nataliechen", "nataliechen@example.com", "password4", "Natalie", "Chen", 4,
                "profile4.jpg");

        // Create a test payment
        Payment payment1 = new Payment();
        payment1.setCardNumber("1234567890123456");
        payment1.setExpDate("01/2020");
        payment1.setCvv(123);
        payment1.setCardHolder("Jason Lee");

        // Create a test payment
        Payment payment2 = new Payment();
        payment2.setCardNumber("1234567890123456");
        payment2.setExpDate("01/2020");
        payment2.setCvv(123);
        payment2.setCardHolder("Natalie Chen");

        // Add test accounts to the accounts map
        Account[] tempAccounts = new Account[testAccounts.length + 2];
        for (int i = 0; i < tempAccounts.length; i++) {
            if (i < testAccounts.length) {
                tempAccounts[i] = testAccounts[i];
            } else if (i == testAccounts.length) {
                tempAccounts[i] = account3;
            } else if (i == testAccounts.length + 1) {
                tempAccounts[i] = account4;
            }
        }
        testAccounts = tempAccounts;

        // Log in the accounts
        accountFileDAO.createAccount(account3);
        accountFileDAO.createAccount(account4);

        accountFileDAO.loginAccount(account3.getUsername(), account3.getPassword());
        
        //test the update payment method
        Payment updatedPayment = accountFileDAO.updatePayment(account3.getId(), payment1);
        assertNotNull(updatedPayment);
        assertEquals(payment1.getCardHolder(), updatedPayment.getCardHolder());


        //test the update payment method
        Payment updatedPayment2 = accountFileDAO.updatePayment(account4.getId(), payment2);
        assertNotNull(updatedPayment2);
        assertEquals(payment2.getCardHolder(), updatedPayment2.getCardHolder());

        accountFileDAO.logoutAccount(account4.getUsername());

        //test update payment on not logged in account
        Payment originalPayment = accountFileDAO.getPayment(account4.getId());
        Payment updatedPayment3 = accountFileDAO.updatePayment(account4.getId(), payment2);
        assertEquals(updatedPayment3, originalPayment);
    
    }

    @Test
    public void testGetAddress() throws IOException {
        // Setup
        Account account = testAccounts[0];

        // Create address
        Address expected = new Address("Test", "Test", "Test", "Test", 1234);
        account.setAddress(expected);

        // Log in account and get address
        accountFileDAO.loginAccount(account.getUsername(), account.getPassword());
        Address address = accountFileDAO.getAddress(account.getSessionID());

        // Analyze results
        assertNotNull(address);
        assertEquals(expected, address);
    }

    @Test
    public void testGetAddressNotLoggedIn() throws IOException {
        // Setup
        Account account = testAccounts[0];

        // Create address
        Address expected = new Address("Test", "Test", "Test", "Test", 1234);
        account.setAddress(expected);

        // Get address
        Address address = accountFileDAO.getAddress(account.getSessionID());

        // Analyze results
        assertNull(address);
    }
}