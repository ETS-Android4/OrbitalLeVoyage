package com.example.levoyage.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.levoyage.R;
import com.example.levoyage.RecyclerItemTouchHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ItineraryFragment extends Fragment {

    private String date, userID;
    private RecyclerView recyclerView;
    private DatabaseReference database;
    private ItineraryAdapter adapter;
    private ArrayList<ItineraryItem> list;
    private TextView start, end;
    private EditText location;
    private FloatingActionButton fab;
    private Button btn;
    private ImageButton closeBtn;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get data passed from home fragment
        if (getArguments() != null) {
            date = getArguments().getString("DATE");
            userID = getArguments().getString("UserID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        database = FirebaseDatabase
                .getInstance(getString(R.string.database_link))
                .getReference(userID).child("Itinerary");
        fab = view.findViewById(R.id.fab);

        // Set header
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(String.format("Itinerary for: %s", date));

        // Fill up recycler view with data from database
        database.child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new ItineraryAdapter(getContext(), list, userID);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ItineraryItem itineraryItem = dataSnapshot.getValue(ItineraryItem.class);
                    list.add(itineraryItem);
                }
                list.sort((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        // Pop up for adding new event to itinerary
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = getLayoutInflater().inflate(R.layout.itinerary_popup, null);
                location = popupView.findViewById(R.id.popupEvent);
                start = popupView.findViewById(R.id.popupStart);
                end = popupView.findViewById(R.id.popupEnd);
                btn = popupView.findViewById(R.id.popupButton);
                closeBtn = popupView.findViewById(R.id.popupClose);
                TimeParcel st = new TimeParcel();
                TimeParcel et = new TimeParcel();

                start.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            st.setHr(hourOfDay);
                            st.setMin(minute);
                            start.setText(st.toString());
                            start.setError(null);
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                end.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            et.setHr(hourOfDay);
                            et.setMin(minute);
                            end.setText(et.toString());
                            end.setError(null);
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                dialogBuilder.setView(popupView);
                dialog = dialogBuilder.create();
                dialog.show();
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String loc = location.getText().toString();
                        if (loc.isEmpty()) {
                            location.setError("Please key in an event");
                            location.requestFocus();
                        } else if (start.getText().toString().isEmpty()) {
                            start.setError("Please select a start time");
                            start.requestFocus();
                        } else if (end.getText().toString().isEmpty()) {
                            end.setError("Please select an end time");
                            end.requestFocus();
                        } else if (et.compareTo(st) < 0) {
                            end.setError("End time is earlier than start time");
                            end.requestFocus();
                        } else {
                            ItineraryItem item = new ItineraryItem(loc, date, st, et);
                            database.child(date).child(loc).setValue(item);
                            dialog.dismiss();
                        }
                    }
                });

                closeBtn.setOnClickListener(x -> dialog.dismiss());
            }
        });

        ItemTouchHelper.SimpleCallback callback = new RecyclerItemTouchHelper(getContext()) {
            @Override
            public void deleteItem(int position) {
                dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle("Delete Event");
                dialogBuilder.setMessage("Are you sure you want to delete this event from your itinerary?");
                dialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ItineraryItem deleted = list.remove(position);
                        adapter.notifyItemRemoved(position);
                        database.child(date).child(deleted.getLocation()).removeValue();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel",
                        (dialog, which) -> adapter.notifyItemChanged(position));
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }

            @Override
            public void editItem(int position) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                View popupView = LayoutInflater.from(getContext()).inflate(R.layout.edit_itinerary_popup, null);
                TextView event = popupView.findViewById(R.id.editPopupEvent);
                TextView date = popupView.findViewById(R.id.editPopupDate);
                TextView start = popupView.findViewById(R.id.editPopupStart);
                TextView end = popupView.findViewById(R.id.editPopupEnd);
                Button updateBtn = popupView.findViewById(R.id.editPopupUpdate);
                ImageButton closeBtn = popupView.findViewById(R.id.editPopupClose);
                TimeParcel st = new TimeParcel();
                TimeParcel et = new TimeParcel();

                ItineraryItem item = list.get(position);
                event.setText(item.getLocation());
                date.setText(item.getDate());
                start.setText(item.getStartTime().toString());
                end.setText(item.getEndTime().toString());

                Map<String, Object> updates = new HashMap<>();

                dialogBuilder.setView(popupView);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                start.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            st.setHr(hourOfDay);
                            st.setMin(minute);
                            updates.put("startTime", st);
                            start.setText(st.toString());
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                end.setOnClickListener(t -> {
                    TimePickerDialog timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            et.setHr(hourOfDay);
                            et.setMin(minute);
                            updates.put("endTime", et);
                            end.setText(et.toString());
                        }
                    }, 0, 0, false);
                    timePicker.show();
                });

                date.setOnClickListener(t -> {
                    DatePickerDialog datePicker = new DatePickerDialog(getContext());
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

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et.compareTo(st) < 0) {
                            end.setError("End time is earlier than start time");
                            end.requestFocus();
                        } else if (updates.containsKey("date")) {
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
                                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                            } else {
                                                newReference.updateChildren(updates)
                                                        .addOnCompleteListener(t -> Toast.makeText(getContext(), "Itinerary updated", Toast.LENGTH_SHORT).show());
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            database.child(item.getDate()).child(item.getLocation()).updateChildren(updates);
                        }
                        dialog.dismiss();
                    }
                });

                closeBtn.setOnClickListener(t -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(position); });
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}