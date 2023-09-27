package com.estore.api.estoreapi.model;

import com.estore.api.estoreapi.model.Color;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class ShoppingCartTest {
	private ShoppingCart shoppingCart;
	private Product product1;
	private Product product2;
	private Color color1;
	private Color color2;

	@BeforeEach
	public void setUp() {
		shoppingCart = new ShoppingCart();
		product1 = new Keyboard(1, Arrays.asList(new Color("Black")), "Keyboard 1", 10, 50.0,
				"http://example.com/keyboard.jpg");
		product2 = new Mouse(2, Arrays.asList(new Color("White")), "Mouse 2", 12, 30.0,
				"http://example.com/mouse.jpg");
		color1 = new Color("Black");
		color2 = new Color("White");

		// add product1 and product2 to the shopping cart
		shoppingCart.addProductToShoppingCart(product1);
		shoppingCart.addColorToShoppingCart(color2);

	}

	@Test
	public void testAddProductToShoppingCart() {
		// Assert
		assertEquals(1, shoppingCart.getProductQuan().length);
		assertEquals(1, shoppingCart.getProductQuan()[0]);
	}

	@Test
	public void testAddColorToShoppingCart() {
		// Assert
		assertEquals(1, shoppingCart.getProductQuan().length);
		assertEquals(1, shoppingCart.getProductQuan()[0]);
	}

	@Test
	public void testClearCart() {
		// Act
		shoppingCart.clearCart();

		// Assert
		assertEquals(0, shoppingCart.getProductQuan().length);
	}

	@Test
	public void testGetProductHistory() {
		// Act
		shoppingCart.addProductToShoppingCart(product2);
		shoppingCart.addColorToShoppingCart(color2);

		// checkout
		shoppingCart.clearCart	();

		// Assert
		assertEquals(2, shoppingCart.getProductHistory().size());
		assertEquals(product1, shoppingCart.getProductHistory().get(0));
		assertEquals(product2, shoppingCart.getProductHistory().get(1));
	}

	@Test
	public void testRemoveProductFromCart() {
		// Act
		shoppingCart.removeProductFromShoppingCart(0);

		// Assert
		assertEquals(0, shoppingCart.getProductQuan().length);
	}

	@Test
	public void testGetMostRecentProduct() {
		shoppingCart.addProductToShoppingCart(product1);
		shoppingCart.addColorToShoppingCart(color1);
		shoppingCart.addProductToShoppingCart(product2);
		shoppingCart.addColorToShoppingCart(color2);
		assertEquals(product2, shoppingCart.getMostRecentProduct());
	}

	@Test
	public void getProductHistory() {
		shoppingCart.addProductToShoppingCart(product1);
		shoppingCart.addColorToShoppingCart(color1);
		shoppingCart.addProductToShoppingCart(product2);
		shoppingCart.addColorToShoppingCart(color2);

		// checkout
		shoppingCart.clearCart();

		// Assert
		assertEquals(3, shoppingCart.getProductHistory().size());
	}

	@Test
	public void incrementProductQuantity() {
		shoppingCart.addProductToShoppingCart(product1);
		shoppingCart.addColorToShoppingCart(color1);
		shoppingCart.addProductToShoppingCart(product2);
		shoppingCart.addColorToShoppingCart(color2);
		shoppingCart.incrementQuantity(0);
		shoppingCart.incrementQuantity(-1);
		shoppingCart.incrementQuantity(10);
		assertEquals(2, shoppingCart.getProductQuan()[0]);
	}

	@Test
	public void decrementProductQuantity() {
		shoppingCart.addProductToShoppingCart(product1);
		shoppingCart.addColorToShoppingCart(color1);
		shoppingCart.addProductToShoppingCart(product2);
		shoppingCart.addColorToShoppingCart(color2);
		shoppingCart.incrementQuantity(0);
		shoppingCart.incrementQuantity(-1);
		shoppingCart.incrementQuantity(10);
		shoppingCart.decrementQuantity(0);
		assertEquals(1, shoppingCart.getProductQuan()[0]);
	}

	@Test
	public void testDecrementProductQuantityInvaildIndex() {
		// Act
		shoppingCart.decrementQuantity(999);

		// Assert
		assertEquals(1, shoppingCart.getProductQuan().length);
	}
}
