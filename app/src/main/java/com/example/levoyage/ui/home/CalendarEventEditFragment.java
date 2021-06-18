package com.example.levoyage.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.levoyage.R;
import com.example.levoyage.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class CalendarEventEditFragment extends Fragment {

    private EditText tripName, tripLocation, tripDescription;
    private DatePickerDialog datePickerDialog;
    private Button startDateButton, endDateButton;
    private Button createNewTripBtn;

    private LocalDate startDate, endDate;

    public CalendarEventEditFragment() {
        // Empty constructor
    }

    public static CalendarEventEditFragment newInstance(String param1, String param2) {
        CalendarEventEditFragment fragment = new CalendarEventEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_event_edit, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tripName = view.findViewById(R.id.tripName);
        tripDescription = view.findViewById(R.id.tripDescription);
        tripLocation = view.findViewById(R.id.tripLocation);
        endDateButton = view.findViewById(R.id.EndDateButton);
        startDateButton = view.findViewById(R.id.StartDateButton);
        startDate = LocalDate.now();
        endDate = LocalDate.now();
        startDateButton.setText(String.format("Date: %s", formattedDate(startDate)));
        endDateButton.setText(String.format("Date: %s", formattedDate(endDate)));
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStartDatePicker();
            }
        });
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEndDatePicker();
            }
        });
        startDateButton.setText(getTodaysDate());
        endDateButton.setText(getTodaysDate());

        createNewTripBtn = view.findViewById(R.id.createNewTripButton);
        createNewTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewTripAction(v);
                Navigation.findNavController(v).navigate(R.id.action_nav_calendarEventEditFragment_to_home);
            }
        });
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initStartDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                month = month + 1;
                String date = makeDateString(day, month, year);
                startDate = LocalDate.of(year, month, day);
                startDateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
        datePickerDialog.show();
    }

    private void initEndDatePicker() {
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                endDate = LocalDate.of(year, month, day);
                endDateButton.setText(date);
            }
        });
        datePickerDialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";
        // default case: should not reach here
        return "SOMETHING WRONG";
    }

    public void saveNewTripAction(View view) {
        String tripName = this.tripName.getText().toString();
        CalendarTripEvent newTrip = new CalendarTripEvent(tripName, startDate, endDate);
        CalendarTripEvent.calendarTripEventArrayList.add(newTrip);
    }

    private String formattedDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        return selectedDate.format(formatter);
    }
}
