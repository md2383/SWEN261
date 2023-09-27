import { Component } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AccountService } from '../account.service';
import { Payment } from '../payment';
import { Account } from '../account';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent {
  paymentForm!: FormGroup;
  account?: Account;
  payment?: Payment;

  constructor(private formBuilder: FormBuilder, private router: Router, private http: HttpClient, private accountService: AccountService) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.paymentForm = this.formBuilder.group({
      cardNumber: ['', Validators.required],
      cardHolder: ['', Validators.required],
      expirationMonthDate: ['', Validators.required],
      expirationYearDate: ['', Validators.required],
      cvv: ['', Validators.required],
    });
  }

  check(): boolean {
    if (this.account?.payment.cardNumber === "" || this.account?.payment.cardHolder === "" || this.account?.payment.expDate === "" || this.account?.payment.cvv === 0) {
      return true;
    }
    return false;
  }

  updatePayment(): void {
    // create date
    const expirationMonthDate = this.paymentForm.get('expirationMonthDate')?.value;
    const expirationYearDate = this.paymentForm.get('expirationYearDate')?.value;
    const date = expirationMonthDate + "/" + expirationYearDate;

    // create payment
    const payment: Payment = {
      cardNumber: this.paymentForm.get('cardNumber')?.value,
      cardHolder: this.paymentForm.get('cardHolder')?.value,
      expDate: this.paymentForm.get('expirationMonthDate')?.value + "/" + this.paymentForm.get('expirationYearDate')?.value,
      cvv: this.paymentForm.get('cvv')?.value
    };

    if (payment.cardNumber.length != 16) {
      alert("Card number must be 16 digits");
      return;
    }

    if (payment.cvv < 99 || payment.cvv > 1000) {
      alert("CVV must be 3 digits");
      return;
    }
    
    this.payment = payment;

    if (this.account) {
      this.accountService.updatePayment(this.account.id, payment).subscribe({
        next: () => {
          this.paymentForm.reset();
          window.location.reload();
        },
        error: (err) => {
          console.log(err);
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

  deletePayment(): void {
    const payment: Payment = {
      cardNumber: '',
      cardHolder: '',
      expDate: '',
      cvv: 0
    };

    this.payment = payment;
    console.log(this.payment);

    if (this.account) {
      this.accountService.updatePayment(this.account.id, payment).subscribe({
        next: () => {
          this.paymentForm.reset();
          window.location.reload();
        },
        error: (err) => {
          console.log(err);
        }
      });
    }
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
