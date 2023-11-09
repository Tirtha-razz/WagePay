package com.example.wagepay;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;




public class AttendanceFragment extends Fragment {
    AttendanceRecyclerAdapter adapter;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String phoneNo;
    private ImageView calender;
    private TextView day;
    private TextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        // Initialize views
        calender = view.findViewById(R.id.calender);
        date = view.findViewById(R.id.date);
        day = view.findViewById(R.id.day);

        Button btn = view.findViewById(R.id.save_button);
        btn.setOnClickListener(v -> saveAttendance());

        // Set today's date and day of the week
        updateTodayDate();

        // Set click listener for the calendar icon
        calender.setOnClickListener(v -> openDatePicker());

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.attendance_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve the user's phone number as the userId
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            phoneNo = currentUser.getPhoneNumber();
        }

        // Create the query for Firebase Realtime Database
        Query query = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .orderByKey();

        // Create FirebaseRecyclerOptions
        FirebaseRecyclerOptions<AttendanceRecyclerModel> options =
                new FirebaseRecyclerOptions.Builder<AttendanceRecyclerModel>()
                        .setQuery(query, AttendanceRecyclerModel.class)
                        .build();

        // Initialize the adapter with FirebaseRecyclerOptions
        adapter = new AttendanceRecyclerAdapter(options, phoneNo);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void saveAttendance() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            AttendanceRecyclerModel worker = adapter.getItem(i);
            String workerId = adapter.getRef(i).getKey();
            String attendanceStatus = adapter.getAttendanceStatus(workerId);

            // Update the worker's attendance status in the Firebase Realtime Database
            DatabaseReference workerAttendanceRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(phoneNo)
                    .child("Workers")
                    .child(workerId)
                    .child("Attendance")
                    .child(getCurrentDate());


            workerAttendanceRef.setValue(attendanceStatus)
                    .addOnSuccessListener(aVoid -> {
                        // Attendance record updated successfully
                        Toast.makeText(getActivity(), "Attendance recorded.", Toast.LENGTH_SHORT).show();
                    })

                    .addOnFailureListener(e -> {
                        // Handle any errors that may occur
                        Toast.makeText(getActivity(), "Error while recording attendance.", Toast.LENGTH_SHORT).show();
                    });
        }
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
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
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
    private String getCurrentDate() {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Define the date format you want (e.g., "yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Format the current date
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
