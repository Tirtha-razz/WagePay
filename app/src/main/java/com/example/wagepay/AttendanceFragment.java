package com.example.wagepay;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AttendanceFragment extends Fragment {
    ArrayList<AttendanceRecyclerModel> arrAttendance = new ArrayList<>();
    AttendanceRecyclerAdapter adapter;

    private ImageView calender;
    private TextView day;
    private TextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        //for today's day
        calender = view.findViewById(R.id.calender);
        date = view.findViewById(R.id.date);
        day = view.findViewById(R.id.day);

        // Set today's date and day of the week
        updateTodayDate();

        // Set click listener for the calender icon
        calender.setOnClickListener(v -> openDatePicker());

        //for today date finish here


        //for recycler view of worker list
        RecyclerView recyclerView = view.findViewById(R.id.attendance_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //clearing array
        arrAttendance.clear();

        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Sudip Baral"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Eden Hazard"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Neymar JR"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Enzo Farnandez"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Recee James"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Levi Cowill"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Ben Chilwell"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Armando Broja"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Noni Madueke"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Christopher Nkunku"));
        arrAttendance.add(new AttendanceRecyclerModel(R.drawable.profile_pic,"Mykhaylo Mudryk"));

        adapter = new AttendanceRecyclerAdapter(this ,arrAttendance);
        recyclerView.setAdapter(adapter);

        // Set the OnSaveClickListener for the adapter
        adapter.setOnSaveClickListener(() -> {
            // Handle the "Save" button click here
            // This will be triggered when the "Save" button is clicked in the RecyclerView
        });

        return view;

    }


    private void updateTodayDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        String todayDate = dateFormat.format(calendar.getTime());
        String todayDay = dayFormat.format(calendar.getTime());

        date.setText(todayDate);
        day.setText(todayDay);
    }

    private void openDatePicker() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                R.style.DatePickerDialogTheme,
                (view, yearSelected, monthOfYear, dayOfMonthSelected) -> {
                    // When the date is selected, this callback is called
                    // You can handle the selected date here
                    String selectedDate = formatDate(yearSelected, monthOfYear, dayOfMonthSelected);
                    String selectedDay = getDayOfWeek(yearSelected, monthOfYear, dayOfMonthSelected);

                    date.setText(selectedDate);
                    day.setText(selectedDay);
                },
                year,
                month,
                dayOfMonth
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    private String getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        return dayFormat.format(calendar.getTime());
    }
}
