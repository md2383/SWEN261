import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})

export class ProductsComponent implements OnInit {

  selectedProduct?: Product;

  products: Product[] = [];
  price = 0;
  quantity = 0;
  productType: string = 'MOUSE';
  imageURL = '';

  constructor(
    private productService: ProductService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products);
  }

  add(name: string, price: number, quantity: number, productType : string, imageURL : string): void {
    const product = { name, price, quantity, productType, imageURL } as Product;
    if (!name) { return; }
    if (price <= 0) { return; }
    if (quantity < 0) { return; }
    this.productService.createProduct(product).subscribe(product => {
      this.products.push(product);
    });
    this.save();
    this.location.back();
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    this.productService.updateProduct(this.selectedProduct as Product)
      .subscribe();
  }

}