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

        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                View popupView = LayoutInflater.from(context).inflate(R.layout.edit_itinerary_popup, null);
                TextView event = popupView.findViewById(R.id.editPopupEvent);
                TextView date = popupView.findViewById(R.id.editPopupDate);
                TextView start = popupView.findViewById(R.id.editPopupStart);
                TextView end = popupView.findViewById(R.id.editPopupEnd);
                Button deleteBtn = popupView.findViewById(R.id.editPopupDelete);
                Button updateBtn = popupView.findViewById(R.id.editPopupUpdate);
                ImageButton closeBtn = popupView.findViewById(R.id.editPopupClose);

                event.setText(item.getLocation());
                date.setText(item.getDate());
                start.setText(item.getStartTime().toString());
                end.setText(item.getEndTime().toString());

                Map<String, Object> updates = new HashMap<>();
                DatabaseReference database = FirebaseDatabase
                        .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                        .getReference(userID).child("Itinerary");

                dialogBuilder.setView(popupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                start.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            TimeParcel st = new TimeParcel(hourOfDay, minute);
                            updates.put("startTime", st);
                            start.setText(st.toString());
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                end.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            TimeParcel et = new TimeParcel(hourOfDay, minute);
                            updates.put("endTime", et);
                            end.setText(et.toString());
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                date.setOnClickListener(t -> {
                    DatePickerDialog datePicker = new DatePickerDialog(context);
                    datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            LocalDate localDate = LocalDate.of(year, month + 1, dayOfMonth);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
                            String dateString = localDate.format(formatter);
                            date.setText(dateString);
                            updates.put("date", dateString);
                        }
                    });
                    datePicker.show();
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.child(item.getDate()).child(item.getLocation()).removeValue()
                                .addOnCompleteListener(task -> Toast.makeText(context,
                                        "Removed from itinerary", Toast.LENGTH_SHORT).show());
                        dialog.dismiss();
                    }
                });

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (updates.containsKey("date")) {
                            String newDate = (String) updates.get("date");
                            DatabaseReference oldReference = database.child(item.getDate()).child(item.getLocation());
                            DatabaseReference newReference = database.child(newDate).child(item.getLocation());
                            updates.put("date", newDate);
                            oldReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                    oldReference.removeValue();
                                    newReference.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError firebaseError, @NonNull @NotNull DatabaseReference firebase) {
                                            if (firebaseError != null) {
                                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                            } else {
                                                newReference.updateChildren(updates)
                                                        .addOnCompleteListener(t -> Toast.makeText(context, "Itinerary updated", Toast.LENGTH_SHORT).show());
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            database.child(item.getDate()).child(item.getLocation()).updateChildren(updates);
                        }
                        dialog.dismiss();
                    }
                });

                closeBtn.setOnClickListener(x -> dialog.dismiss());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
