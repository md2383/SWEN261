import { Payment } from './payment';
import { ShoppingCart } from './shopping-cart';
import { Address } from './address';

export interface Account {
    id: number;
    username: string;
    email: string;
    firstName: string;
    lastName: string;
    password: string;
    isLoggedIn: boolean;
    sessionID: number;
    payment: Payment;
    shoppingCart: ShoppingCart;
    address: Address;
    profilePicture: string;
}