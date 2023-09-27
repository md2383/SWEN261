import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';

@Component({
  selector: 'app-account-edit',
  templateUrl: './account-edit.component.html',
  styleUrls: ['./account-edit.component.css']
})
export class AccountEditComponent {
  passwordForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private accountService: AccountService, private router: Router) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.passwordForm = this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  account?: Account;

  update(): void {
    const oldPassword = this.passwordForm.get('oldPassword')?.value;
    const newPassword = this.passwordForm.get('newPassword')?.value;
    const confirmPassword = this.passwordForm.get('confirmPassword')?.value;

    if (this.account) {
      if (this.account.password === oldPassword) {
        if (newPassword === confirmPassword) {
          this.account.password = newPassword;
          this.accountService.updateAccount(this.account).subscribe(newAccount => {
            this.account = newAccount;
            this.router.navigate(['/account']).then(() => {
              window.location.reload();
            });
          });
        } else {
          alert("The new passwords do not match!");
        }
      } else {
        alert("The old password is incorrect!");
      }
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
