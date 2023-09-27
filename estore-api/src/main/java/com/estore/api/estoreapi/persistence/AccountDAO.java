package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.Address;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.model.Payment;
import com.estore.api.estoreapi.model.Product;

public interface AccountDAO {
    Account[] getAllAccounts() throws IOException;

    Account createAccount(Account account) throws IOException;

    Account loginAccount(String username, String password) throws IOException;

    Account updateAccount(Account account) throws IOException;

    Account deleteAccount(int userid) throws IOException;

    Account logoutAccount(String usernameString) throws IOException;

    Account getCurrentAccount(int userid) throws IOException;

    ShoppingCart getShoppingCart(int userid) throws IOException;

    ShoppingCart addProductToShoppingCart(int userid, Product product) throws IOException;

    ShoppingCart addColorToShoppingCart(int userid, Color color) throws IOException;

    ShoppingCart removeProductFromShoppingCart(int userid, int index) throws IOException;

    ShoppingCart incrementQuantity(int userid, int index) throws IOException;

    ShoppingCart decrementQuantity(int userid, int index) throws IOException;

    ShoppingCart clearShoppingCart(int userid) throws IOException;

    ShoppingCart updateShoppingCart(int userid, ShoppingCart shoppingCart) throws IOException;

    Payment getPayment(int userid) throws IOException;

    Payment updatePayment(int userid, Payment payment) throws IOException;

    Address getAddress(int userid) throws IOException;

    Address updateAddress(int userid, Address address) throws IOException;
    
    Product[] productsAllowedToReview(int userid) throws IOException;

    int[] productsAllowedToReviewQuantities(int userid) throws IOException;

    Account getAccountById(int id) throws IOException;
}
