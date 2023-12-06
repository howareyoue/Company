package com.example.company;
public class Restaurant {
    private String name;
    private String address;
    private String review;

    public Restaurant() {}

    public Restaurant(String name, String address, String review) {
        this.name = name;
        this.address = address;
        this.review = review;
    }
    public String getName() { return name; }
    public String getAddress() {
        return address;
    }
    public String getReview() { return review; }
}