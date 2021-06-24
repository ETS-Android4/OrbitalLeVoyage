package com.example.levoyage.ui.home;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.levoyage.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler);
        database = FirebaseDatabase
                .getInstance("https://orbital-le-voyage-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference(userID).child("Itinerary").child(date);
        fab = view.findViewById(R.id.fab);

        // Set header
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(String.format("Itinerary for: %s", date));

        // Fill up recycler view with data from database
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
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
            public void onCancelled(@NotNull DatabaseError error) {
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
                        } else {
                            ItineraryItem item = new ItineraryItem(loc, date, st, et);
                            database.child(loc).setValue(item);
                            dialog.dismiss();
                        }
                    }
                });

                closeBtn.setOnClickListener(x -> dialog.dismiss());
            }
        });
    }
}