import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { AccountService } from '../account.service';
import { Account } from '../account';
import { Product } from '../product';
import { Payment } from '../payment';
import { Address } from '../address';
import {ShoppingCart} from '../shopping-cart';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  signUpForm!: FormGroup;
  products: Product[] = [];

  constructor(private formBuilder: FormBuilder, private router: Router, private http: HttpClient, private accountService: AccountService) { }

  ngOnInit(): void {
    this.signUpForm = this.formBuilder.group({
      username: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  }

  shoppingCart: ShoppingCart = { productsInCart: this.products, listofColors: [], productQuan: [] };
  address: Address = { city: '', street: '', state: '', houseNumber: 0, zip: 0 };
  payment: Payment = { cardNumber: '', cardHolder: '', expDate: '', cvv: 0 };

  signUp(): void {
    const account: Account = {
      id: 0,
      username: this.signUpForm.get('username')?.value,
      email: this.signUpForm.get('email')?.value,
      firstName: this.signUpForm.get('firstName')?.value,
      lastName: this.signUpForm.get('lastName')?.value,
      password: this.signUpForm.get('password')?.value,
      isLoggedIn: false,
      sessionID: 0,
      shoppingCart: this.shoppingCart,
      payment: this.payment,
      address: this.address,
      profilePicture: 'https://yt3.ggpht.com/a/AGF-l78BbxziKOEbYYrWTHTPnj_cNfEE6iNwbeQGWA=s900-c-k-c0xffffffff-no-rj-mo'
    };

    const confirmPassword = this.signUpForm.get('confirmPassword')?.value;

    if (account.password !== confirmPassword) {
      alert('Passwords do not match. Please try again.');
      return;
    } // check if the passwords match

    this.accountService.createAccount(account).subscribe({
      next: () => {
        this.signUpForm.reset();
        this.router.navigate(['/dashboard']).then(() => {
          window.location.reload();
        });
      },
      error: (err: any) => {
        alert('Error creating account. Please Try again.')
        console.log(err);
      }
    });
  }
}