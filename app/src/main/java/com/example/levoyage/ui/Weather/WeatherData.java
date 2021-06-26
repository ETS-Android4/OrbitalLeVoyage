package com.example.levoyage.ui.Weather;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    private String temperature, icon, city, weatherType;
    private int condition;

    public static WeatherData fromJson(JSONObject jsonObject) {
        try {
            WeatherData weatherD = new WeatherData();
            weatherD.city = jsonObject.getString("name");
            weatherD.condition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.icon = updateWeatherIcon(weatherD.condition);
            double temp = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(temp);
            weatherD.temperature = Integer.toString(roundedValue);
            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition) {
        if (condition >= 0 && condition <= 300 || condition >= 900 && condition <= 902 || condition >= 905 && condition <= 1000) {
            return "weather_thunder";
        } else if (condition >= 300 && condition <= 500) {
            return "weather_light_rain";
        } else if (condition >= 500 && condition <= 600) {
            return "weather_heavy_rain";
        } else if (condition >= 600 && condition <= 700) {
            return "weather_snow";
        } else if (condition >= 701 && condition <= 771) {
            return "weather_fog";
        } else if (condition >= 772 && condition < 800 || condition >= 801 && condition <= 804) {
            return "weather_cloudy";
        } else if (condition == 800 || condition == 904) {
            return "weather_sunny";
        } else if (condition == 903) {
            return "weather_snow";
        } else {
            return "No matching weather";
        }
    }

    public String getTemperature() {
        return temperature + "°C";
    }

    public String getIcon() {
        return icon;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherType() {
        return weatherType;
    }
}