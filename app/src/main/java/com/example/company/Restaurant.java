package com.example.company;

import android.text.style.IconMarginSpan;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String address;
    private String review;
    private String company;

    private String createdByUserId;

    public Restaurant() {}

    public Restaurant(String name, String address, String review, String company) {
        this.name = name;
        this.address = address;
        this.review = review;
        this.company = company;
    }
    public String getName() { return name; }
    public String getAddress() {
        return address;
    }
    public String getReview() { return review; }
    public String getCompany() {return company;}

    public void setCompany(String company) {
        this.company = company;
    }
}