package com.example.levoyage.ui.Accommodation;

public class Accommodation {

    private String name, address, rating, priceRange, id, imageURL;

    public Accommodation(String name, String address, String rating, String priceRange, String id, String imageURL) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.priceRange = priceRange;
        this.id = id;
        this.imageURL = imageURL;
    }

    public Accommodation() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPriceRange() { return priceRange; }

    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
