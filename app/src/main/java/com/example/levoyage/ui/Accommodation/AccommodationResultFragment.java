package com.example.levoyage.ui.Accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.levoyage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccommodationResultFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Accommodation> list;
    AccommodationAdapter adapter;
    String adults, rooms, nights, locationID;

    public AccommodationResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationID = getArguments().getString("locationID");
            adults = getArguments().getString("adults");
            rooms = getArguments().getString("rooms");
            nights = getArguments().getString("nights");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accommodation_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.accommodationRecycler);

        if (list == null) {
            list = new ArrayList<>();
            extractHotels();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AccommodationAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

    }

    private String getPropertiesURL() {
        String propertiesURL = "https://travel-advisor.p.rapidapi.com/hotels/list?sort=recommended";
//        propertiesURL = propertiesURL + "&location_id=" + 293919; for testing without location search API call
        propertiesURL = propertiesURL + "&location_id=" + locationID;
        propertiesURL = propertiesURL + "&adults=" + adults;
        propertiesURL = propertiesURL + "&rooms=" + rooms;
        propertiesURL = propertiesURL + "&nights=" + nights;
        return propertiesURL;
    }

    private void extractHotels() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest searchHotels = new JsonObjectRequest(Request.Method.GET,
                getPropertiesURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray arr = response.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        if (i == 6 || i == 15 || i == 24) { // API call returns different data at these positions

                        } else {
                            JSONObject hotel = arr.getJSONObject(i);
                            Accommodation item = new Accommodation();
                            item.setName(hotel.getString("name"));
                            item.setRating(hotel.getString("rating"));
                            item.setAddress(hotel.getString("location_string"));
                            item.setPriceRange(hotel.getString("price"));
                            item.setId(hotel.getString("location_id"));
                            JSONObject image = hotel.getJSONObject("photo").getJSONObject("images").getJSONObject("medium");
                            item.setImageURL(image.getString("url"));
                            list.add(item);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new AccommodationAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());
                Toast.makeText(getContext(), "Error. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
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