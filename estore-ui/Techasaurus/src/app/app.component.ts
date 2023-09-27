import { Component } from '@angular/core';
import { lastValueFrom, Observable } from 'rxjs';

import { AccountService } from './account.service';
import { Router } from '@angular/router';
import { Account } from './account';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Techasaurus';

  account?: Account;

  constructor(public accountService: AccountService, private router: Router) {
    this.getCurrentAccount();
  }

  isAdmin(): boolean {
    if (this.account) {
      return this.account?.username === "admin";
    }
    return false;
  }

  isLoggedIn(): boolean {
    if (this.account) {
      return this.account.isLoggedIn;
    }
    return false;
  }
  
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
    console.log("Checking if there is a session...");
    const hasSession = await this.checkIfSessions();
    console.log("Session check complete. Has session: " + hasSession);
    if (hasSession) {
      console.log("There is a session, getting current account!");
      try {
        const account = await lastValueFrom(this.accountService.getCurrentAccount());
        console.log("Current account: " + account);
        if (account) {
          this.account = account;
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
}
