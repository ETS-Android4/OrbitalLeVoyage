package com.example.levoyage.ui.home;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarTripEvent {
    public static ArrayList<CalendarTripEvent> calendarTripEventArrayList = new ArrayList<>();

    public static ArrayList<CalendarTripEvent> tripsBeforeDate(LocalDate date) {
        ArrayList<CalendarTripEvent> trips = new ArrayList<>();

        for(CalendarTripEvent trip : calendarTripEventArrayList) {
            if(!trip.getEndDate().isBefore(date))
                trips.add(trip);
        }

        return trips;
    }

    private String name;
    private LocalDate startDate, endDate;

    public CalendarTripEvent(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
