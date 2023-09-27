package com.estore.api.estoreapi.model;

import java.util.Set;

/**
 * Represents an Account class
 * 
 * @author Team H
 */
public class Account {
    private int id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Address address;
    private Payment payment;
    private boolean isLoggedIn;
    private int sessionID;
    private ShoppingCart shoppingCart;
    private String profilePicture;

    /**
     * Empty Account constructor
     */
    public Account() {
    }

    /**
     * Create an account with a username, email, password, first and last name, and
     * an ID
     * 
     * @param username  the username of the account
     * @param email     the email of the account
     * @param password  the password of the account
     * @param firstName the first name of the account
     * @param lastName  the last name of the account
     * @param id        the id of the account
     */
    public Account(String username, String email, String password, String firstName, String lastName, int id, String profilePicture) {
        this.id = id; // unique id
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = new Address();
        this.payment = new Payment();
        this.isLoggedIn = false;
        this.shoppingCart = new ShoppingCart();
        this.sessionID = 0;
        this.profilePicture = profilePicture;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * update payment info
     * 
     * @param String cardHolder, cardNumber, cvv, expDate ; all are components of
     *               credit card info
     * 
     */
    public void updatePaymentInfo(String cardHolder, String cardNumber, int cvv, String expDate) {
        this.payment.setCardHolder(cardHolder);
        this.payment.setCardNumber(cardNumber);
        this.payment.setCvv(cvv);
        this.payment.setExpDate(expDate);
    }

    /**
     * update the address for the account
     * 
     * @param String city, street, houseNumber, state, zip ; all are components of
     *               address info
     */
    public void updateAddressInfo(String city, String street, String houseNumber, String state, int zip) {
        this.address.setStreet(street);
        this.address.setState(state);
        this.address.setHouseNumber(houseNumber);
        this.address.setCity(city);
        this.address.setZip(zip);

    }

    /**
     * Get the id of the account
     * 
     * @return the id of the account
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set the id of an account
     * 
     * @param nextId the next value for the id
     */
    public void setId(int nextId) {
        this.id = nextId;
    }

    // get session id
    public int getSessionID() {
        return this.sessionID;
    }

    // set session id
    public void setSessionID(int id) {
        this.sessionID = id;
    }

    /**
     * Get the username of an account
     * 
     * @return the username of the account as a string
     */
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the email for the account
     * 
     * @return the email of the account
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Get the first name for the account
     * 
     * @return the first name of the account
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Get the last name for the account
     * 
     * @return the last name of the account
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Get the address for the account
     * 
     * @return the address of the account
     */
    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * Get the payment for the account
     * 
     * @return the payment of the account
     */
    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * Get the password for the account
     * 
     * @return the password of the account
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the password for the account
     * 
     * @param password the password of the account
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * check if logged in
     * 
     * @return boolean if logged in
     */
    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    /**
     * Set loggedIn
     * 
     * @param boolean isLoggedIn
     */
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String url) {
        this.profilePicture = url;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
