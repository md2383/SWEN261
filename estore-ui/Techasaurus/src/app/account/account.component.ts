import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account.service';
import { lastValueFrom, Observable } from 'rxjs';
import { Router } from '@angular/router';

import { Account } from '../account';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  constructor(private accountService: AccountService, private router: Router) {}

  account?: Account;
  newProfile?: string;
  showProfilePictureInput = false;

  ngOnInit(): void {
    this.getCurrentAccount();
  }

  isAdmin(): boolean {
    if (this.account?.username === 'admin') {
      return true;
    }
    return false;
  }

  logout(): void {
    if (this.account) {
      this.accountService.logoutAccount(this.account.username).subscribe(account => {
        this.account = undefined;
        localStorage.removeItem('account');
        this.router.navigate(['/dashboard']).then(() => {
          window.location.reload();
        });
      });
    }
  }

  getNewProfilePicture(): string {
    if (this.account) {
      if (this.newProfile) {
        return this.newProfile;
      }
      return this.account.profilePicture;
    }
    return '';
  }

  delete(): void {
    if (this.account) {
      if (this.account?.username === 'admin') {
        alert('Cannot delete admin account!');
        return;
      }
      this.accountService.deleteAccount(this.account.id).subscribe(account => {
        this.account = undefined;
        localStorage.removeItem('account');
        this.router.navigate(['/dashboard']).then(() => {
          window.location.reload();
        });
      });
    }
  }

  public toggleProfilePictureInput(): void {
    this.showProfilePictureInput = !this.showProfilePictureInput;
  }

  public changeProfilePicture(): void {
    if (this.account) {
      this.account.profilePicture = this.newProfile!;
      this.accountService.updateAccount(this.account).subscribe(account => {
        this.account = account;
        this.getCurrentAccount();
        // Log out the user and prompt them to sign back in
        this.accountService.logoutAccount(this.account.username).subscribe(account => {
          alert('Your profile picture has been updated. Please sign in again.');
        });
      });
    }
    this.router.navigate(['/login']).then(() => {
      window.location.reload();
    });    
  }


  update(): void {
    if (this.account) { 
      this.router.navigate(['/account-edit']); // route to account-edit component
    }
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
