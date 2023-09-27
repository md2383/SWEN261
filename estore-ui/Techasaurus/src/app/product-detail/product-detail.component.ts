import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';

import { Observable, lastValueFrom } from 'rxjs';
import { Account } from '../account';
import { AccountService } from '../account.service';
import { Router } from '@angular/router';
import { Color } from '../color';
import { Review } from '../review';
import { ShoppingCart } from '../shopping-cart';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product?: Product;
  account?: Account;
  colors: Color[] = [];
  reviews: Review[] = [];
  accounts: Account[] = [];
  selectedColor: Color = {name: '' };
  shoppingCart?: ShoppingCart;
  reviewsLoaded: boolean = false;

  anonymAccount: Account = {
    id: 0,
    username: 'Anonymous',
    password: 'wahfencpaioafuffpowmDIceawouDISOu wiopQUDWNPOCEUWQP8ONT93R2R0NE9Wad',
    email: 'oqiwdoi',
    firstName: 'anonym',
    lastName: 'anonym',
    isLoggedIn: false,
    sessionID: 0,
    address: { city: '', street: '', state: '', houseNumber: 0, zip: 0 },
    shoppingCart: { productsInCart: [], listofColors: [], productQuan: [] },
    payment: { cardNumber: '', cardHolder: '', expDate: '', cvv: 0 },
    profilePicture: 'https://yt3.ggpht.com/a/AGF-l78BbxziKOEbYYrWTHTPnj_cNfEE6iNwbeQGWA=s900-c-k-c0xffffffff-no-rj-mo'
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private accountService: AccountService,
    private router: Router
  ) { }

  async ngOnInit() {
    this.getCurrentAccount(); 
    const id = Number(this.route.snapshot.paramMap.get('id'));
    await this.getProduct(id);
    if (this.product && this.reviewsLoaded === false) {
      this.loadReviews(this.product.id);
    }
  }

  onColorChange(color: any): void {
    this.selectedColor.name = color.value;
  }

  async getProduct(id: number): Promise<void> {
    try {
      const product = await lastValueFrom(this.productService.getProductById(id));
      if (product) {
        this.product = product;
        this.getAllSelectedColors();
      }
    } catch (error) {
      console.error(error);
    }
  }

  getQuantity(): number {
    if (this.product) {
      return this.product.quantity;
    }
    return 0;
  }

  hasStock(): boolean {
    if (this.product) {
      return this.product.quantity > 0;
    }
    return false;
  }

  isAdmin(): boolean {
    if (this.account) {
      return this.account.id == 1;
    }
    return false;
  }

  getAllSelectedColors(): void {
    console.log('getAllSelectedColors ' + this.product?.id)
    if (this.product) {
      this.productService.getColors(this.product.id)
        .subscribe(selectedcolors => this.colors = selectedcolors);
    }
  }

  addToCart(): void {
    if (this.selectedColor.name == '') {
      alert("Please select a color");
    } else {
      if (!this.account) {
        alert('You must be logged in!');
      } else if (this.product && this.account.username != 'admin') {
        this.getProduct(this.product.id);
        this.accountService.addProductToShoppingCart(this.account.id, this.product).subscribe(shoppingCart => this.shoppingCart = shoppingCart);
        this.accountService.addColorToShoppingCart(this.account.id, this.selectedColor).subscribe(shoppingCart => this.shoppingCart = shoppingCart);
        this.save();
      } else {
        alert('You cannot add to cart as admin!');
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  delete(): void {
    if (this.product) {
      this.productService.deleteProduct(this.product.id).subscribe(() => this.goBack());
    }
  }

  save(): void {
    if (this.product) {
      this.productService.updateProduct(this.product)
        .subscribe(() => this.goBack());
    }
  }

  /* Review Functions */

  loadReviews(productid: number): void {
    console.log('loadReviews ' + this.product?.id);
    if (this.product) {
        this.productService.getReviewsByProductId(productid).subscribe((reviews: Review[]) => {
          this.reviews = reviews;
          this.loadAccounts(this.reviews);
        });
        this.reviewsLoaded = true;
    }
  }   

  loadAccounts(reviews: Review[]): void {
    console.log('load accounts called');
    console.log('Reviews loaded ' + reviews.length + ' reviews ');
    console.log(reviews);
    for (let i = 0; i < reviews.length; i++) {
      try {
        this.getAccountFromReview(reviews[i]);
      } catch (error) {
        
        this.accounts.push(this.anonymAccount);
        }
    }
    console.log(this.accounts);
  }

  getAccountFromReview(review: Review): void {
    console.log('getAccountFromReview called ' + review.userid);
    const id = review.userid;
    this.accountService.getAccountById(id).subscribe((account: Account) => {
      console.log(account);
      this.accounts.push(account);
    });
  } 

  averageRating(): number {
    let sum = 0;
    for (let i = 0; i < this.reviews.length; i++) {
      sum += this.reviews[i].rating;
    }
    let average = sum / this.reviews.length
    let fixedAverage = average.toFixed(1);
    return Number(fixedAverage);
  }

  removeReview(review: Review): void {
    console.log('removeReview called ' + review.userid + ": " + review.productid);
    this.productService.deleteReview(review);
    this.reviewsLoaded = false;
    this.loadReviews(review.productid);
    alert('removed');
  }

  getAccountFromReviewId(review: Review): Account {
    
    for (let i = 0; i < this.accounts.length; i++) {
      if (this.accounts[i].id == review.userid) {
        return this.accounts[i];
      }
    }

    const fakeaccount: Account = {
      id: 0,
      username: 'anonymous',
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      isLoggedIn: false,
      sessionID: 0,
      shoppingCart: { productsInCart: [], listofColors: [], productQuan: [] },
      payment: { cardNumber: '', cardHolder: '', expDate: '', cvv: 0},
      address: { city: '', street: '', state: '', houseNumber: 0, zip: 0 },
      profilePicture: 'https://yt3.ggpht.com/a/AGF-l78BbxziKOEbYYrWTHTPnj_cNfEE6iNwbeQGWA=s900-c-k-c0xffffffff-no-rj-mo'
    };

    return fakeaccount;
  }


  /* Account Information Functions */

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
          localStorage.setItem('account', JSON.stringify(account));
        }
      } catch (error) {
        console.error(error);
      }
    }
  }
  
}