package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Account;
import com.estore.api.estoreapi.model.Address;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Payment;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import com.estore.api.estoreapi.persistence.AccountDAO;

/**
 * Handles the REST API requests for Accounts
 * 
 * @author Team H
 */

@RestController
@RequestMapping("/account")
public class AccountController {
	private static final Logger LOG = Logger.getLogger(AccountController.class.getName());
	private AccountDAO accountDAO;

	public AccountController(AccountDAO accountDao) {
		this.accountDAO = accountDao;
	}

	/**
	 * Get all accounts in the system
	 * 
	 * @return ResponseEntity with the list of accounts and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping(value = { "", "/" })
	public ResponseEntity<Account[]> getAllAccounts() {
		LOG.info("GET /account");
		try {
			Account[] returnValue = accountDAO.getAllAccounts();
			return new ResponseEntity<Account[]>(returnValue, HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Get =the account by id 
	 * 
	 * @param int id, unqiue id of account 
	 * @return ResponseEntity HTTP status of OK if id exist 
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") int id) {
		LOG.info("GET /account/" + id);
		try {
			Account returnValue = accountDAO.getAccountById(id);
			if (returnValue != null) {
				return new ResponseEntity<Account>(returnValue, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Creates an account with the provided account object
	 * 
	 * @param account The account to create
	 * 
	 * @return ResponseEntity with create account object and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@PostMapping("/create")
	public ResponseEntity<Account> createAccount(@RequestBody Account account) {
		LOG.info("POST /account/create" + account);
		try {
			Account returnValue = accountDAO.createAccount(account);
			if (returnValue != null) {
				return new ResponseEntity<Account>(returnValue, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update the password of the current account
	 * 
	 * @param password The password of the account that needs to be updated
	 * 
	 * @return the account that was updated
	 */
	@PostMapping("/update")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
		LOG.info("POST /account/update" + account);
		try {
			Account returnValue = accountDAO.updateAccount(account);
			if (returnValue != null) {
				return new ResponseEntity<Account>(returnValue, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete an account given ID
	 * 
	 * @param id The ID of the account
	 * 
	 * @return ResponseEntity with the HTTP status of OK when account is deleted
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 * 
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Account> deleteAccount(@PathVariable("id") int id) {
		LOG.info("DELETE /account/" + id);
		try {
			if (accountDAO.deleteAccount(id) == null) {
				return new ResponseEntity<Account>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Login the account given the username and password
	 * 
	 * @param username The username of the account
	 * 
	 * @param password The password of the account
	 * 
	 * @return ResponseEntity with the result of the login and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account is already logged in
	 * 
	 *         ResponseEntity with HTTP status of UNAUTHORIZED if the username or
	 *         password is incorrect
	 */
	@GetMapping("/login")
	public ResponseEntity<Account> loginAccount(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		LOG.info("GET /account/login?username=" + username + "&password=" + password);
		try {
			Account res = accountDAO.loginAccount(username, password);
			if (res != null) {
				;
				return new ResponseEntity<Account>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * Logout the account given the username
	 * 
	 * @param username The username of the account
	 * 
	 * @return ResponseEntity with the result of the logout and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account is not logged in
	 */
	@GetMapping("/logout")
	public ResponseEntity<Account> logoutAccount(@RequestParam("username") String username) {
		LOG.info("GET /account/logout?username=" + username);
		try {
			Account res = accountDAO.logoutAccount(username);
			if (res != null) {
				return new ResponseEntity<Account>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get current account
	 * 
	 * @param int unique id of account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/current/{id}")
	public ResponseEntity<Account> getCurrentAccount(@PathVariable("id") int id) {
		LOG.info("GET /account/current/" + id);
		try {
			Account res = accountDAO.getCurrentAccount(id);
			if (res != null) {
				return new ResponseEntity<Account>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get shopping cart of the account
	 * 
	 * @param int id of the account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/cart/{id}")
	public ResponseEntity<ShoppingCart> getShoppingCart(@PathVariable("id") int userid) {
		LOG.info("GET /account/cart/" + userid);
		try {
			ShoppingCart res = accountDAO.getShoppingCart(userid);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * add product to shopping cart
	 * 
	 * @param int unique id of account and int unique id of product
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist and product exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account or product does not exist
	 */
	@PostMapping("/cart/{id}/add/product")
	public ResponseEntity<ShoppingCart> addProductToShoppingCart(@PathVariable("id") int userid, @RequestBody Product product) {
		LOG.info("POST /account/" + "/cart/" + userid +"/add/" + product);
		try {
			ShoppingCart res = accountDAO.addProductToShoppingCart(userid, product);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * add color to shopping cart
	 * 
	 * @param int unique id of account and color color 
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist and color exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account or color does not exist
	 */
	@PostMapping("/cart/{id}/add/color")
	public ResponseEntity<ShoppingCart> addColorToShoppingCart(@PathVariable("id") int userid, @RequestBody Color color) {
		LOG.info("POST /account/" + "/cart/" + userid +"/add/" + color);
		try {
			ShoppingCart res = accountDAO.addColorToShoppingCart(userid, color);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * delete product from shopping cart
	 * 
	 * @param int unique id of account and int index of the product 
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist and index exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account or index does not exist
	 */
	@PostMapping("/cart/{id}/remove/{index}")
	public ResponseEntity<ShoppingCart> removeProductFromShoppingCart(@PathVariable("id") int userid, @PathVariable("index") int index) {
		LOG.info("POST /account/cart/" + userid + "/remove/" + index);
		try {
			ShoppingCart res = accountDAO.removeProductFromShoppingCart(userid, index);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * increase quantity of the product from cart 
	 * 
	 * @param int unique id of account and int index of the cart 
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist and index exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account or index does not exist
	 */
	@PostMapping("/cart/{id}/increment/{index}")
	public ResponseEntity<ShoppingCart> incrementQuantity(@PathVariable("id") int userid, @PathVariable("index") int index) {
		LOG.info("POST /account/cart/" + userid + "/increment/" + index);
		try {
			ShoppingCart res = accountDAO.incrementQuantity(userid, index);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * decrease quantity of the product from cart 
	 * 
	 * @param int unique id of account and int index of the cart 
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist and index exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account or index does not exist
	 */
	@PostMapping("/cart/{id}/decrement/{index}")
	public ResponseEntity<ShoppingCart> decrementQuantity(@PathVariable("id") int userid, @PathVariable("index") int index) {
		LOG.info("POST /account/cart/" + userid + "/increment/" + index);
		try {
			ShoppingCart res = accountDAO.decrementQuantity(userid, index);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * clear all product from shopping cart
	 * 
	 * @param int unique id of account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */

	@PostMapping("/cart/{id}/clear")
	public ResponseEntity<ShoppingCart> clearShoppingCart(@PathVariable("id") int id) {
		LOG.info("POST /account/cart/" + id + "/clear");
		try {
			ShoppingCart res = accountDAO.clearShoppingCart(id);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/cart/{id}/update")
	public ResponseEntity<ShoppingCart> updateShoppingCart(@PathVariable("id") int id, @RequestBody ShoppingCart shoppingCart) {
		LOG.info("PUT /account/cart/" + id + "/update");
		try {
			ShoppingCart res = accountDAO.updateShoppingCart(id, shoppingCart);
			return new ResponseEntity<ShoppingCart>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get payment of account
	 * 
	 * @param int unique id of account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/payment/{id}")
	public ResponseEntity<Payment> getPayment(@PathVariable("id") int id) {
		LOG.info("GET /account/payment/" + id);
		try {
			Payment res = accountDAO.getPayment(id);
			if (res != null) {
				return new ResponseEntity<Payment>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * update payment of account
	 * 
	 * @param int unique id of account and payment payment info
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@PostMapping("/{id}/payment")
	public ResponseEntity<Payment> updatePayment(@PathVariable("id") int id, @RequestBody Payment payment) {
		LOG.info("POST /account/" + id + "/payment/" + payment);
		try {
			Payment updatedPayment = accountDAO.updatePayment(id, payment);
			if (updatedPayment != null) {
				return new ResponseEntity<Payment>(updatedPayment, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get address of account
	 * 
	 * @param int unique id of account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/address/{id}")
	public ResponseEntity<Address> getAddress(@PathVariable("id") int id) {
		LOG.info("GET /account/address/" + id);
		try {
			Address res = accountDAO.getAddress(id);
			if (res != null) {
				return new ResponseEntity<Address>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * update address of account
	 * 
	 * @param int unique id of account and Address address info
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@PostMapping("/{id}/address/")
	public ResponseEntity<Address> updateAddress(@PathVariable("id") int id, @RequestBody Address address) {
		LOG.info("POST /account/" + id + "/address/" + address);
		try {
			Address res = accountDAO.updateAddress(id, address);
			if (res != null) {
				return new ResponseEntity<Address>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get order history of account
	 * 
	 * @param int unique id of account
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist exist
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/order-history/{id}")
	public ResponseEntity<Product[]> getOrderHistory(@PathVariable("id") int id) {
		LOG.info("GET /account/order-history/" + id);
		try {
			Product[] res = accountDAO.productsAllowedToReview(id);
			if (res != null) {
				return new ResponseEntity<Product[]>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get quantity of products that has being purchased 
	 * 
	 * @param int unique id of account  
	 * 
	 * @return ResponseEntity HTTP status of OK if account exist 
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if the
	 *         account does not exist
	 */
	@GetMapping("/order-history/{id}/quantity")
	public ResponseEntity<int[]> getOrderHistoryQuantity(@PathVariable("id") int id) {
		LOG.info("GET /account/order-history/" + id + "/quantity");
		try {
			int[] res = accountDAO.productsAllowedToReviewQuantities(id);
			return new ResponseEntity<int[]>(res, HttpStatus.OK);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}