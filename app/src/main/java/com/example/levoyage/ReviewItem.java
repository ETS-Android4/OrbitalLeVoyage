package com.example.levoyage;

public class ReviewItem {

    private String userID, review, date, locationID;
    private float rating;

    public ReviewItem(String userID, float rating, String review, String date, String locationID) {
        this.userID = userID;
        this.rating = rating;
        this.review = review;
        this.date = date;
        this.locationID = locationID;
    }

    public ReviewItem() {
    }

    public String getUserID() { return userID; }

    public void setUserID(String userID) { this.userID = userID; }

    public float getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }

    public String getReview() { return review; }

    public void setReview(String review) { this.review = review; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getLocationID() { return locationID; }

    public void setLocationID(String locationID) { this.locationID = locationID; }
}
