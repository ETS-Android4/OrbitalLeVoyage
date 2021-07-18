package com.example.levoyage.ui.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeatherData5Days {
    private String city;
    private String temperatureDay1, temperatureDay2, temperatureDay3, temperatureDay4, temperatureDay5;
    private String iconDay1, iconDay2, iconDay3, iconDay4, iconDay5;
    private String weatherTypeDay1, weatherTypeDay2, weatherTypeDay3, weatherTypeDay4, weatherTypeDay5;
    private int conditionDay1, conditionDay2, conditionDay3, conditionDay4, conditionDay5;
    private Date day1, day2, day3, day4, day5;

    public static WeatherData5Days fromJson(JSONObject jsonObject) {
        try {
            WeatherData5Days weatherD = new WeatherData5Days();
            weatherD.city = jsonObject.getString("name");
            weatherD.day1 = timestampToDate(jsonObject.getJSONArray("daily").getJSONObject(0).getLong("dt"));
            weatherD.conditionDay1 = jsonObject.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherTypeDay1 = jsonObject.getJSONArray("daily").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.iconDay1 = updateWeatherIcon(weatherD.conditionDay1);
            double temp = jsonObject.getJSONArray("daily").getJSONObject(0).getJSONObject("temp").getDouble("day") - 273.15;
            int roundedValue = (int) Math.rint(temp);
            weatherD.temperatureDay1 = Integer.toString(roundedValue);

            weatherD.day2 = timestampToDate(jsonObject.getJSONArray("daily").getJSONObject(1).getLong("dt"));
            weatherD.conditionDay2 = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherTypeDay2 = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.iconDay2 = updateWeatherIcon(weatherD.conditionDay2);
            temp = jsonObject.getJSONArray("daily").getJSONObject(1).getJSONObject("temp").getDouble("day") - 273.15;
            roundedValue = (int) Math.rint(temp);
            weatherD.temperatureDay2 = Integer.toString(roundedValue);

            weatherD.day3 = timestampToDate(jsonObject.getJSONArray("daily").getJSONObject(2).getLong("dt"));
            weatherD.conditionDay3 = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherTypeDay3 = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.iconDay3 = updateWeatherIcon(weatherD.conditionDay3);
            temp = jsonObject.getJSONArray("daily").getJSONObject(2).getJSONObject("temp").getDouble("day") - 273.15;
            roundedValue = (int) Math.rint(temp);
            weatherD.temperatureDay3 = Integer.toString(roundedValue);

            weatherD.day4 = timestampToDate(jsonObject.getJSONArray("daily").getJSONObject(3).getLong("dt"));
            weatherD.conditionDay4 = jsonObject.getJSONArray("daily").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherTypeDay4 = jsonObject.getJSONArray("daily").getJSONObject(3).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.iconDay4 = updateWeatherIcon(weatherD.conditionDay4);
            temp = jsonObject.getJSONArray("daily").getJSONObject(3).getJSONObject("temp").getDouble("day") - 273.15;
            roundedValue = (int) Math.rint(temp);
            weatherD.temperatureDay4 = Integer.toString(roundedValue);

            weatherD.day5 = timestampToDate(jsonObject.getJSONArray("daily").getJSONObject(4).getLong("dt"));
            weatherD.conditionDay5 = jsonObject.getJSONArray("daily").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherTypeDay5 = jsonObject.getJSONArray("daily").getJSONObject(4).getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.iconDay5 = updateWeatherIcon(weatherD.conditionDay5);
            temp = jsonObject.getJSONArray("daily").getJSONObject(4).getJSONObject("temp").getDouble("day") - 273.15;
            roundedValue = (int) Math.rint(temp);
            weatherD.temperatureDay5 = Integer.toString(roundedValue);

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

    private static Date timestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        return date;
    }

    public String getTemperatureDay1() {
        return temperatureDay1 + "°C";
    }

    public String getTemperatureDay2() {
        return temperatureDay2 + "°C";
    }

    public String getTemperatureDay3() {
        return temperatureDay3 + "°C";
    }

    public String getTemperatureDay4() { return temperatureDay4 + "°C"; }

    public String getTemperatureDay5() {
        return temperatureDay5 + "°C";
    }

    public String getIconDay1() {
        return iconDay1;
    }

    public String getIconDay2() {
        return iconDay2;
    }

    public String getIconDay3() {
        return iconDay3;
    }

    public String getIconDay4() {
        return iconDay4;
    }

    public String getIconDay5() {
        return iconDay5;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherTypeDay1() { return weatherTypeDay1; }

    public String getWeatherTypeDay2() {
        return weatherTypeDay2;
    }

    public String getWeatherTypeDay3() {
        return weatherTypeDay3;
    }

    public String getWeatherTypeDay4() {
        return weatherTypeDay4;
    }

    public String getWeatherTypeDay5() {
        return weatherTypeDay5;
    }

    public Date getDay1() { return day1; }

    public Date getDay2() { return day2; }

    public Date getDay3() { return day3; }

    public Date getDay4() { return day4; }

    public Date getDay5() { return day5; }
}

