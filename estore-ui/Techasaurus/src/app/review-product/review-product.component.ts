import { Component } from '@angular/core';
import { Observable, lastValueFrom } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { Review } from '../review';

import { Account } from '../account';
import { AccountService } from '../account.service';

import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-review-product',
  templateUrl: './review-product.component.html',
  styleUrls: ['./review-product.component.css']
})
export class ReviewProductComponent {
  reviewForm!: FormGroup;
  account?: Account;
  product?: Product;

  constructor(private router: Router, private formBuilder: FormBuilder, private accountService: AccountService, private productService: ProductService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.getCurrentAccount();
    this.getProduct();
    this.reviewForm = this.formBuilder.group({
      selectedValue: ['', Validators.required],
      review: ['', Validators.required],
    });
  }

  submitReview(): void {
    if (this.account && this.product) {
      const review: Review = {
        productid: this.product.id,
        userid: this.account.id,
        rating: this.reviewForm.value.selectedValue,
        review: this.reviewForm.value.review
      };

      console.log(review);

        this.productService.createReview(review).subscribe(() => {
          this.router.navigate(['detail/' + this.product?.id]).then(() => {
            this.reviewForm.reset();
            // window.location.reload();
          });
        });
    }
  }

  getProduct(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id)
      .subscribe(product => this.product = product);
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
