import { Color } from "./color";

export interface Product {
    id: number;
    name: string;
    price: number;
    quantity: number;
    productType: string;
    description: string;
    imageURL: string;
    color: Color[]; 
}