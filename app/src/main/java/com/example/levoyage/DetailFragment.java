package com.example.levoyage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.ui.home.ItineraryAdapter;
import com.example.levoyage.ui.home.ItineraryItem;
import com.example.levoyage.ui.home.TimeParcel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class DetailFragment<T extends ItineraryItem> extends Fragment {

    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private TextView date, start, end, location, reviewLocation, ratingBarBG;
    private Button itineraryBtn, reviewBtn;
    private ImageButton itineraryCloseBtn, reviewCloseBtn;
    private EditText reviewText;
    private RatingBar ratingBar;
    private ArrayList<ReviewItem> list;
    private ReviewAdapter adapter;

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
        itineraryBtn = popupView.findViewById(R.id.detailPopupAdd);
        itineraryCloseBtn = popupView.findViewById(R.id.detailPopupClose);
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
                    start.setError(null);
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
                    end.setError(null);
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
                    date.setError(null);
                }
            });
            datePicker.show();
        });

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
        itineraryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (date.getText().toString().isEmpty()) {
                    date.setError("Please select a date");
                    date.requestFocus();
                } else if (start.getText().toString().isEmpty()) {
                    start.setError("Please select a start time");
                    start.requestFocus();
                } else if (end.getText().toString().isEmpty()) {
                    end.setError("Please select an end time");
                    end.requestFocus();
                } else {
                    database.child(item.getDate()).child(item.getLocation()).setValue(item);
                    dialog.dismiss();
                }
            }
        });

        itineraryCloseBtn.setOnClickListener(v -> dialog.dismiss());
    }

    public void addReview(String location, String locationID) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        View popupView = getLayoutInflater().inflate(R.layout.review_popup, null);
        reviewLocation = popupView.findViewById(R.id.addReviewLocation);
        reviewText = popupView.findViewById(R.id.addReviewText);
        ratingBar = popupView.findViewById(R.id.addRatingBar);
        reviewBtn = popupView.findViewById(R.id.addReviewBtn);
        reviewCloseBtn = popupView.findViewById(R.id.addReviewClose);
        ratingBarBG = popupView.findViewById(R.id.ratingBarBG);

        reviewLocation.setText(location);
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating > 0.0) {
                    ratingBarBG.setError(null);
                } else {
                    ratingBarBG.setError("Please select a rating");
                    ratingBarBG.requestFocus();
                }
            }
        });

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewString = reviewText.getText().toString();
                float rating = ratingBar.getRating();
                if (rating == 0.0) {
                    ratingBarBG.setError("Please select a rating");
                    ratingBarBG.requestFocus();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String userID = user.getUid();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
                    String date = LocalDate.now().format(formatter);
                    ReviewItem review = new ReviewItem(user.getDisplayName(), rating, reviewString, date, locationID);
                    DatabaseReference database = FirebaseDatabase
                            .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("Reviews").child(locationID).child(userID);
                    database.setValue(review);
                    dialog.dismiss();
                }
            }
        });
        reviewCloseBtn.setOnClickListener(v -> dialog.dismiss());
    }

    public void retrieveReviews(String locationID, RecyclerView recyclerView) {
        DatabaseReference database = FirebaseDatabase
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Reviews").child(locationID);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ReviewAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReviewItem reviewItem = dataSnapshot.getValue(ReviewItem.class);
                    list.add(reviewItem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
