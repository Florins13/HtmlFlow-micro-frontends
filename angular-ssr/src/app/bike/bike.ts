interface Cart {
    cartItems: CartItem[];
    rentTotal: number;
    cartTotal: number;
    cartIsEmpty: boolean;
  }
  
  interface CartItem {
    id: number;
    bike: Bike;
    quantity: number;
  }
  
export interface Bike {
    imageSource: string;
    details: string;
    stock: number;
    price: number;
    id: number;
    model: string;
    electric: boolean;
    inStock: boolean;
  }
  