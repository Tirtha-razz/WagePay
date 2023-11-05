package com.example.wagepay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SumTotalPayments extends AppCompatActivity {
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    String phoneNo;
    private RecyclerView recyclerView;
    private SumTotalPaymentsAdapter adapter; // Use the modified SummerDueAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum_due_payment);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }

        // Set the status bar icons' color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // For the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Total Payments");
        setSupportActionBar(toolbar);

        // Retrieve the user's phone number as the userId
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            phoneNo = currentUser.getPhoneNumber();
        }

        recyclerView = findViewById(R.id.recyclerView);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(phoneNo)
                .child("Workers");

        Query query = databaseReference.orderByChild("WageDue").startAt(1);

        FirebaseRecyclerOptions<SumTotalPaymentsModel> options = new FirebaseRecyclerOptions.Builder<SumTotalPaymentsModel>()
                .setQuery(query, SumTotalPaymentsModel.class)
                .build();

        adapter = new SumTotalPaymentsAdapter(options);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
