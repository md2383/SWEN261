import { Component } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { Router } from '@angular/router';

import { AccountService } from '../account.service';
import { Account } from '../account';

import { ProductService } from '../product.service';
import { Product } from '../product';

@Component({
  selector: 'app-order-history',
  templateUrl: './order-history.component.html',
  styleUrls: ['./order-history.component.css']
})
export class OrderHistoryComponent {
  products: Product[] = [];
  productQuantity: number[] = [];
  account?: Account;

  constructor(private accountService: AccountService, private productService: ProductService, private router: Router) { }

  ngOnInit(): void {
    this.getCurrentAccount().then(() => {
      this.getOrderHistory();
    });
  }

  review(productId: number): void {
    this.router.navigate(['/review-product', productId]);
  }

  getQuantity(index: number): number {
    return this.productQuantity[index];
  }

  /* Get Order History */

  getOrderHistory(): void {
    if (this.account) {
      this.accountService.getOrderHistory(this.account.id).subscribe({
        next: (product: Product[]) => {
          this.products = product;
          this.getProductQuantity();
        },
        error: (err: any) => console.error(err)
      });
    }
    console.log(this.products);
  }

  getProductQuantity(): void {
    if (this.account) {
      console.log(this.account.id);
      this.accountService.getQuantity(this.account.id).subscribe({
        next: (productQuantity: number[]) => {
          this.productQuantity = productQuantity;
        },
        error: (err: any) => console.error(err)
      });
    }
    console.log(this.productQuantity);
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
        console.log("Found a session!");
        hasSession = true;
      }
    });
    return hasSession;
  }

  async getCurrentAccount(): Promise<void> {
    const hasSession = await this.checkIfSessions();
    if (hasSession) {
      try {
        const account = await lastValueFrom(this.accountService.getCurrentAccount());
        if (account) {
          this.account = account;
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
