package com.example.levoyage.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.levoyage.R;

import java.util.Date;

public class WeatherAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private Date[] dates;
    private String[] temperature;
    private String[] weatherIcons;

    public WeatherAdapter(@NonNull Context context, String[] weatherIcons, Date[] dates, String[] temperature) {
        super(context, R.layout.weather_listview_item);
        this.dates = dates;
        this.temperature = temperature;
        this.mContext = context;
        this.weatherIcons = weatherIcons;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.weather_listview_item, parent, false);
            mViewHolder.mWeatherIcon = (ImageView) convertView.findViewById(R.id.weather_icon_lv);
            mViewHolder.temp = (TextView) convertView.findViewById(R.id.temperature_lv);
            mViewHolder.date = (TextView) convertView.findViewById(R.id.date_lv);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

//        int resourceID = getResources().getIdentifier(weatherIconsA[0], "drawable", getPackageName()); //TODO
//        mViewHolder.mWeatherIcon.setImageResource(resourceID);
        mViewHolder.temp.setText(temperature[position]);
        mViewHolder.date.setText(dates[position].toString());
        return convertView;
    }

    static class ViewHolder {
        ImageView mWeatherIcon;
        TextView temp, date;
    }
}
