import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MessageService } from './message.service';
import { ShoppingCart } from './shopping-cart';
import { Observable, catchError, tap } from 'rxjs';
import { log, handleError } from './utils/utils';
import { AccountService } from './account.service';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private cartUrl = 'http://localhost:8080/account/cart';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  
  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private accountService: AccountService) {
  }
  
  getCart(userid: number): Observable<ShoppingCart> {
    return this.http.get<ShoppingCart>(this.cartUrl + userid, this.httpOptions).pipe(
      tap(_ => log('fetched current cart')),
      catchError(handleError<ShoppingCart>('getCart'))
    );
  }
}
