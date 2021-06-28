package com.example.levoyage.ui.food;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.levoyage.ui.home.ItineraryItem;

public class FoodItineraryItem extends ItineraryItem implements Parcelable {

    private String description, link, imageURL, address, category, rating, price, id;

    public FoodItineraryItem() {
        super(3);
    }

    protected FoodItineraryItem(Parcel in) {
        description = in.readString();
        link = in.readString();
        imageURL = in.readString();
        address = in.readString();
        category = in.readString();
        rating = in.readString();
        price = in.readString();
        id = in.readString();
    }

    public static final Creator<FoodItineraryItem> CREATOR = new Creator<FoodItineraryItem>() {
        @Override
        public FoodItineraryItem createFromParcel(Parcel in) {
            return new FoodItineraryItem(in);
        }

        @Override
        public FoodItineraryItem[] newArray(int size) {
            return new FoodItineraryItem[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(imageURL);
        dest.writeString(address);
        dest.writeString(category);
        dest.writeString(rating);
        dest.writeString(price);
        dest.writeString(id);
    }
}
