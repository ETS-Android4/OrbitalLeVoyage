package com.example.levoyage.ui.Accommodation;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.levoyage.R;
import com.example.levoyage.ui.home.TimeParcel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AccommodationDetailFragment extends Fragment {

    private String hotelID, name, address, imageURL, description, price, link, dateString;
    private TextView nameView, descriptionView, linkView, priceView, addressView;
    private ImageView image;
    private FloatingActionButton detailFab;
    private AlertDialog dialog;
    private AlertDialog.Builder dialogBuilder;
    private DatabaseReference database;
    private TextView date, start, end, location;
    private Button btn;
    private ImageButton closeBtn;
    private boolean saved;

    public AccommodationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            saved = getArguments().getBoolean("Saved");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accommodation_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameView = view.findViewById(R.id.detailName);
        addressView = view.findViewById(R.id.detailAddress);
        descriptionView = view.findViewById(R.id.detailDescription);
        priceView = view.findViewById(R.id.detailPrice);
        linkView = view.findViewById(R.id.detailLink);
        image = view.findViewById(R.id.detailImage);
        detailFab = view.findViewById(R.id.detailfab);

        if (!saved) {
            hotelID = getArguments().getString("hotelID");
            callDetailsAPI();
        } else {
            retrieveSavedAccommodation();
        }

        detailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.detail_popup, null);
                location = popupView.findViewById(R.id.detailPopupLocation);
                date = popupView.findViewById(R.id.detailPopupDate);
                start = popupView.findViewById(R.id.detailPopupStart);
                end = popupView.findViewById(R.id.detailPopupEnd);
                btn = popupView.findViewById(R.id.detailPopupAdd);
                closeBtn = popupView.findViewById(R.id.detailPopupClose);
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database = FirebaseDatabase
                        .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference(userID).child("Itinerary");
                AccommodationItineraryItem item = new AccommodationItineraryItem();

                location.setText(name);
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
                            dateString = localDate.format(formatter);
                            date.setText(dateString);
                            database = database.child(dateString);
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
                        item.setDescription(description);
                        item.setAddress(address);
                        item.setPrice(price);
                        item.setLink(link);
                        item.setImageURL(imageURL);
                        item.setLocation(name);
                        database.child(name).setValue(item);
                        dialog.dismiss();
                    }
                });

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void retrieveSavedAccommodation() {

        dateString = getArguments().getString("Date");
        name = getArguments().getString("Location");
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(userID).child("Itinerary").child(dateString).child(name);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AccommodationItineraryItem accommodation = dataSnapshot.getValue(AccommodationItineraryItem.class);
                address = accommodation.getAddress();
                description = accommodation.getDescription();
                price = accommodation.getPrice();
                link = accommodation.getLink();
                imageURL = accommodation.getImageURL();

                nameView.setText(name);
                addressView.setText("Address: " + address);
                descriptionView.setText(description);
                priceView.setText("Price Range (USD): " + price);
                linkView.setText("Website: " + link);
                Picasso.get().load(imageURL).into(image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "No data saved", Toast.LENGTH_SHORT).show();
            }
        });

        detailFab.hide();

    }

    private void callDetailsAPI() {
        String detailURL = "https://travel-advisor.p.rapidapi.com/hotels/get-details?location_id=" + hotelID;
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest searchHotels = new JsonObjectRequest(Request.Method.GET, detailURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONArray("data").getJSONObject(0);
                    name = data.getString("name");
                    address = data.getString("address");
                    description = data.getString("description");
                    price = data.getString("price");
                    link = data.getString("website");

                    nameView.setText(name);
                    addressView.setText("Address: " + address);
                    descriptionView.setText(description);
                    priceView.setText("Price Range (USD): " + price);
                    linkView.setText("Website: " + link);
                    JSONObject imageJson = data.getJSONObject("photo").getJSONObject("images").getJSONObject("large");
                    imageURL = imageJson.getString("url");
                    Picasso.get().load(imageURL).into(image);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());
                Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {//here before semicolon ; and use { }.
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> h = new HashMap<>();
                h.put("x-rapidapi-key", "445a09e84fmsh6d11b122cbebd2bp1bbc53jsnfed0b11069eb");
                h.put("x-rapidapi-host", "travel-advisor.p.rapidapi.com");
                return h;
            }
        };
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();
        queue.add(searchHotels);
    }
}