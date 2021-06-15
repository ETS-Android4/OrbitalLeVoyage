package com.example.levoyage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.levoyage.ui.home.ItineraryItem;
import com.example.levoyage.ui.home.TimeParcel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class DetailFragment<T extends ItineraryItem> extends Fragment {

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView date, start, end, location;
    private Button btn;
    private ImageButton closeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void goToLink(String link) {
        if (link == null) {
            Toast.makeText(getContext(), "Not Available", Toast.LENGTH_SHORT).show();
        } else {
            Uri webaddress = Uri.parse(link);
            Intent goToLink = new Intent(Intent.ACTION_VIEW, webaddress);
            startActivity(goToLink);
        }
    }

    public void retrieveSavedInfo(String dateString, String location, Class<T> tClass) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(userID).child("Itinerary").child(dateString).child(location);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                T item = dataSnapshot.getValue(tClass);
                setDetails(item);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "No data saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public abstract void setDetails(T item);

    public void addToItinerary(T item) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        View popupView = getLayoutInflater().inflate(R.layout.detail_popup, null);
        location = popupView.findViewById(R.id.detailPopupLocation);
        date = popupView.findViewById(R.id.detailPopupDate);
        start = popupView.findViewById(R.id.detailPopupStart);
        end = popupView.findViewById(R.id.detailPopupEnd);
        btn = popupView.findViewById(R.id.detailPopupAdd);
        closeBtn = popupView.findViewById(R.id.detailPopupClose);
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(userID).child("Itinerary");

        location.setText(item.getLocation());
        start.setOnClickListener(t -> {
            TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TimeParcel st = new TimeParcel(hourOfDay, minute);
                    item.setStartTime(st);
                    start.setText(st.toString());
                }
            }, 0, 0, false);
            timePicker.show();
        });

        end.setOnClickListener(t -> {
            TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TimeParcel et = new TimeParcel(hourOfDay, minute);
                    item.setEndTime(et);
                    end.setText(et.toString());
                }
            }, 0, 0, false);
            timePicker.show();
        });

        date.setOnClickListener(t -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext());
            datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
                    String dateString = localDate.format(formatter);
                    date.setText(dateString);
                    item.setDate(dateString);
                }
            });
            datePicker.show();
        });

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(item.getDate()).child(item.getLocation()).setValue(item);
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(v -> dialog.dismiss());
    }
}
