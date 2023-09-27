import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { MessagesComponent } from './messages/messages.component';
import { AppRoutingModule } from './app-routing.module';

import { DashboardComponent } from './dashboard/dashboard.component';

import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductSearchComponent } from './product-search/product-search.component';
import { ProductEditComponent } from './product-edit/product-edit.component';

import { LoginComponent } from './login/login.component';
import { AccountService } from './account.service';
import { SignUpComponent } from './sign-up/sign-up.component';
import { RouterModule } from '@angular/router';
import { SearchResultsComponent } from './search-results/search-results.component';
import { AccountComponent } from './account/account.component';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { PaymentComponent } from './payment/payment.component';
import { AddressComponent } from './address/address.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
import { ReviewProductComponent } from './review-product/review-product.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { UsersComponent } from './users/users.component';

@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    ProductDetailComponent,
    MessagesComponent,
    DashboardComponent,
    ProductSearchComponent,
    ProductEditComponent,
    LoginComponent,
    SignUpComponent,
    SearchResultsComponent,
    AccountComponent,
    AccountEditComponent,
    PaymentComponent,
    AddressComponent,
    OrderHistoryComponent,
    ReviewProductComponent,
    ShoppingCartComponent,
    UsersComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,
    RouterModule
  ],
  providers: [AccountService],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }