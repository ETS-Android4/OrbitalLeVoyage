package com.example.levoyage.ui.home;

public class ItineraryItem implements Comparable<ItineraryItem> {

    String location, date;
    TimeParcel startTime, endTime;
    int type;

    public ItineraryItem(String location, String date, TimeParcel startTime, TimeParcel endTime) {
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = 0;
    }

    public ItineraryItem(int type) {
        this.type = type;
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

    public int getType() { return type; }

    public void setType(int type) { this.type = type; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    @Override
    public int compareTo(ItineraryItem o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
