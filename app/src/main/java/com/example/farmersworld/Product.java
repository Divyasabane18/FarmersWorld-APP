package com.example.farmersworld;

public class Product {
    private String productId;  // Unique ID for the product
    private String name;
    private String description;
    private String imageUrl;
    private String price;
    private String city;

    public Product() {}

    public Product(String productId, String name, String description, String imageUrl, String price, String city) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.city = city;
    }

    // Getters and setters for productId, name, description, imageUrl, price, city
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
