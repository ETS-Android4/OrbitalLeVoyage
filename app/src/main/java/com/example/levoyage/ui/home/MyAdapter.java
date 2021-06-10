package com.example.levoyage.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.example.levoyage.ui.Accommodation.AccommodationItineraryItem;

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
        LinearLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.itineraryLocation);
            time = itemView.findViewById(R.id.itineraryTime);
            itemLayout = itemView.findViewById(R.id.itineraryItemLayout);
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

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Saved", true);
                bundle.putString("Date", item.getDate());
                Log.d("date", item.getDate());
                bundle.putString("Location", item.getLocation());
                if (item.getType() == 1) {
                    Navigation.findNavController(v).navigate(
                            R.id.action_itineraryFragment_to_accommodationDetailFragment, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
