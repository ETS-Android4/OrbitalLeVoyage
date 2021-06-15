package com.example.levoyage.ui.Accommodation;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.levoyage.DetailFragment;
import com.example.levoyage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccommodationDetailFragment extends DetailFragment<AccommodationItineraryItem> {

    private TextView nameView, descriptionView, linkView, priceView, addressView, extraView;
    private ImageView image;
    private FloatingActionButton detailFab;
    private AccommodationItineraryItem accommodation;
    private boolean saved;

    public AccommodationDetailFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            saved = getArguments().getBoolean("Saved");
        }
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameView = view.findViewById(R.id.detailName);
        addressView = view.findViewById(R.id.detailAddress);
        descriptionView = view.findViewById(R.id.detailDescription);
        priceView = view.findViewById(R.id.detailPrice);
        linkView = view.findViewById(R.id.detailLink);
        extraView = view.findViewById(R.id.detailBooking);
        image = view.findViewById(R.id.detailImage);
        detailFab = view.findViewById(R.id.detailfab);

        extraView.setVisibility(View.GONE);

        if (saved) {
            detailFab.hide();
            String dateString = getArguments().getString("Date");
            String location = getArguments().getString("Location");
            retrieveSavedInfo(dateString, location, AccommodationItineraryItem.class);
        } else {
            accommodation = getArguments().getParcelable("Accommodation");
            callDetailsAPI();
        }
    }

    private void callDetailsAPI() {
        String detailURL = "https://travel-advisor.p.rapidapi.com/hotels/get-details?location_id=" + accommodation.getId();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest searchHotel = new JsonObjectRequest(Request.Method.GET, detailURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONArray("data").getJSONObject(0);
                    accommodation.setAddress(getFromJson("address", data));
                    accommodation.setDescription(getFromJson("description", data));
                    accommodation.setLink(getFromJson("website", data));

                    setDetails(accommodation);
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, e -> Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show())
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> h = new HashMap<>();
                h.put("x-rapidapi-key", "445a09e84fmsh6d11b122cbebd2bp1bbc53jsnfed0b11069eb");
                h.put("x-rapidapi-host", "travel-advisor.p.rapidapi.com");
                return h;
            }
        };
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();
        searchHotel.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(searchHotel);
    }

    public void setDetails(AccommodationItineraryItem item) {
        nameView.setText(item.getLocation());
        addressView.setText(String.format("Address: %s", item.getAddress()));
        descriptionView.setText(item.getDescription());
        priceView.setText(String.format("Price Range (USD): %s", item.getPrice()));
        Picasso.get().load(item.getImageURL()).into(image);
        String link = item.getLink();

        linkView.setOnClickListener(v -> goToLink(link));
        detailFab.setOnClickListener(v -> addToItinerary(item));
    }

    private String getFromJson(String tag, JSONObject json) throws JSONException {
        if (json.isNull(tag)) {
            return "Not Available";
        } else if (json.getString(tag).isEmpty()) {
            return "Not Available";
        } else {
            return json.getString(tag);
        }
    }
}