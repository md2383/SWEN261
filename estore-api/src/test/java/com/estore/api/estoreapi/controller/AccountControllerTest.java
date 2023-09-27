package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Keyboard;
import com.estore.api.estoreapi.model.Payment;
import com.estore.api.estoreapi.model.Address;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.AccountDAO;

@Tag("Controller-tier")
public class AccountControllerTest{
    private AccountController accountController;
    private AccountDAO mockAccountDAO;

    @BeforeEach
    public void setUp() {
        mockAccountDAO = mock(AccountDAO.class);
        accountController = new AccountController(mockAccountDAO);
    }

    @Test
    public void testGetAllAccounts() throws IOException {
        //Setup
        Account[] accounts = new Account[2];
        accounts[0] = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 69, "");
        accounts[1] = new Account("av456", "cc@gmail.com", "password", "Anna", "Vi", 70, "");
        //when getAllAccounts is called return the accounts above
        when(mockAccountDAO.getAllAccounts()).thenReturn(accounts);

        //Invoke
        ResponseEntity<Account[]> response = accountController.getAllAccounts();
        
        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());
    }

    @Test
    public void testGetAllAccountsHandleException() throws IOException{
        //Setup
        //When getAllAccounts is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getAllAccounts();

        //Invoke
        ResponseEntity<Account[]> response = accountController.getAllAccounts();
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAccountById() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 69, "");
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getAccountById(69)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.getAccountById(69);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAccountByIdNotFound() throws IOException{
        //Setup
        //when getAccountById is called with the id 69 return null
        when(mockAccountDAO.getAccountById(69)).thenReturn(null);

        //Invoke
        ResponseEntity<Account> response = accountController.getAccountById(69);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAccountByIdHandleException() throws IOException{
        //Setup
        //When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getAccountById(69);

        //Invoke
        ResponseEntity<Account> response = accountController.getAccountById(69);
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testcreateAccount(){
        //Setup
        Account account = new Account("ry456", "ry789@gmail.com", "password123", "Ryan", "Yan", 23, "");
        
        //Invoke
        ResponseEntity<Account> response = accountController.createAccount(account);

        //Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testcreateAccountFailed() throws IOException{
        //Setup
        Account account = new Account("admin", "admin@gmail.com", "password", "John", "Wayne", 1, "");
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.createAccount(account)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.createAccount(account);

        //Analyze
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testCreateAccountHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).createAccount(account); 

        //Invoke
        ResponseEntity<Account> response = accountController.createAccount(account);
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateAccount() throws IOException{
        //Setup
        Account account = new Account("admin", "admin@gmail.com", "1234", "ad", "admin", 1, "");
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updateAccount(account)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.updateAccount(account);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateAccountFailed() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        //Invoke
        ResponseEntity<Account> response = accountController.updateAccount(null);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateAccountHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        //When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).updateAccount(account);

        //Invoke
        ResponseEntity<Account> response = accountController.updateAccount(account);
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDeleteAccount() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.deleteAccount(1)).thenReturn(null);

        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(1);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountNotFound() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return null
        when(mockAccountDAO.deleteAccount(1)).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(1);

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAccountHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).deleteAccount(1);
        
        //Invoke
        ResponseEntity<Account> response = accountController.deleteAccount(1);
        
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogin() throws IOException{
        //Setup
        Account account = new Account("admin", "bb@gmail.com", "1234", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.loginAccount(account.getUsername(), account.getPassword())).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.loginAccount(account.getUsername(), account.getPassword());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLoginFailed() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //Invoke
        ResponseEntity<Account> response = accountController.loginAccount(account.getUsername(), account.getPassword());

        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testLoginHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).loginAccount(account.getUsername(), account.getPassword());

        //Invoke
        ResponseEntity<Account> response = accountController.loginAccount(account.getUsername(), account.getPassword());

        //Analyze
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testLogout() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.logoutAccount(account.getUsername())).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.logoutAccount(account.getUsername());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testLogoutFailed() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //Invoke
        ResponseEntity<Account> response = accountController.logoutAccount(account.getUsername());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testLogoutHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).logoutAccount(account.getUsername());

        //Invoke
        ResponseEntity<Account> response = accountController.logoutAccount(account.getUsername());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testgetCurrentAccount() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getCurrentAccount(account.getId())).thenReturn(account);

        //Invoke
        ResponseEntity<Account> response = accountController.getCurrentAccount(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testgetCurrentAccountFailed() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        //Invoke
        ResponseEntity<Account> response = accountController.getCurrentAccount(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testgetCurrentAccountHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getCurrentAccount(account.getId());

        //Invoke
        ResponseEntity<Account> response = accountController.getCurrentAccount(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetShoppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getShoppingCart(account.getId())).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.getShoppingCart(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getShoppingCart(account.getId());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.getShoppingCart(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddProductToShoppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        List<Color> colors = new ArrayList<Color>();
        Keyboard kes = new Keyboard(6,colors,"blue log", 0, 0.0, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.addProductToShoppingCart(account.getId(), kes)).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.addProductToShoppingCart(account.getId(), kes);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddProductToShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        List<Color> colors = new ArrayList<Color>();
        Keyboard kes = new Keyboard(6,colors,"blue log", 0, 0.0, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).addProductToShoppingCart(account.getId(), kes);

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.addProductToShoppingCart(account.getId(), kes);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testAddColorToShoppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.addColorToShoppingCart(account.getId(), new Color("blue"))).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.addColorToShoppingCart(account.getId(), new Color("blue"));

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddColorToShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).addColorToShoppingCart(account.getId(), new Color("blue"));

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.addColorToShoppingCart(account.getId(), new Color("blue"));

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testRemoveProductFromShoppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.removeProductFromShoppingCart(account.getId(), 1)).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.removeProductFromShoppingCart(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveProductFromShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).removeProductFromShoppingCart(account.getId(), 1);

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.removeProductFromShoppingCart(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testIncrementQuantity() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.incrementQuantity(account.getId(), 1)).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.incrementQuantity(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testIncrementQuanityHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).incrementQuantity(account.getId(), 1);
        
        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.incrementQuantity(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testDecrementQuantity() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.decrementQuantity(account.getId(), 1)).thenReturn(account.getShoppingCart());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.decrementQuantity(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDecrementQuantityHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).decrementQuantity(account.getId(), 1);

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.decrementQuantity(account.getId(), 1);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testClearShopppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.clearShoppingCart(account.getId())).thenReturn(account.getShoppingCart());
        
        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.clearShoppingCart(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testClearShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).clearShoppingCart(account.getId());

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.clearShoppingCart(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateShoppingCart() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        ShoppingCart shoppingCart = new ShoppingCart();
    
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updateShoppingCart(account.getId(), shoppingCart)).thenReturn(account.getShoppingCart());
    
        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.updateShoppingCart(account.getId(), shoppingCart);
    
        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateShoppingCartHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
        ShoppingCart shoppingCart = new ShoppingCart();

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).updateShoppingCart(account.getId(), shoppingCart);

        //Invoke
        ResponseEntity<ShoppingCart> response = accountController.updateShoppingCart(account.getId(), shoppingCart);

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetPayment() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getPayment(account.getId())).thenReturn(account.getPayment());

        //Invoke
        ResponseEntity<Payment> response = accountController.getPayment(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetPaymentWhenNull() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getPayment(account.getId())).thenReturn(null);

        //Invoke
        ResponseEntity<Payment> response = accountController.getPayment(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetPaymentHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getPayment(account.getId());

        //Invoke
        ResponseEntity<Payment> response = accountController.getPayment(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdatePayment() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updatePayment(account.getId(), account.getPayment())).thenReturn(account.getPayment());
    
        //Invoke
        ResponseEntity<Payment> response = accountController.updatePayment(account.getId(), account.getPayment());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    @Test
    public void testUpdatePaymentWhenNull() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updatePayment(account.getId(), account.getPayment())).thenReturn(null);

        //Invoke
        ResponseEntity<Payment> response = accountController.updatePayment(account.getId(), account.getPayment());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
    @Test
    public void testUpdatePaymentHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).updatePayment(account.getId(), account.getPayment());

        //Invoke
        ResponseEntity<Payment> response = accountController.updatePayment(account.getId(), account.getPayment());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAddress() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getAddress(account.getId())).thenReturn(account.getAddress());
    
        //Invoke
        ResponseEntity<Address> response = accountController.getAddress(account.getId());
    
        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetAddressWhenNull() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.getAddress(account.getId())).thenReturn(null);

        //Invoke
        ResponseEntity<Address> response = accountController.getAddress(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetAddressHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).getAddress(account.getId());

        //Invoke
        ResponseEntity<Address> response = accountController.getAddress(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateAddress() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updateAddress(account.getId(), account.getAddress())).thenReturn(account.getAddress());

        //Invoke
        ResponseEntity<Address> response = accountController.updateAddress(account.getId(), account.getAddress());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateAddressWhenNull() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.updateAddress(account.getId(), account.getAddress())).thenReturn(null);

        //Invoke
        ResponseEntity<Address> response = accountController.updateAddress(account.getId(), account.getAddress());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateAddressHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).updateAddress(account.getId(), account.getAddress());

        //Invoke
        ResponseEntity<Address> response = accountController.updateAddress(account.getId(), account.getAddress());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistory() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.productsAllowedToReview(account.getId())).thenReturn(account.getShoppingCart().getProductHistory().toArray(new Product[0]));
    
        //Invoke
        ResponseEntity<Product[]> response = accountController.getOrderHistory(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistoryWhenNull() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.productsAllowedToReview(account.getId())).thenReturn(null);

        //Invoke
        ResponseEntity<Product[]> response = accountController.getOrderHistory(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistoryHandleException() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).productsAllowedToReview(account.getId());

        //Invoke
        ResponseEntity<Product[]> response = accountController.getOrderHistory(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistoryQuantity() throws IOException{
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");

        //when getAccountById is called with the id 69 return the account above
        when(mockAccountDAO.productsAllowedToReviewQuantities(account.getId())).thenReturn(account.getShoppingCart().getProductHistoryQuan());

        //Invoke
        ResponseEntity<int[]> response = accountController.getOrderHistoryQuantity(account.getId());

        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOrderHistoryQuantityHandleException() throws IOException {
        //Setup
        Account account = new Account("jw123", "bb@gmail.com", "password", "John", "Wayne", 100, "");
    
        // When getAccountById is called on the Mock AccountDAO throw an Exception
        doThrow(new IOException()).when(mockAccountDAO).productsAllowedToReviewQuantities(account.getId());

        //Invoke
        ResponseEntity<int[]> response = accountController.getOrderHistoryQuantity(account.getId());

        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}