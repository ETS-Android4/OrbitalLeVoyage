package com.example.levoyage.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levoyage.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ItineraryAdapter class is an adapter for
 * recycler views used in the itinerary fragment.
 */
public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.MyViewHolder> {

    Context context;
    String userID;
    ArrayList<ItineraryItem> list;

    public ItineraryAdapter(Context context, ArrayList<ItineraryItem> list, String userID) {
        this.context = context;
        this.list = list;
        this.userID = userID;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView location, time;
        ConstraintLayout itemLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.itineraryLocation);
            time = itemView.findViewById(R.id.itineraryTime);
            itemLayout = itemView.findViewById(R.id.itineraryItemLayout);
        }
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itinerary_item, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItineraryItem item = list.get(position);
        holder.location.setText(item.getLocation());
        holder.time.setText(String.format("%s - %s", item.getStartTime().toString(), item.getEndTime().toString()));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Saved", true);
                bundle.putString("Date", item.getDate());
                bundle.putString("Location", item.getLocation());
                if (item.getType() == 1) {
                    Navigation.findNavController(v).navigate(
                            R.id.action_itineraryFragment_to_accommodationDetailFragment, bundle);
                } else if (item.getType() == 2) {
                    Navigation.findNavController(v).navigate(
                            R.id.action_itineraryFragment_to_attractionDetailFragment, bundle);
                } else if (item.getType() == 3) {
                    Navigation.findNavController(v).navigate(
                            R.id.action_itineraryFragment_to_foodDetailFragment, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
