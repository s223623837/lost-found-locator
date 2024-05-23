package com.example.lostfoundlocator.models;

public class Advert {
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String location;
    private double latitude;
    private double longitude;

    // Constructor
    public Advert(String postType, String name, String phone, String description, String location, double latitude, double longitude) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        return "Post Type: " + postType + "\nName: " + name + "\nPhone: " + phone + "\nDescription: " + description + "\nLocation: " + location;
    }
}
