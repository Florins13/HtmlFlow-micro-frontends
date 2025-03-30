package com.dev;


public class CartItemDto {
    private int id;
    private BikeDTO bike;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BikeDTO getBike() {
        return bike;
    }

    public void setBike(BikeDTO bike) {
        this.bike = bike;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}