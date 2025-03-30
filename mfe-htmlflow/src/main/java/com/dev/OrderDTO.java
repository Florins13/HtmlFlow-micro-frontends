package com.dev;

import java.util.List;

public class OrderDTO {
    private String transaction;
    private String orderState;
    private String acquireType;
    private List<ShippingItemDTO> shippingItems;
    private ShippingAddressDTO shippingAddress;
    private double totalPrice;
    private String username;

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getAcquireType() {
        return acquireType;
    }

    public void setAcquireType(String acquireType) {
        this.acquireType = acquireType;
    }

    public List<ShippingItemDTO> getShippingItems() {
        return shippingItems;
    }

    public void setShippingItems(List<ShippingItemDTO> shippingItems) {
        this.shippingItems = shippingItems;
    }

    public ShippingAddressDTO getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressDTO shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}