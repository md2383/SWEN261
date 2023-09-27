import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { Color } from '../color';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent {

  product: Product | undefined;
  colors: Color[] = [];
  selectedColors: Color[] = [];
  temp: Color[] = [];

  defaultColor: Color = {
    name: 'black',
  };

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getProduct();
    this.getAllAvailableColors();
  }

  getAllAvailableColors(): void {
    this.productService.getAllColors()
      .subscribe(colors => this.colors = colors);
  }

  getCurrentExistingColors(product: Product): void {
    this.productService.getColors(product.id).subscribe(temp => this.temp = temp);
  }

  setAllSelectedColors(product: Product): void {
    if (product) {
      if (this.checkColorDupes(product) === true) {
        alert('You have selected duplicate colors. Please remove the duplicate colors and try again.');
        return;
      } else {
        this.productService.setColors(product.id, this.selectedColors);
      }
    }
  }

  checkColorDupes(product: Product): boolean {
    let isDupe = false;
    for (let i = 0; i < this.selectedColors.length; i++) {
      for (let j = i + 1; j < this.temp.length; j++) {
        if (this.selectedColors[i].name === this.temp[j].name) {
          isDupe = true;
        }
      }
    }
    return isDupe;
  }

  toggleSelection(color: Color): void {
    console.log('toggleSelection called with color:', color);
    console.log('selectedColors before:', this.selectedColors);

    const index = this.selectedColors.indexOf(color);
    if (index >= 0) {
      // Color is already selected, remove it
      this.selectedColors.splice(index, 1);
    } else {
      // Color is not selected, add it if there are no duplicates
      if (this.colors.indexOf(color) >= 0) {
        const duplicates = this.selectedColors.filter(c => c.name === color.name);
        if (duplicates.length === 0) {
          this.selectedColors.push(color);
        }
      }
    }

    // Remove unselected colors from the selectedColors array
    this.selectedColors = this.selectedColors.filter(c => this.colors.indexOf(c) >= 0);

    console.log('selectedColors after:', this.selectedColors);
  }


  currentColors: string[] = [];

  // Function to get the currently selected colors
  getCurrentColors(): string[] {
    this.currentColors = [];
    for (let i = 0; i < this.selectedColors.length; i++) {
      this.currentColors.push(this.selectedColors[i].name);
    }
    return this.currentColors;
  }

  getProduct(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProductById(id)
      .subscribe(product => {
        if (product) {
          this.product = product;
          this.getAllSelectedColors(product);
        }
      });
  }

  isSelectedColor(color: Color): boolean {
    return this.selectedColors.some(c => c.name === color.name);
  }

  getAllSelectedColors(product: Product): void {
    console.log('getAllSelectedColors ' + product.id + 'current selected: ' + this.currentColors)
    if (product) {
      this.productService.getColors(product.id)
        .subscribe(selectedcolors => this.selectedColors = selectedcolors);
    }
  }

  getQuantity(): number {
    if (this.product) {
      return this.product.quantity;
    }
    return 0;
  }

  incrementQuantity(): void {
    if (this.product) {
      this.product.quantity++;
      this.productService.updateProduct(this.product)
    }
  }

  decrementQuantity(): void {
    if (this.product) {
      this.product.quantity--;
      this.productService.updateProduct(this.product)
    }
  }

  goBack(): void {
    this.location.back();
  }

  delete(product: Product): void {
    this.productService.deleteProduct(product.id).subscribe(() => this.goBack());
  }

  save(product: Product): void {
    if (this.product) {
      //this.saveColors(this.product);
      this.productService.updateProduct(product).subscribe(() => this.saveColors(product));
    }
    
  }

  saveColors(product: Product): void {
    console.log('saveColors called with product:', product);
    if (product) {
      console.log('saveColors2 called with product:', product);
      this.productService.setColors(product.id, this.selectedColors)
        .subscribe(selectedcolors => {
          console.log('saveColors3 called with product:', product);
          product.color = selectedcolors; // Update the product object with the new colors
          console.log('product.color called with product:', product.color.length);
          this.productService.setColors(product.id, this.selectedColors);
          this.selectedColors = [];
          this.goBack();
        });
        
    }
    
  }

}
