package com.estore.api.estoreapi.model;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShoppingCart {
    // What is in the cart currently
    private Product[] productsInCart;
    private Color[] listofColors;
    private int[] productQuan;

    private Product mostRecentProduct = null;

    // What was purchased in the past
    private List<Product> productHistory;
    private List<Integer> productHistoryQuan;

    public ShoppingCart() {
        this.productsInCart = new Product[0];
        this.listofColors = new Color[0];
        this.productQuan = new int[0];

        this.productHistory = new ArrayList<>();
        this.productHistoryQuan = new ArrayList<>();
    }

    /**
     * get the quantity of the product history
     * 
     * @return int list of quantities
     * 
     * 
     */
    public int[] getProductHistoryQuan() {
        int[] temp = new int[this.productHistoryQuan.size()];
        for (int i = 0; i < this.productHistoryQuan.size(); i++) {
            temp[i] = this.productHistoryQuan.get(i);
        }
        return temp;
    }

    public List<Product> getProductHistory() {
        return productHistory;
    }

    public Product[] getProductsInCart() {
        return productsInCart;
    }

    public Color[] getListofColors() {
        return listofColors;
    }

    public int[] getProductQuan() {
        return productQuan;
    }

    public Product getMostRecentProduct() {
        return mostRecentProduct;
    }

    /**
     * add product to shopping cart
     * 
     * @param product - the product to be added
     * 
     * @return ResponseEntity with HTTP status of OK when the product is added to
     *         cart
     * 
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     */
    public ShoppingCart addProductToShoppingCart(Product product) {
        if (productsInCart.length == 0) { // If list is empty
            this.productsInCart = new Product[] { product };
            this.productQuan = new int[] { 1 };
            return this;
        }

        // Add product to cart if list is not empty
        Product[] tempInCart = new Product[this.productsInCart.length + 1];
        for (int i = 0; i < this.productsInCart.length; i++) {
            tempInCart[i] = this.productsInCart[i];
        }
        tempInCart[tempInCart.length - 1] = product;
        this.productsInCart = tempInCart;
        this.mostRecentProduct = product; // Saves most recent product

        return this;
    }

    /**
     * add colors to shopping cart
     * 
     * @param color - the color to be added to shopping cart
     * 
     * @return ResponseEntity with HTTP status of OK when the color is added
     * 
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     */
    public ShoppingCart addColorToShoppingCart(Color color) {
        if (listofColors.length == 0) { // If list is empty
            this.listofColors = new Color[] { color };
            this.productQuan = new int[] { 1 };
            return this;
        }

        boolean colorExists = false;

        // Check if color and the product already exists in the cart
        for (int i = 0; i < this.listofColors.length; i++) {
            if (this.listofColors[i].getName().equals(color.getName()) && (this.mostRecentProduct == null
                    || this.mostRecentProduct.getId() == this.productsInCart[i].getId())) {
                this.productQuan[i] += 1;
                colorExists = true;

                // Delete the product from the cart that was just added before because it
                // already exists
                Product[] tempInCart = new Product[this.productsInCart.length - 1];
                for (int j = 0, k = 0; j < this.productsInCart.length; j++) {
                    if (j != i) {
                        tempInCart[k++] = this.productsInCart[j];
                    }
                }
                this.productsInCart = tempInCart;
            }
        }

        if (!colorExists) { // If color does not exist in the cart
            Color[] tempInCartColor = new Color[this.listofColors.length + 1];
            for (int i = 0; i < this.listofColors.length; i++) {
                tempInCartColor[i] = this.listofColors[i];
            }
            tempInCartColor[tempInCartColor.length - 1] = color;
            this.listofColors = tempInCartColor;

            int[] tempInCartQuan = new int[this.productQuan.length + 1];
            for (int i = 0; i < this.productQuan.length; i++) {
                tempInCartQuan[i] = this.productQuan[i];
            }
            tempInCartQuan[tempInCartQuan.length - 1] = 1;
            this.productQuan = tempInCartQuan;
        }

        return this;
    }

    /**
     * clear the cart
     * 
     */
    public void clearCart() {
        for (int i = 0; i < this.productsInCart.length; i++) {
            this.productHistory.add(this.productsInCart[i]);
            this.productHistoryQuan.add(this.productQuan[i]);
        }
        this.productsInCart = new Product[0];
        this.listofColors = new Color[0];
        this.productQuan = new int[0];
    }

    /**
     * remove a product from the cart
     * 
     * @param product product, color color
     */
    public void removeProductFromShoppingCart(int index) {

        if (index >= 0 && index < this.productsInCart.length) { // If index is valid
            boolean productExists = false;

            Product[] tempInCart = new Product[this.productsInCart.length - 1];
            Color[] tempInCartColor = new Color[this.listofColors.length - 1];
            int[] tempInCartQuan = new int[this.productQuan.length - 1];

            for (int i = 0; i < this.productsInCart.length - 1; i++) {
                if (productExists == false) {
                    if (i == index) { // If product exists in the cart
                        productExists = true;
                    } else { // If product does not exist in the cart
                        tempInCart[i] = this.productsInCart[i];
                        tempInCartColor[i] = this.listofColors[i];
                        tempInCartQuan[i] = this.productQuan[i];
                    }
                }

                if (productExists == true) { // If product exists in the cart
                    tempInCart[i] = this.productsInCart[i + 1];
                    tempInCartColor[i] = this.listofColors[i + 1];
                    tempInCartQuan[i] = this.productQuan[i + 1];
                }
            }

            this.productsInCart = tempInCart;
            this.listofColors = tempInCartColor;
            this.productQuan = tempInCartQuan;
        }
    }

    /**
     * increase quantity of a product
     * 
     * @param index - the index of shopping cart list
     * 
     * @return ResponseEntity with HTTP status of OK if index exist
     * 
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     */
    public void incrementQuantity(int index) {
        if (index >= 0 && index < this.productQuan.length) { // If index is valid
            this.productQuan[index] += 1;
        }
    }

    /**
     * decrease quantity of a product
     * 
     * @param index - the index of shopping cart list
     * 
     * @return ResponseEntity with HTTP status of OK if index exist
     * 
     *         ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * 
     */
    public void decrementQuantity(int index) {
        if (index >= 0 && index < this.productQuan.length) { // If index is valid
            if (this.productQuan[index] > 1) {
                this.productQuan[index] -= 1;
            } else if (this.productQuan[index] == 1) {
                removeProductFromShoppingCart(index);
            }
        }
    }
}