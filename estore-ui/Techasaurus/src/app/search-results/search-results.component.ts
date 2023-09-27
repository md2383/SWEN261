import { Component } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrls: ['./search-results.component.css']
})
export class SearchResultsComponent {
  products: Product[] = [];
  topProduct: Product[] = [];
  recentProducts: Product[] = [];
  selectedProduct?: Product;
  latestProduct?: Product;
  featuredProducts: Product[] = [];

  products$: Observable<Product[]> = new Observable<Product[]>(); 
  termString: string = '';

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
    ) { }

  ngOnInit(): void {
    const term = this.route.snapshot.paramMap.get('term');
    this.termString = term?.toString() ?? '';
    if(term != null){ 
      this.products$ = this.productService.searchForProduct(term);
    }
    this.getProducts();
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  getFilteredProducts(string: String): Product[] {
    return this.products.filter(p => p.productType === string);
  }

  getAllProducts(): Product[] {
    return this.products;
  }

  getProducts(): void {
    this.products$.subscribe(products => this.products = products.slice(0));
  }





}
