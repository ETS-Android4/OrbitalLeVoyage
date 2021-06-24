package com.example.levoyage.ui.Accommodation;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

    private TextView nameView, descriptionView, linkView, priceView, addressView, extraView, review;
    private ImageView image;
    private FloatingActionButton detailFab;
    private Button addReviewBtn;
    private RecyclerView reviewsRecycler;
    private ProgressBar progressBar;
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
        addReviewBtn = view.findViewById(R.id.addReviewOpenBtn);
        reviewsRecycler = view.findViewById(R.id.detailReviews);
        review = view.findViewById(R.id.review);
        progressBar = view.findViewById(R.id.detailProgressBar);

        extraView.setVisibility(View.GONE);

        if (saved) {
            detailFab.hide();
            String dateString = getArguments().getString("Date");
            String location = getArguments().getString("Location");
            retrieveSavedInfo(dateString, location, AccommodationItineraryItem.class);
        } else {
            accommodation = getArguments().getParcelable("Accommodation");
            review.setVisibility(View.INVISIBLE);
            addReviewBtn.setVisibility(View.INVISIBLE);
            linkView.setVisibility(View.INVISIBLE);
            callDetailsAPI();
        }
    }

    private void callDetailsAPI() {
        String detailURL = "https://travel-advisor.p.rapidapi.com/hotels/get-details?location_id=" + accommodation.getId();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest searchHotel = new JsonObjectRequest(Request.Method.GET, detailURL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONArray("data").getJSONObject(0);
                    accommodation.setAddress(getFromJson("address", data));
                    accommodation.setDescription(getFromJson("description", data));
                    accommodation.setLink(getURLFromJson("website", data));
                    review.setVisibility(View.VISIBLE);
                    addReviewBtn.setVisibility(View.VISIBLE);
                    linkView.setVisibility(View.VISIBLE);
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
        searchHotel.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(searchHotel);
    }

    public void setDetails(AccommodationItineraryItem item) {
        nameView.setText(item.getLocation());
        addressView.setText(String.format("Address: %s", item.getAddress()));
        descriptionView.setText(item.getDescription());
        priceView.setText(String.format("Price Range (USD): %s", item.getPrice()));
        Picasso.get().load(item.getImageURL()).placeholder(R.drawable.error_placeholder_large).fit().into(image);
        String link = item.getLink();
        retrieveReviews(item.getId(), reviewsRecycler);
        progressBar.setVisibility(ProgressBar.GONE);

        linkView.setOnClickListener(v -> goToLink(link));
        detailFab.setOnClickListener(v -> addToItinerary(item));
        addReviewBtn.setOnClickListener(v -> addReview(item.getLocation(), item.getId()));
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

    private String getURLFromJson(String tag, JSONObject json) throws JSONException {
        if (json.isNull(tag)) {
            return null;
        } else if (json.getString(tag).isEmpty()) {
            return null;
        } else {
            return json.getString(tag);
        }
    }

}