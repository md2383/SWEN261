import { Component } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Address } from '../address';

import { AccountService } from '../account.service';
import { Account } from '../account';

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent {
  addressForm!: FormGroup;
  address?: Address;
  account?: Account;

  constructor(private formBuilder: FormBuilder, private router: Router, private http: HttpClient, private accountService: AccountService) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.addressForm = this.formBuilder.group({
      city: ['', Validators.required],
      street: ['', Validators.required],
      state: ['', Validators.required],
      houseNumber: ['', Validators.required],
      zip: ['', Validators.required]
    });
  }

  check(): boolean {
    if (this.account?.address.city === '' || this.account?.address.street === '' || this.account?.address.state === '' || this.account?.address.houseNumber === 0 || this.account?.address.zip === 0) {
      return true;
    }
    return false;
  }

  deleteAddress(): void {
    const address: Address = {
      city: '',
      street: '',
      state: '',
      houseNumber: 0,
      zip: 0
    };

    this.address = address;
    console.log(address);

    if (this.account) {
      this.accountService.updateAddress(this.account.id, address).subscribe({
        next: () => {
          this.addressForm.reset();
          window.location.reload();
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
   }

  updateAddress() {
    const address: Address = {
      city: this.addressForm.get('city')?.value,
      street: this.addressForm.get('street')?.value,
      state: this.addressForm.get('state')?.value,
      houseNumber: this.addressForm.get('houseNumber')?.value,
      zip: this.addressForm.get('zip')?.value
    };

    if (address.zip >= 100000) {
      alert("Zip code must be 5 digits");
      return;
    }

    this.address = address;

    if (this.account) {
      this.accountService.updateAddress(this.account.id, address).subscribe({
        next: () => {
          this.addressForm.reset();
          window.location.reload();
        },
        error: (err) => {
          console.error(err);
        }
      });
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
