import { Product } from "./product";
import { Color } from "./color";

export interface ShoppingCart {
    productsInCart: Product[];
    listofColors: Color[];
    productQuan: number[];
}