import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ProductEditComponent } from './product-edit/product-edit.component';
import { LoginComponent } from './login/login.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { AccountComponent } from './account/account.component';
import { SearchResultsComponent } from './search-results/search-results.component';
import { AccountEditComponent } from './account-edit/account-edit.component';
import { PaymentComponent } from './payment/payment.component';
import { AddressComponent } from './address/address.component';
import { OrderHistoryComponent } from './order-history/order-history.component';
import { ReviewProductComponent } from './review-product/review-product.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { UsersComponent } from './users/users.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:id', component: ProductDetailComponent },
  { path: 'products', component: ProductsComponent},
  { path: 'edit/:id', component: ProductEditComponent },
  { path: 'login', component: LoginComponent },
  { path: 'sign-up', component: SignUpComponent },
  { path: 'account', component: AccountComponent },
  { path: 'search-results/:term', component: SearchResultsComponent },
  { path: 'shopping-cart', component: ShoppingCartComponent },
  { path: 'account-edit', component: AccountEditComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'address', component: AddressComponent },
  { path: 'order-history', component: OrderHistoryComponent },
  { path: 'review-product/:id', component: ReviewProductComponent },
  { path: 'users', component: UsersComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }