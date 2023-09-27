package com.estore.api.estoreapi.controller;

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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.Review;

/**
 * Handles the REST API requests for the Product resource
 *
 * @author Team H
 */
@RestController
@RequestMapping("product")
public class ProductController {
	private static final Logger LOG = Logger.getLogger(ProductController.class.getName());

	private ProductDAO productDAO;

	public ProductController(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	/**
	 * Creates a Product with the provided product object
	 *
	 * @param product - The product to create
	 *
	 * @return ResponseEntity with create product object and HTTP status of CREATED
	 *
	 *         ResponseEntity with HTTP status of CONFLICT if product object already
	 *         exists
	 *
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@PostMapping(value = { "", "/" })
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		LOG.info("POST /product" + product);
		try {
			Product returnValue = productDAO.createProduct(product);
			//Product returnValue = inventory.createProduct(product);
			if (returnValue != null) {
				return new ResponseEntity<Product>(returnValue, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update a Product given the product id and other Product information
	 *
	 * @param product - The product to be updates and its new information
	 *
	 * @return ResponseEntity with create product object and HTTP status of OK
	 *
	 *         ResponseEntity with HTTP status of NOT FOUND if product object does
	 *         not exist
	 *
	 *         ResponseEntity with HTTP status of INTERNAL SERVER ERROR otherwise
	 */
	@PutMapping("")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		LOG.info("PUT /product " + product);
		try {
			Product product_n = productDAO.updateProduct(product);
			if (product_n != null) {
				return new ResponseEntity<Product>(product_n, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Gets all products in the inventory
	 * 
	 * @return ResponseEntity with list of products and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("")
	public ResponseEntity<Product[]> getAllProduct() {
		LOG.info("GET /product");
		try {
			return new ResponseEntity<Product[]>(productDAO.getProducts(), HttpStatus.OK);
			//return new ResponseEntity<Product[]>(inventory.getAllProducts(), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Gets all products in the inventory that contain the provided text
	 * 
	 * @param id - The id of the product to get
	 * 
	 * @return ResponseEntity with list of products and HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
		LOG.info("GET /product/" + id);
		try {
			return new ResponseEntity<Product>(productDAO.getProductById(id), HttpStatus.OK);
			//return new ResponseEntity<Product>(inventory.getProduct(id), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Deletes a product from the Inventory
	 * 
	 * @param product - the product to delete
	 * 
	 * @return ResponseEntity HTTP status of OK if deleted
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") int id) {
		LOG.info("DELETE /product/" + id);
		try {
			// if (inventory.deleteProduct(id))
			if (productDAO.deleteProduct(id)) {
				return new ResponseEntity<Product>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Searches for products from the Inventory containing a string
	 * 
	 * @param contains - the string to search for products
	 * 
	 * @return List of Products containing string used in search and ResponseEntity
	 *         HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/")
	public ResponseEntity<Product[]> searchForProduct(@RequestParam("name") String contains) {
		LOG.info("GET /product/?name=" + contains);
		try {
			return new ResponseEntity<Product[]>(productDAO.searchForProduct(contains), HttpStatus.OK);
			//return new ResponseEntity<Product[]>(inventory.searchForProduct(contains), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Gets all the possible colors that can be used
	 * 
	 * 
	 * @return List of Colors and ResponseEntity HTTP status of OK
	 * 
	 *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@GetMapping("/colors")
	public ResponseEntity<Color[]> getAllColors() {
		LOG.info("GET /product/colors");
		try {
			return new ResponseEntity<Color[]>(productDAO.getAllColors(), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Sets all the possible colors that can be used
	 * 
	 * @param colors - the list of colors to be set
	 * 
	 * @return ResponseEntity with HTTP status of OK when all the colors are set
	 * 
	 * 		   Response Entity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 */
	@PutMapping("/colors")
	public ResponseEntity<Color[]> setAllColors(@RequestBody Color[] colors) {
		LOG.info("PUT /product/colors" + colors);
		try {
			return new ResponseEntity<Color[]>(productDAO.setAllColors(colors), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Adds to all colors that can be used
	 * 
	 * @param	color - the color to be added to the list
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is added
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@PostMapping("/colors")
	public ResponseEntity<Color[]> addColors(@RequestBody Color color) {
		LOG.info("POST /product/colors/" + color);
		try {
			return new ResponseEntity<Color[]>(productDAO.addToAllColors(color), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Removes from all colors that can be used
	 * 
	 * @param	color - the color to be removed from the list
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is removed
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@DeleteMapping("/colors")
	public ResponseEntity<Color[]> removeColors(@RequestBody Color color) {
		LOG.info("DELETE /product/colors/" + color);
		try {
			return new ResponseEntity<Color[]>(productDAO.removeFromAllColors(color), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Gets a color from a product
	 * 
	 * @param	productid - the id of the product to be retrieved from
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is retrieved
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@GetMapping("/{id}/colors")
	public ResponseEntity<Color[]> getColors(@PathVariable("id") int productid) {
		LOG.info("GET /product/" + productid + "/colors");
		try {
			return new ResponseEntity<Color[]>(productDAO.getProductColors(productid), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Sets colors for a product
	 * 
	 * @param	productid - the id of the product to be set
	 * 
	 * @param	colors - the list of colors to add the product to 
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is set
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@PutMapping("/{id}/colors")
	public ResponseEntity<Color[]> setProductColors(@PathVariable("id") int productid, @RequestBody Color[] colors) {
		LOG.info("PUT /product/" + productid + "/colors/" + colors);
		try {
			return new ResponseEntity<Color[]>(productDAO.setProductColors(productid, colors), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Adds colors to a product
	 * 
	 * @param productid - the id of the product to be added
	 * @param color - the color to be added
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is added
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@PostMapping("/{id}/colors")
	public ResponseEntity<Color[]> addProductColors(@PathVariable("id") int productid, @RequestBody Color color) {
		LOG.info("POST /product/" + productid + "/colors/" + color);
		try {
			return new ResponseEntity<Color[]>(productDAO.addProductColor(productid, color), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Removes colors from a product
	 * 
	 * @param	productid - the id of the product to be removed
	 * @param	color - the color to be removed from the product
	 * 
	 * @return	ResponseEntity with HTTP status of OK when the color is removed
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@DeleteMapping("/{id}/colors")
	public ResponseEntity<Color[]> removeProductColors(@PathVariable("id") int productid, @RequestBody Color color) {
		LOG.info("DELETE /product/" + productid + "/colors/" + color);
		try {
			return new ResponseEntity<Color[]>(productDAO.removeProductColor(productid, color), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/review")
	public ResponseEntity<Review[]> getAllReviews() {
		LOG.info("GET /product/review");
		try {
			return new ResponseEntity<Review[]>(productDAO.getAllReviews(), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * create a review 
	 * 
	 * @param	review - a review
	 * 
	 * @return	ResponseEntity with HTTP status of OK when review is created 
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@PostMapping("/review/create")
	public ResponseEntity<Review> createReview(@RequestBody Review review) {
		LOG.info("POST /product/review/create" + review);
		try {
			Review newReview = productDAO.createReview(review);
			if (newReview != null) {
				return new ResponseEntity<Review>(newReview, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * update a review 
	 * 
	 * @param	review - a review
	 * 
	 * @return	ResponseEntity with HTTP status of OK when review is updated 
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	// @DeleteMapping("/review/delete")
	// public ResponseEntity<Review> removeReview(@RequestBody Review review) {
	// 	LOG.info("DELETE /product/review/delete" + review);
	// 	try {
	// 		return new ResponseEntity<Review>(productDAO.removeReview(review), HttpStatus.OK);
	// 	} catch (Exception e) {
	// 		LOG.log(Level.SEVERE, e.getLocalizedMessage());
	// 		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// 	}
	// }

	/**
	 * get the list of reviews by id of a product
	 * 
	 * @param	productid - the id of the product
	 * 
	 * @return	ResponseEntity with HTTP status of OK if product exist 
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@GetMapping("/review/product/{id}")
	public ResponseEntity<Review[]> getReviewByProductId(@PathVariable("id") int productid) {
		LOG.info("GET /product/review/product/" + productid);
		LOG.info("Review: userid " + productid);
		try {
			Review[] res = productDAO.getReviewsByProduct(productid);
			if (res != null) {
				return new ResponseEntity<Review[]>(res, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * get the list of reviews by id of a user
	 * 
	 * @param	userid - the id of the user
	 * 
	 * @return	ResponseEntity with HTTP status of OK if user exist 
	 * 
	 * 			ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
	 * 
	 */
	@GetMapping("/review/user/{id}")
	public ResponseEntity<Review[]> getReviewByUserId(@PathVariable("id") int userid) {
		LOG.info("GET /product/review/user/" + userid);
		try {
			return new ResponseEntity<Review[]>(productDAO.getReviewsByUser(userid), HttpStatus.OK);
		} catch (Exception e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
