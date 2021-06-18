package com.example.levoyage.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.levoyage.R;

import java.util.List;

public class CalendarEventAdapter extends ArrayAdapter<CalendarTripEvent> {

    public CalendarEventAdapter(@NonNull Context context, List<CalendarTripEvent> trips) {
        super(context, 0, trips);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CalendarTripEvent trip = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_event_cell, parent, false);

        TextView tripCellTV = convertView.findViewById(R.id.tripCellTV);

        String tripName = trip.getName();
        tripCellTV.setText(tripName);
        return convertView;
    }
}
