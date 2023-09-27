import { Component } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { Router } from '@angular/router';

import { ShoppingCart } from '../shopping-cart';

import { Product } from '../product';
import { ProductService } from '../product.service';

import { Account } from '../account';
import { AccountService } from '../account.service';
import { Color } from '../color';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css'],
})
export class ShoppingCartComponent {
  account?: Account;
  products: Product[] = [];
  colors: Color[] = [];
  quantities: number[] = [];
  shoppingCart?: ShoppingCart;

  newQuantity: number = 0;
    
  constructor(private accountService: AccountService, private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.getShoppingCart();
  }

  getShoppingCart(): void {
    if (this.account) {
      console.log(
        'Account found: ' + this.account.id + ', Getting shopping cart...'
      );
      this.accountService
        .getShoppingCart(this.account.id)
        .subscribe((shoppingCart) => {
          this.shoppingCart = shoppingCart;
          this.setShoppingCart();
        });
    } else {
      console.log('No account found');
    }
  }

  setShoppingCart(): void {
    if (this.shoppingCart) {
      this.products = this.shoppingCart.productsInCart;
      this.colors = this.shoppingCart.listofColors;
      this.quantities = this.shoppingCart.productQuan;
    }
  }

  getColor(index: number): string {
    if (this.colors[index]) {
      return this.colors[index].name;
    }
    return '';
  }

  getQuantity(index: number): number {
    if (this.quantities[index]) {
      return this.quantities[index];
    }
    return 0;
  }

  getProduct(index: number): Product {
    if (this.products[index]) {
      return this.products[index];
    }
    return {} as Product;
  }

  increment(index: number): void {
    if (this.account) {
      this.accountService.incrementQuantity(this.account.id, index).subscribe({
        next: () => {
          this.getShoppingCart();
        },
        error: (error) => {
          console.error(error);
        }
      });
    }
  }

  decrement(index: number): void {
    if (this.account) {
      this.accountService.decrementQuantity(this.account.id, index).subscribe({
        next: () => {
          this.getShoppingCart();
        },
        error: (error) => {
          console.error(error);
        }
      });
    }
  }

  remove(index: number): void {
    if (this.account) {
      this.accountService
        .removeProductFromShoppingCart(this.account.id, index)
        .subscribe({
          next: () => {
            this.getShoppingCart();
            window.location.reload();
          },
          error: (error) => {
            console.error(error);
          },
        });
    }
  }

  getTotal(): number {
    let total = 0;
    for (let i = 0; i < this.products.length; i++) {
      total += this.products[i].price * this.quantities[i];
    }
    return total;
  }

  async placeOrder(): Promise<void> {
    if (this.account) {
      console.log('payment= ' + this.account.payment.cardHolder + ', address= ' + this.account.address.city)
      if (this.account.payment.cardHolder === '') {
        alert('Please add a payment method before placing an order.');
        await this.router.navigate(['/payment']);
        await this.waitForPaymentUpdate();
        this.router.navigate(['/shoppingcart']);
        return;
      }
      if (this.account.address.city === '') {
        alert('Please add an address before placing an order.');
        await this.router.navigate(['/address']);
        await this.waitForPaymentUpdate();
        this.router.navigate(['/shoppingcart']);
        return;
      }
      if (this.shoppingCart) {
        for (let i = 0; i < this.shoppingCart.productsInCart.length; i++) {
          if (this.shoppingCart.productQuan[i] > this.shoppingCart.productsInCart[i].quantity) {
            alert("Sorry, we do not have enough " + this.shoppingCart.productsInCart[i].name + " in stock.");
            return;
          }

          this.newQuantity = this.shoppingCart.productsInCart[i].quantity - this.shoppingCart.productQuan[i];
          this.shoppingCart.productsInCart[i].quantity = this.newQuantity;
          this.productService.updateProduct(this.shoppingCart.productsInCart[i]).subscribe({
            next: () => {
              console.log("Product quantity updated!");
            }
          });
        }

        this.accountService.placeOrder(this.account.id).subscribe({
          next: () => {
            alert('Order placed successfully!');
            this.getShoppingCart();
            window.location.reload();
          },
          error: (error) => {
            console.error(error);
          },
        });
      } else {
        alert('Please add items to your cart before placing an order.');
      }
    } else {
      alert('Please log in to place an order.');
    }
  }

  waitForPaymentUpdate() {
    return new Promise<void>((resolve) => {
      const interval = setInterval(() => {
        if (this.account?.payment) {
          clearInterval(interval);
          resolve();
        }
      }, 1000);
    });
  }

  /* Get Current Account */

  getAllAccounts(): Observable<Account[]> {
    return this.accountService.getAccounts();
  }

  async checkIfSessions(): Promise<boolean> {
    let hasSession = false;
    const accounts = await lastValueFrom(this.getAllAccounts());
    accounts.forEach((account: Account) => {
      if (account.sessionID !== 0) {
        console.log('Found a session!');
        hasSession = true;
      }
    });
    return hasSession;
  }

  async getCurrentAccount(): Promise<void> {
    const hasSession = await this.checkIfSessions();
    if (hasSession) {
      try {
        const account = await lastValueFrom(
          this.accountService.getCurrentAccount()
        );
        if (account) {
          this.account = account;
          this.getShoppingCart();
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
