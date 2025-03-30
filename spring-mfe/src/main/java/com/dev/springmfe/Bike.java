package com.dev.springmfe;



import java.math.BigDecimal;

public class Bike {
    public Long id;
    public String model;
    public boolean electric;
    public String imageSource;
    public String details;
    public Integer stock;
    public BigDecimal price;
    public boolean isInStock;


    public Bike() {
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public boolean isElectric() {
        return electric;
    }

    public String getImageSource() {
        return imageSource;
    }

    public String getDetails() {
        return details;
    }

    public Integer getStock() {
        return stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isInStock() {
        return isInStock;
    }
}
