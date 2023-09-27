import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Product } from './product';
import { MessageService } from './message.service';
import { log, handleError } from './utils/utils';
import { Color } from './color';
import { Review } from './review';


@Injectable({ providedIn: 'root' })
export class ProductService {

  private productsUrl = 'http://localhost:8080/product';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService) { }

  /** GET products from the server */
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl)
      .pipe(
        tap(_ => log('fetched products')),
        catchError(handleError<Product[]>('getProducts', []))
      );
  }

  /** GET Product by id. Return `undefined` when id not found */
  getProductNo404<Data>(id: number): Observable<Product> {
    const url = `${this.productsUrl}/?id=${id}`;
    return this.http.get<Product[]>(url)
      .pipe(
        map(products => products[0]), // returns a {0|1} element array
        tap(h => {
          const outcome = h ? 'fetched' : 'did not find';
          log(`${outcome} product id=${id}`);
        }),
        catchError(handleError<Product>(`getProduct id=${id}`))
      );
  }

  /** GET Product by id. Will 404 if id not found */
  getProductById(id: number): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.get<Product>(url).pipe(
      tap(_ => log(`fetched product id=${id}`)),
      catchError(handleError<Product>(`getProduct id=${id}`))
    );
  }

  /* GET Productes whose name contains search term */
  searchForProduct(term: string): Observable<Product[]> {
    if (!term.trim()) {
      // if not search term, return empty Product array.
      return of([]);
    }
    return this.http.get<Product[]>(`${this.productsUrl}/?name=${term}`).pipe(
      tap(x => x.length ?
        log(`found products matching "${term}"`) :
        log(`no products matching "${term}"`)),
      catchError(handleError<Product[]>('searchForProducts', []))
    );
  }

  //////// Save methods //////////

  /** POST: add a new Product to the server */
  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.productsUrl, product, this.httpOptions).pipe(
      tap((newProduct: Product) => log(`added product w/ id=${newProduct.id}`)),
      catchError(handleError<Product>('createProduct'))
    );
  }

  /** DELETE: delete the product from the server */
  deleteProduct(id: number): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;

    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap(_ => log(`deleted product id=${id}`)),
      catchError(handleError<Product>('deleteProduct'))
    );
  }

  /** PUT: update the Product on the server */
  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(this.productsUrl, product, this.httpOptions).pipe(
      tap(_ => log(`updated product id=${product.id}`)),
      catchError(handleError<any>('updateProduct'))
    );
  }

  /** GET all colors from a product from the server */
  getColors(id: number): Observable<Color[]> {
    const url = `${this.productsUrl}/${id}/colors`;
    return this.http.get<Color[]>(url)
      .pipe(
        tap(_ => log('fetched colors')),
        catchError(handleError<Color[]>('getColors', []))
      );
  }

  setColors(id: number, colors: Color[]): Observable<Color[]> {
    const url = `${this.productsUrl}/${id}/colors`;
    return this.http.put<Color[]>(url, colors, this.httpOptions).pipe(
      tap(_ => log(`updated colors id=${id}`)),
      catchError(handleError<any>('updateColors'))
    );
  }

  getAllColors(): Observable<Color[]> {
    const url = `${this.productsUrl}/colors`;
    return this.http.get<Color[]>(url)
      .pipe(
        tap(_ => log('fetched colors')),
        catchError(handleError<Color[]>('getColors', []))
      );
  }

  createReview(review: Review): Observable<Review> {
    return this.http.post<Review>(this.productsUrl + '/review/create', review, this.httpOptions).pipe(
      catchError(handleError<Review>('createReview'))
    );
  }

  getReviewsByProductId(productid: number): Observable<Review[]> {
    return this.http.get<Review[]>(this.productsUrl + '/review/product/' + productid).pipe(
      tap(() => log('fetched reviews by product id' + productid)),
      catchError(handleError<Review[]>('getReviewsByProductId', []))
    );
  }

  getReviewsByUserId(userId: number): Observable<Review[]> {
    return this.http.get<Review[]>(this.productsUrl + '/review/user/' + userId).pipe(
      tap(() => log('fetched reviews')),
      catchError(handleError<Review[]>('getReviewsByUserId', []))
    );
  }

  deleteReview(review: Review): Observable<Review> {
    return this.http.delete<Review | ArrayBuffer>(this.productsUrl + '/review/delete', { ...this.httpOptions, body: review }).pipe(
      map(response => {

        console.log(response);

        // Check if the response is an ArrayBuffer
        if (response instanceof ArrayBuffer) {
          // Handle the ArrayBuffer and return a Review object
          // Replace this logic with your actual implementation
          const review: Review = {
            productid: 0,
            userid: 0,
            rating: 0,
            review: ''
          };
        return review;
        }
        // If the response is already a Review object, return it as is
        return response as Review;
      }),
      catchError(handleError<Review>('deleteReview'))
    );
  }

}