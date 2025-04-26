package com.example.farmersworld;

public class Customer {
    private String name, city, email;

    public Customer() {
        // Required for Firebase
    }

    public Customer(String name, String city, String email) {
        this.name = name;
        this.city = city;
        this.email = email;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setEmail(String email) { this.email = email; }
}
