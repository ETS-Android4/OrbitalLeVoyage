package com.example.levoyage.ui.Attractions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.levoyage.R;
import com.example.levoyage.SearchFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttractionsFragment extends SearchFragment {

    private ArrayList<AttractionItineraryItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private AttractionsAdapter adapter;
    private EditText locationView;
    private ImageButton searchBtn;
    private RequestQueue queue;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.searchRecycler);
        locationView = view.findViewById(R.id.searchLocation);
        searchBtn = view.findViewById(R.id.searchButton);


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                queue = Volley.newRequestQueue(getContext());
                String location = locationView.getText().toString();
//                getLocationID(location, queue);
                extractInfo("298570");
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttractionsAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }

    public void extractInfo(String locationID) {
        String attractionsURL = "https://travel-advisor.p.rapidapi.com/attractions/list?sort=recommended&location_id=" + locationID;
        JsonObjectRequest searchAttractions = new JsonObjectRequest(Request.Method.GET,
                attractionsURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("data");
                    if (arr.length() == 0) {
                        Toast.makeText(getContext(), "No attractions found.", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < arr.length(); i++) {
                        if (i != 6 && i != 15 && i != 24) { // API call returns different data at these positions
                            JSONObject item = arr.getJSONObject(i);
                            AttractionItineraryItem attraction = new AttractionItineraryItem();
                            attraction.setId(getFromJson("location_id", item));
                            attraction.setLocation(getFromJson("name", item));
                            attraction.setRating(getFromJson("rating", item));
                            attraction.setAddress(getFromJson("address", item));
                            attraction.setLink(getURLFromJson("website", item));
                            attraction.setDescription(getFromJson("description", item));
                            attraction.setBookingURL(getBookingURLFromJson(item));
                            JSONObject image = item.getJSONObject("photo").getJSONObject("images").getJSONObject("medium");
                            attraction.setImageURL(image.getString("url"));
                            attraction.setCategory(getFromJsonArray("subcategory", "name", item));

                            list.add(attraction);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new AttractionsAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
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
        searchAttractions.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();
        queue.add(searchAttractions);
    }

    private String getBookingURLFromJson(JSONObject json) throws JSONException {
        if (json.isNull("booking")) {
            return null;
        } else {
            JSONObject booking = json.getJSONObject("booking");
            return getURLFromJson("url", booking);
        }
    }
}