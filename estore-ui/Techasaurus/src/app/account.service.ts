import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Account } from './account';
import { MessageService } from './message.service';
import { log, handleError } from './utils/utils';

import { Payment } from './payment';
import { Address } from './address';

import { Color } from './color';
import { Product } from './product';
import { ShoppingCart } from './shopping-cart';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private accountsUrl = 'http://localhost:8080/account';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {
    const currentAccount = localStorage.getItem('currentAccount');
    if (currentAccount) {
      this.account = JSON.parse(currentAccount);
    }
    console.log("Current account sessionID: " + this.account?.sessionID)
  }

  account?: Account;

  loginAccount(username?: string, password?: string): Observable<Account> {
    return this.http.get<Account>(this.accountsUrl + '/login?username=' + username + '&password=' + password, this.httpOptions).pipe(
      tap(account => {
        localStorage.setItem('currentAccount', JSON.stringify(account));
        log(`logged in account w/ username=${username}`);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error(error);
        return of({ isLoggedIn: false } as Account);
      })
    );
  }

  logoutAccount(username?: string): Observable<Account> {
    console.log("Logging out of the account with the username: " + username)
    return this.http.get<Account>(`${this.accountsUrl}/logout?username=${username}`).pipe(
      catchError(handleError<Account>('logoutAccount'))
    );
  }

  /**
   * Gets the current account from the server. This is the account that is currently logged in.
   * 
   * @returns the current account
   */
  getCurrentAccount(): Observable<Account> {
    const localAccount = localStorage.getItem('currentAccount');
    if (!localAccount) {
      return of({ isLoggedIn: false } as Account);
    }
    const account = JSON.parse(localAccount);
    console.log(account);
    console.log("Getting the session id:" + account.sessionID);
    return this.http.get<Account>(this.accountsUrl + '/current/' + account.sessionID, this.httpOptions).pipe(
      tap(_ => log('fetched current account')),
      catchError(handleError<Account>('getCurrentAccount'))
    );
  }

  getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.accountsUrl, this.httpOptions)
      .pipe(
        tap(_ => log('fetched accounts')),
        catchError(handleError<Account[]>('getAccounts', []))
      );
  }

  getAccountById(id: number): Observable<Account> {
    return this.http.get<Account>(this.accountsUrl + '/' + id, this.httpOptions).pipe(
      tap(_ => log(`fetched account id=${id}`)),
      catchError(handleError<Account>(`getAccountById id=${id}`))
    );
  }

  createAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(this.accountsUrl + '/create', account, this.httpOptions).pipe(
      tap((account: Account) => {
        localStorage.setItem('currentAccount', JSON.stringify(account));
        log(`created account w/ username=${account.username}`);
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 200 && error.error instanceof Blob && error.headers.get('Content-Type')?.startsWith('text/html')) {
          // Handle the case where the response is an HTML page
          console.error('Error: response is not valid JSON');
          return of({ isLoggedIn: false } as Account);
        } else {
          // Handle other errors
          console.error(error);
          return of({ isLoggedIn: false } as Account);
        }
      })
    );
  }

  updateAccount(account: Account): Observable<Account> {
    return this.http.post<Account>(this.accountsUrl + '/update', account, this.httpOptions).pipe(
      tap((account: Account) => {
        localStorage.setItem('currentAccount', JSON.stringify(account));
        log(`updated account w/ id=${account.id}`);
      }),
      catchError(handleError<Account>('updateAccount'))
    );
  }

  deleteAccount(id: number): Observable<Account> {
    return this.http.delete<Account>(this.accountsUrl + '/' + id, this.httpOptions).pipe(
      tap(_ => {
        localStorage.removeItem('currentAccount');
        log(`deleted account w/ id=${id}`);
      }),
      catchError(handleError<Account>('deleteAccount'))
    );
  }

  updatePayment(userid: number, payment: Payment): Observable<Payment> {
    return this.http.post<Payment>(this.accountsUrl + '/' + userid + '/payment/', payment, this.httpOptions).pipe(
      tap(_ => console.log('updated payment')),
      catchError(handleError<Payment>('updatePayment'))
    );
  }
  

  updateAddress(userid: number, address: Address): Observable<Address> {
    return this.http.post<Address>(this.accountsUrl + '/' + userid + '/address/', address, this.httpOptions).pipe(
      tap(_ => console.log('updated address')),
      catchError(handleError<Address>('updateAddress'))
    );
  }

  getOrderHistory(userid: number): Observable<Product[]> {
    return this.http.get<Product[]>(this.accountsUrl + '/order-history/' + userid, this.httpOptions).pipe(
      tap(_ => console.log('fetched order history')),
      catchError(handleError<Product[]>('getOrderHistory', []))
    );
  }

  getShoppingCart(userid: number): Observable<ShoppingCart> {
    return this.http.get<ShoppingCart>(this.accountsUrl + '/cart/' + userid, this.httpOptions).pipe(
      tap(_ => console.log('fetched shopping cart')),
      catchError(handleError<ShoppingCart>('getShoppingCart'))
    );
  }

  /* Quantity Functions */

  getQuantity(userid: number): Observable<number[]> {
    return this.http.get<number[]>(this.accountsUrl + '/order-history/' + userid + '/quantity', this.httpOptions).pipe(
      tap(_ => console.log('fetched quantity')),
      catchError(handleError<number[]>('getQuantity'))
    );
  }

  incrementQuantity(userid: number, index: number): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/increment/' + index, this.httpOptions).pipe(
      tap(_ => console.log('incremented quantity')),
      catchError(handleError<ShoppingCart>('incrementQuantity'))
    );
  }

  decrementQuantity(userid: number, index: number): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/decrement/' + index, this.httpOptions).pipe(
      tap(_ => console.log('incremented quantity')),
      catchError(handleError<ShoppingCart>('incrementQuantity'))
    );
  }

  /* Checkout */

  placeOrder(userid: number): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/clear', this.httpOptions).pipe(
      tap(_ => console.log('placed order')),
      catchError(handleError<ShoppingCart>('placeOrder'))
    );
  }


  /* Add To Shopping Cart */

  addProductToShoppingCart(userid: number, product: Product): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/add/product', product, this.httpOptions).pipe(
      tap(_ => console.log('added product to shopping cart')),
      catchError(handleError<ShoppingCart>('addProductToShoppingCart'))
    );
  }

  addColorToShoppingCart(userid: number, color: Color): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/add/color', color, this.httpOptions).pipe(
      tap(_ => console.log('added color to shopping cart')),
      catchError(handleError<ShoppingCart>('addColorToShoppingCart'))
    );
  }

  /* Remove From Shopping Cart */

  removeProductFromShoppingCart(userid: number, index: number): Observable<ShoppingCart> {
    return this.http.post<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/remove/' + index, this.httpOptions).pipe(
      tap(_ => console.log('removed from shopping cart')),
      catchError(handleError<ShoppingCart>('removeProductFromShoppingCart'))
    );
  }

  updateShoppingCart(userid: number, cart: ShoppingCart): Observable<ShoppingCart> {
    return this.http.put<ShoppingCart>(this.accountsUrl + '/cart/' + userid + '/update', cart, this.httpOptions).pipe(
      tap(_ => console.log('updated shopping cart')),
      catchError(handleError<ShoppingCart>('updateShoppingCart'))
    );
  }

}

