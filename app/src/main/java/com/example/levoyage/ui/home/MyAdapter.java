package com.example.levoyage.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;

import java.util.ArrayList;

/**
 * MyAdapter class is an adapter for
 * recycler views used in the itinerary fragment.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<ItineraryItem> list;

    public MyAdapter(Context context, ArrayList<ItineraryItem> list) {
        this.context = context;
        this.list = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView location, time;

        public MyViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.itineraryLocation);
            time = itemView.findViewById(R.id.itineraryTime);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_item, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItineraryItem item = list.get(position);
        holder.location.setText(item.getLocation());
        holder.time.setText(item.getStartTime().toString() + " - " + item.getEndTime().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
