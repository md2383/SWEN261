import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  products: Product[] = [];
  recentProducts: Product[] = [];
  selectedProduct?: Product;
  slideIndex = 1;


  constructor(
    private productService: ProductService, private router: Router
  ) { }

  ngOnInit(): void {
    this.getProducts();
    this.getLatestProducts();
    this.slideIndex = 1;
    this.showSlides(this.slideIndex);
    this.timeToNextSlide();
  }

  onSelect(product: Product): void {
    this.selectedProduct = product;
  }

  getFilteredProducts(string: String): Product[] {
    return this.products.filter(p => p.productType === string);
  }

  timeToNextSlide(): void {
    if (this.router.url === '/dashboard') {
      setTimeout(() => {
        this.plusSlides(1);
        this.timeToNextSlide();
      }, 5000);
    }
  }

  plusSlides(n: number): void {
    this.showSlides(this.slideIndex += n);
  }

  currentSlide(n: number): void {
    this.showSlides(this.slideIndex = n);
  }

  showSlides(n: number): void {
    let i: number;
    const slides = document.getElementsByClassName('slide-card');
    const dots = document.getElementsByClassName('dot');

    if (n > slides.length) {
      this.slideIndex = 1;
    }
    if (n < 1) {
      this.slideIndex = slides.length;
    }

    for (i = 0; i < slides.length; i++) {
      slides[i].setAttribute('style', 'display:none');
    }
    /* set dot as active from the current slide its one */
    for (i = 0; i < dots.length; i++) {
      dots[i].className = dots[i].className.replace(' active', '');
    }
    slides[this.slideIndex - 1].setAttribute('style', 'display:block');
    dots[this.slideIndex - 1].className += ' active';
  }

  /* Individual Products */

  getProducts(): void {
    this.productService.getProducts().subscribe(products => { this.products = products; });
  }

  getLatestProducts(): void {
    this.productService.getProducts().subscribe(products => {
      //add 3 latest products to recentProducts array
      this.recentProducts = products.slice(products.length - 3);
      console.log('recentProducts', this.recentProducts);
    });
  }
}
