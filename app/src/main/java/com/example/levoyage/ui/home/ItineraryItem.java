package com.example.levoyage.ui.home;

public class ItineraryItem {

    String location;
    TimeParcel startTime, endTime;

    public ItineraryItem(String location, TimeParcel startTime, TimeParcel endTime) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ItineraryItem() {}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TimeParcel getStartTime() {
        return startTime;
    }

    public void setStartTime(TimeParcel startTime) {
        this.startTime = startTime;
    }

    public TimeParcel getEndTime() {
        return endTime;
    }

    public void setEndTime(TimeParcel endTime) {
        this.endTime = endTime;
    }
}
