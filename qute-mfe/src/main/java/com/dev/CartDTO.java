package com.dev;

import java.util.List;

public class CartDTO {
    private List<CartItemDto> cartItems;
    private double cartTotal;
    private boolean cartIsEmpty;

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }

    public boolean isCartIsEmpty() {
        return cartIsEmpty;
    }

    public void setCartIsEmpty(boolean cartIsEmpty) {
        this.cartIsEmpty = cartIsEmpty;
    }
}