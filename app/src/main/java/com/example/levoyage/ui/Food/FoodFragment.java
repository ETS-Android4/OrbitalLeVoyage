package com.example.levoyage.ui.Food;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
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

public class FoodFragment extends SearchFragment {

    private ArrayList<FoodItineraryItem> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private EditText locationView;
    private ImageButton searchBtn;
    private RequestQueue queue;

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
        adapter = new FoodAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void extractInfo(String locationID) {
        String foodURL = "https://travel-advisor.p.rapidapi.com/restaurants/list?currency=USD&location_id=" + locationID;
        JsonObjectRequest searchFood = new JsonObjectRequest(Request.Method.GET,
                foodURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("data");
                    if (arr.length() == 0) {
                        Toast.makeText(getContext(), "No restaurants found.", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < arr.length(); i++) {
                        if (i != 4 && i != 11 && i != 18) { // API call returns different data at these positions
                            JSONObject item = arr.getJSONObject(i);
                            FoodItineraryItem restaurant = new FoodItineraryItem();
                            restaurant.setId(getFromJson("location_id", item));
                            restaurant.setLocation(getFromJson("name", item));
                            restaurant.setRating(getFromJson("rating", item));
                            restaurant.setAddress(getFromJson("address", item));
                            restaurant.setLink(getURLFromJson("website", item));
                            restaurant.setPrice(getFromJson("price_level", item));
                            restaurant.setDescription(getFromJson("description", item));
                            JSONObject image = item.getJSONObject("photo").getJSONObject("images").getJSONObject("medium");
                            restaurant.setImageURL(image.getString("url"));
                            restaurant.setCategory(getFromJsonArray("cuisine", "name", item));

                            list.add(restaurant);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new FoodAdapter(getContext(), list);
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
        searchFood.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Toast.makeText(getContext(), "Loading...", Toast.LENGTH_LONG).show();
        queue.add(searchFood);
    }
}