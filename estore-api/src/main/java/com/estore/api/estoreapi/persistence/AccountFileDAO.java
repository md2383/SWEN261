package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.AccountAuthentication;
import com.estore.api.estoreapi.model.AccountSession;
import com.estore.api.estoreapi.model.ActiveAccountSessions;
import com.estore.api.estoreapi.model.Address;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Payment;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AccountFileDAO implements AccountDAO {
    private static final Logger LOG = Logger.getLogger(AccountFileDAO.class.getName());
    private Map<Integer, Account> accounts = null;

    private ObjectMapper mapper;
    private String filename;

    private ActiveAccountSessions activeSessions = new ActiveAccountSessions();
    private AccountSession accountSession;
    private AccountAuthentication accountAuth = new AccountAuthentication();

    private static int nextId;

    public AccountFileDAO(@Value("${account.file}") String filename, ObjectMapper mapper) throws IOException {
        LOG.info(filename);
        this.filename = filename;
        this.mapper = mapper;
        load();
    }

    /**
     * Serialize all accounts currently in memory to the disk
     *
     * @return true if the accounts were successfully serialized and written to the
     *         disk
     */
    private boolean save() throws IOException {
        Account[] accountArray = getAllAccountsArray(null);
    
        String filePath = new File(filename).getAbsolutePath();
        LOG.info("Saving accounts to file: " + filePath);
    
        mapper.writeValue(new File(filename), accountArray);
        return true;
    }
    
    

    /**
     * Deserialize all accounts from the disk
     *
     * @return true if the accounts were successfully deserialized and read from the
     *         disk
     */
    private boolean load() throws IOException {
        accounts = new TreeMap<>();
        nextId = 0;
        LOG.info(filename);

        Account[] accountArray = mapper.readValue(new File(filename), Account[].class);
        for (Account account : accountArray) {
            int id = account.getId();
            accounts.put(id, account);
            if (id > nextId) {
                nextId = id;
            }
        }
        nextId++;
        return true;
    }

    /**
     * Get the next available account id
     * 
     * @return the next id
     */
    private synchronized static int getNextId() {
        int id = nextId++;
        return id;
    }

    /**
     * @return all the accounts in the list
     */
    private Account[] getAllAccountsArray(String containsText) {
        synchronized (accounts) {
            List<Account> accountList = new ArrayList<>();
            for (Account account : accounts.values()) {
                if (containsText == null || account.getUsername().contains(containsText)) {
                    accountList.add(account);
                }
            }
            return accountList.toArray(new Account[0]);
        }
    }

    /**
     * @return list of all the account values in the map
     */
    @Override
    public Account[] getAllAccounts() {
        synchronized (accounts) {
            return getAllAccountsArray(null);
        }
    }

    
    @Override
    public Account getAccountById(int id) {
        synchronized (accounts) {
            return accounts.get(id);
        }
    }

    /**
     * @param account is the account to create and add to the list
     * 
     * @return the account that was created
     */
    @Override
    public Account createAccount(Account account) throws IOException {
        synchronized (accounts) {
            // Check if the username or email already exists
            for (Account a : accounts.values()) {
                if (a.getUsername().equals(account.getUsername()) || a.getEmail().equals(account.getEmail())) {
                    return null; // Return null if username or email already exists
                }
            }

            // Generate a new ID and set it to the account
            int id = getNextId();
            account.setId(id);

            // Add the account to the accounts map
            accounts.put(id, account);

            // Log in the account
            Account loggedAccount = loginAccount(account.getUsername(), account.getPassword());

            // Save the changes
            save();

            // Return the logged-in account
            return loggedAccount;
        }
    }

    /**
     * log in to an account given the username and password
     * 
     * @param username the username of the account to login
     * 
     * @param password the password of the account to login
     * 
     * @return true if the account was logged in successfully, else false
     */
    @Override
    public Account loginAccount(String username, String password) throws IOException {
        for (Account account : accounts.values()) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                try {
                    accountSession = new AccountSession(account, activeSessions);
                } catch (Exception e) {
                    return null;
                }
                activeSessions.addSession(accountSession);
                account.setIsLoggedIn(true);
                account.setSessionID(accountSession.getSessionID());
                save();
                return account;
            }
        }
        return null;

    }

    /**
     * @param username the username of the account to login
     * 
     * @param password the password of the account to login
     * 
     * @return true if the account was logged in
     */
    @Override
    public Account logoutAccount(String username) throws IOException {
        for (Account account : accounts.values()) {
            if (account.getUsername().equals(username)) {
                for (AccountSession session : activeSessions.getSessions()) {
                    if (accountAuth.accountAuthentication(session, account)) {
                        activeSessions.removeSession(accountSession); // remove session
                        accountSession = null; // delete session
                        account.setIsLoggedIn(false);
                        account.setSessionID(0);
                        save();
                        LOG.info("FileDAO: " + account.getUsername() + " logged out");
                        return account;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param account the account to update
     * 
     * @param id      the id of the account to delete
     * 
     * @return true if the account was deleted
     */
    @Override
    public Account deleteAccount(int id) throws IOException {
        synchronized (accounts) {
            if (accounts.containsKey(id)) {
                for (AccountSession session : activeSessions.getSessions()) {
                    if (accountAuth.accountAuthentication(session, accounts.get(id))) {
                        activeSessions.removeSession(accountSession); // remove
                        accountSession = null; // delete session
                        accounts.remove(id);
                        save();
                        return null;
                    }
                }
            }
            return accounts.get(id);
        }
    }

    /**
     * Replace existing account with the new updated account
     * 
     * @param account the account to update
     * 
     * @return the updated account
     */
    @Override
    public Account updateAccount(Account account) throws IOException {
        synchronized (accounts) {
            for (AccountSession session : activeSessions.getSessions()) {
                if (accountAuth.accountAuthentication(session, account)) {
                    accounts.put(account.getId(), account);
                    save();
                    return account;
                }
            }
            return null;
        }
    }

    /**
     * Gets the session id of the account that is logged in
     * 
     * @param id the id of the account session to get
     * 
     * @return the account session id that is currently logged in
     */
    @Override
    public Account getCurrentAccount(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getSessionID() == id) {
                return session.getLoggedAccount();
            }
        }
        return null;
    }

    /**
     * get the shopping cart
     * 
     * @param int id of account
     * @return the shopping cart
     */
    @Override
    public ShoppingCart getShoppingCart(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                System.out.println("getting shopping cart from user: " + session.getLoggedAccount().getUsername()
                        + " , shopping cart" + session.getLoggedAccount().getShoppingCart()
                        + ".");
                return session.getLoggedAccount().getShoppingCart();
            }
        }
        return new ShoppingCart();
    }


    /**
     * add product to cart 
     * 
     * @param int id of user 
     * @param product the product 
     * @return the shopping cart 
     */
    @Override
    public ShoppingCart addProductToShoppingCart(int userid, Product product)
            throws IOException {

        if (product == null) {
            return new ShoppingCart();
        }

        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    ShoppingCart cart = session.getLoggedAccount().getShoppingCart().addProductToShoppingCart(product);
                    session.getLoggedAccount().setShoppingCart(cart);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    /**
     * add color to cart 
     * 
     * @param int id of user 
     * @param color the color 
     * @return the shopping cart 
     */
    @Override
    public ShoppingCart addColorToShoppingCart(int userid, Color color)
            throws IOException {
        Color c = color;

        if (c == null) {
            return new ShoppingCart();
        }

        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    ShoppingCart cart = session.getLoggedAccount().getShoppingCart().addColorToShoppingCart(c);
                    session.getLoggedAccount().setShoppingCart(cart);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }   

    /**
     * remove product to cart 
     * 
     * @param int id of user 
     * @param int the index  
     * @return the shopping cart 
     */
    @Override
    public ShoppingCart removeProductFromShoppingCart(int userid, int index) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    session.getLoggedAccount().getShoppingCart().removeProductFromShoppingCart(index);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    /**
     * increase quantity of the product
     * 
     * @param int id of user 
     * @param int the index  
     * @return the shopping cart 
     */
    @Override
    public ShoppingCart incrementQuantity(int userid, int index) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    session.getLoggedAccount().getShoppingCart().incrementQuantity(index);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    /**
     * decrease quantity of the product
     * 
     * @param int id of user 
     * @param int the index  
     * @return the shopping cart 
     */
    @Override
    public ShoppingCart decrementQuantity(int userid, int index) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    session.getLoggedAccount().getShoppingCart().decrementQuantity(index);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    /**
     * clear all cart from session
     * 
     * @param id of userid
     * @return updated session of cart
     */
    @Override
    public ShoppingCart clearShoppingCart(int userid) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    session.getLoggedAccount().getShoppingCart().clearCart();
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    @Override
    public ShoppingCart updateShoppingCart(int userid, ShoppingCart cart) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == userid) {
                try {
                    session.getLoggedAccount().setShoppingCart(cart);
                    save();
                    return session.getLoggedAccount().getShoppingCart();
                } catch (Exception e) {
                    return session.getLoggedAccount().getShoppingCart();
                }
            }
        }
        return new ShoppingCart();
    }

    /**
     * get payment info of the active session
     * 
     * @param int id of current session
     * @return payment info
     */
    @Override
    public Payment getPayment(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                return session.getLoggedAccount().getPayment();
            }
        }
        return null;
    }

    /**
     * update payment info of the active session
     * 
     * @param int id of current session, payment payment info
     * @return payment info
     */
    @Override
    public Payment updatePayment(int id, Payment payment) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                session.getLoggedAccount().updatePaymentInfo(payment.getCardHolder(), payment.getCardNumber(),
                        payment.getCvv(), payment.getExpDate());
                save();
                return session.getLoggedAccount().getPayment();
            }
        }
        return null;
    }

    /**
     * get address info of the active session
     * 
     * @param int id of current session
     * @return address info
     */
    @Override
    public Address getAddress(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getSessionID() == id) {
                return session.getLoggedAccount().getAddress();
            }
        }
        return null;
    }

    /**
     * update address info of the active session
     * 
     * @param int id of current session, address address info
     * @return address info
     */
    @Override
    public Address updateAddress(int id, Address address)
            throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                session.getLoggedAccount().updateAddressInfo(address.getCity(), address.getStreet(),
                        address.getHouseNumber(), address.getState(), address.getZip());
                save();
                return session.getLoggedAccount().getAddress();
            }
        }
        return null;
    }

    /**
     * show products that they have bought
     * 
     * @param int id of current session
     * @return list of products
     */
    @Override
    public Product[] productsAllowedToReview(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                return session.getLoggedAccount().getShoppingCart().getProductHistory().toArray(new Product[0]);
            }
        }
        return new Product[0];
    }

    /**
     * show product quantities that they have bought
     * 
     * @param int id of current session
     * @return list of quantities
     */
    @Override
    public int[] productsAllowedToReviewQuantities(int id) throws IOException {
        for (AccountSession session : activeSessions.getSessions()) {
            if (session.getLoggedAccountID() == id) {
                return session.getLoggedAccount().getShoppingCart().getProductHistoryQuan();
            }
        }
        return new int[0];
    }
}