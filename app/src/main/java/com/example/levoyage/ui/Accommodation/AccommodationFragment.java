package com.example.levoyage.ui.Accommodation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.levoyage.R;
import com.example.levoyage.databinding.FragmentAccommodationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AccommodationFragment extends Fragment {

    private AccommodationViewModel galleryViewModel;
    private FragmentAccommodationBinding binding;
    private EditText locationText, adultsText, roomsText, nightsText;
    private Button searchBtn;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(AccommodationViewModel.class);

        binding = FragmentAccommodationBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationText = view.findViewById(R.id.hotelSearchLocation);
        adultsText = view.findViewById(R.id.hotelAdults);
        roomsText = view.findViewById(R.id.hotelRooms);
        nightsText = view.findViewById(R.id.hotelNights);
        searchBtn = view.findViewById(R.id.hotelSearchBtn);
        Bundle bundle = new Bundle();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationText.getText().toString();
                String adults = adultsText.getText().toString();
                String rooms = roomsText.getText().toString();
                String nights = nightsText.getText().toString();
                bundle.putString("adults", adults);
                bundle.putString("rooms", rooms);
                bundle.putString("nights", nights);
//                Navigation.findNavController(view).navigate(
//                        R.id.action_nav_accommodation_to_accommodationResultFragment, bundle);

                RequestQueue queue = Volley.newRequestQueue(getContext());
                JsonObjectRequest searchLocation = new JsonObjectRequest(Request.Method.GET, searchURl(location), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arr = response.getJSONArray("data");
                            JSONObject result = arr.getJSONObject(0);
                            String locationID = result.getJSONObject("result_object").getString("location_id");
                            bundle.putString("locationID", locationID);
                            Navigation.findNavController(view).navigate(
                                    R.id.action_nav_accommodation_to_accommodationResultFragment, bundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", error.getMessage());
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
                queue.add(searchLocation);
            }
        });
    }


    private String searchURl(String location) {
        try {
            return "https://travel-advisor.p.rapidapi.com/locations/search?query=" + URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}