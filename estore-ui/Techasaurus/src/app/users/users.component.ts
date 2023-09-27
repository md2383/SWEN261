import { Component, OnInit } from '@angular/core';
import { lastValueFrom, Observable } from 'rxjs';


import { Account } from '../account';
import { Review } from '../review';
import { AccountService } from '../account.service';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  account?: Account;
  users: Account[] = [];
  reviews: Review[] = [];

  constructor(private accountService: AccountService, private productService: ProductService) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.getAllUsers();
  }


  delete(userId: number): void {
    this.accountService.deleteAccount(userId).subscribe(account => {
      this.getAllUsers();
      window.location.reload();
    });
  }

  /* Get All Users */
  getAllUsers(): void {
    this.accountService.getAccounts().subscribe(accounts => {
      accounts.forEach((account: Account) => {
        if (account.username !== 'admin') {
          this.users.push(account);
        }
      });
    });
  }

  /* Check if Admin */

  isAdmin(): boolean {
    if (this.account?.username === 'admin') {
      return true;
    }
    return false;
  }

  /* Get Current Accoount */

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
