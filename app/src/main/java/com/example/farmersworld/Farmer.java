package com.example.farmersworld;

public class Farmer {
    private String name, aadhar, state, city, email, mobileNumber; // 🔹 Added mobileNumber
    private boolean isVerified;
    private String uid; // 🔹 Add this

    public Farmer() {
        // Needed for Firebase
    }

    public Farmer(String name, String aadhar, String state, String city, String email, boolean isVerified, String mobileNumber) {
        this.name = name;
        this.aadhar = aadhar;
        this.state = state;
        this.city = city;
        this.email = email;
        this.isVerified = isVerified;
        this.mobileNumber = mobileNumber; // 🔹 Added mobileNumber
    }

    // 🔹 Getters
    public String getName() { return name; }
    public String getAadhar() { return aadhar; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getEmail() { return email; }
    public String getMobileNumber() { return mobileNumber; } // 🔹 Added getter for mobileNumber
    public boolean isVerified() { return isVerified; }
    public String getUid() { return uid; }

    // 🔹 Setters
    public void setName(String name) { this.name = name; }
    public void setAadhar(String aadhar) { this.aadhar = aadhar; }
    public void setState(String state) { this.state = state; }
    public void setCity(String city) { this.city = city; }
    public void setEmail(String email) { this.email = email; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; } // 🔹 Added setter for mobileNumber
    public void setVerified(boolean verified) { isVerified = verified; }
    public void setUid(String uid) { this.uid = uid; } // 🔥 The one causing the error
}
