import { Component, OnInit } from '@angular/core';

import { Observable, Subject } from 'rxjs';
import { Location } from '@angular/common';

import {
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.css']
})
export class ProductSearchComponent implements OnInit {
  products$!: Observable<Product[]>;
  private searchTerms = new Subject<string>();

  constructor(private productService: ProductService, private route: Router, private location: Location) {
    route.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.ngOnInit();
      }
    });
  }

  ngOnInit(): void {
    this.products$ = this.searchTerms.pipe(
      // wait 300ms after each keystroke before considering the term
      debounceTime(300),

      // ignore new term if same as previous term
      distinctUntilChanged(),

      // switch to new search observable each time the term changes
      switchMap((term: string) => this.productService.searchForProduct(term)),
    );
  }

  search(term: string): void {
    this.searchTerms.next(term);
  }

  goSearch(term: string | null): void {
    if (typeof term === 'string') {
      this.route.navigate([`search-results/${term}`]);
      this.route.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.route.navigate([`search-results/${term}`]);
      });
    }
  }

  getSearchTerm(): Observable<string> {
    return this.searchTerms.asObservable();
  }
}