package com.example.quickcheck;

public class User {
    private String email;
    private String name;
    private String degree;
    private String unit;

    // Required default constructor for Firebase
    public User() {}

    public User(String email, String name, String degree, String unit) {
        this.email = email;
        this.name = name;
        this.degree = degree;
        this.unit = unit;
    }

    // Getters and setters for the class variables
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

