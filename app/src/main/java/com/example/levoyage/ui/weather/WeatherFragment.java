package com.example.levoyage.ui.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.android.volley.Header;
import com.example.levoyage.R;
import com.google.firebase.database.annotations.NotNull;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class WeatherFragment extends Fragment {

    final String API = "798183b24b356e657baaff7ec8bfc4c6";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER;

    TextView cityName, weatherState, temperature;
    ImageView weatherIcon;
    ProgressBar progressBar;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    Button cityFinder;

    LocationManager locationManager;
    LocationListener locationListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weatherState = view.findViewById(R.id.weather_condition);
        temperature = view.findViewById(R.id.temperature);
        weatherIcon = view.findViewById(R.id.weather_icon);
        cityFinder = view.findViewById(R.id.changeCityButton);
        cityName = view.findViewById(R.id.city_name);
        progressBar = view.findViewById(R.id.weatherProgressBar);

        getWeatherForCurrentLocation();

        cityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.city_search_popup, null);
                dialogBuilder.setView(popupView);

                ImageButton closeButton = popupView.findViewById(R.id.cityCloseButton);
                EditText changeCity = popupView.findViewById(R.id.changed_city_name);
                Button changeButton = popupView.findViewById(R.id.changeButton);

                dialog = dialogBuilder.create();
                dialog.show();

                changeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newCity = changeCity.getText().toString();
                        getWeatherForNewCity(newCity);
                        dialog.dismiss();
                    }
                });

                closeButton.setOnClickListener(t -> dialog.dismiss());
            }
        });
    }

    private void getWeatherForNewCity(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", API);
        doNetworking(params);
    }

    @SuppressLint("MissingPermission")
    private void getWeatherForCurrentLocation() {
        weatherState.setVisibility(View.INVISIBLE);
        temperature.setVisibility(View.INVISIBLE);
        weatherIcon.setVisibility(View.INVISIBLE);
        cityName.setVisibility(View.INVISIBLE);
        cityFinder.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                RequestParams params = new RequestParams();
                params.put("lat", latitude);
                params.put("lon", longitude);
                params.put("appid", API);
                doNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                // unable to get location
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, locationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Location Get Successfully", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void doNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                WeatherData weatherD = WeatherData.fromJson(response);
                updateUI(weatherD);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void updateUI(WeatherData weatherData) {
        temperature.setText(weatherData.getTemperature());
        cityName.setText(weatherData.getCity());
        weatherState.setText(weatherData.getWeatherType());
        int resourceID = getResources().getIdentifier(weatherData.getIcon(), "drawable", getActivity().getPackageName());
        weatherIcon.setImageResource(resourceID);

        temperature.setVisibility(View.VISIBLE);
        cityFinder.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
        weatherState.setVisibility(View.VISIBLE);
        weatherIcon.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }
}
