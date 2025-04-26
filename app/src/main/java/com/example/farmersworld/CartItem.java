package com.example.farmersworld;

public class CartItem {
    private String name;
    private String price;
    private int quantity;
    private String imageUrl;

    // Default constructor for Firebase
    public CartItem() {}

    // Constructor with parameters
    public CartItem(String name, String price, int quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Constructor with 3 parameters (without imageUrl)
    public CartItem(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = "";  // Set imageUrl to an empty string or a default value
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Override toString() for easier logging
    @Override
    public String toString() {
        return "CartItem{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
