package com.example.wagepay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerDetailsActivity extends AppCompatActivity {
    private TextView nameTextView;

    private CircleImageView imageView;
    private AlertDialog alert;
    String workerId;

    private TextView addressTextView;
    private TextView phoneTextView;
    private TextView wageTextView;
    String phoneNo;
    CardView payment_button ;
    FirebaseUser currentUser;
    FirebaseAuth firebaseAuth;
    DatabaseReference remainingWageRef;
    CardView paymentDetails, attendanceDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        //set the status bar color
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

        //for toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Worker Details");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Retrieve the user's phone number as the userId
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            phoneNo = currentUser.getPhoneNumber();
        }
        // Retrieve the data from the intent
        Intent intent = getIntent();
        String workerImage = intent.getStringExtra("workerImage");
        String workerName = intent.getStringExtra("workerName");
        String workerAddress = intent.getStringExtra("workerAddress");
        String workerPhone = intent.getStringExtra("workerNumber");
        workerId = intent.getStringExtra("workerId");
        String wageRate = intent.getStringExtra("wageRate");

        int wageInt = Integer.parseInt(wageRate);


        // Initialize your views
        imageView = findViewById(R.id.worker_image);
        nameTextView = findViewById(R.id.worker_name);
        addressTextView = findViewById(R.id.worker_address);
        phoneTextView = findViewById(R.id.worker_contact);
        wageTextView = findViewById(R.id.wageDue);
        payment_button = findViewById(R.id.payment_button);
        paymentDetails = findViewById(R.id.paymentDetails);


        payment_button.setOnClickListener(v -> payPayment());
        paymentDetails.setOnClickListener(v -> {
            Intent newIntent = new Intent(WorkerDetailsActivity.this, PaymentDetails.class);
            newIntent.putExtra("workerId",workerId);
            // Start the new activity
            startActivity(newIntent);
        });



        DatabaseReference totalWageRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .child(workerId)
                .child("TotalWage");

        remainingWageRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .child(workerId)
                .child("WageDue");




        // Calculate the present count and wage rate
        calculatePresentCount(workerId, new OnPresentCountCallback() {
            @Override
            public void onPresentCount(int count) {
                int presentCount = count;



                int totalWage = presentCount * wageInt;

                DatabaseReference totalWageRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(phoneNo)
                        .child("Workers")
                        .child(workerId)
                        .child("TotalWage");

                totalWageRef.setValue(totalWage);

                fetchTotalWage(workerId, new TotalWageCallback() {
                    @Override
                    public void onTotalWage(int totalWage) {
                        fetchTotalPayments(workerId, new TotalPaymentsCallback() {
                            @Override
                            public void onTotalPayments(int totalPayments) {
                                // Calculate the remaining wage
                                int remainingWage = totalWage - totalPayments;
                                wageTextView.setText(String.valueOf(remainingWage));
                                saveRemainingWage(remainingWage);

                            }
                        });
                    }
                });

                // Set the data in the views
                if (workerImage != null) {
                    Glide.with(WorkerDetailsActivity.this).load(workerImage).into(imageView);
                }
                nameTextView.setText(workerName);
                addressTextView.setText(workerAddress);
                phoneTextView.setText(workerPhone);


            }
        });







    }

    // Define the OnPresentCountCallback interface
    public interface OnPresentCountCallback {
        void onPresentCount(int count);
    }

    private void calculatePresentCount(String workerId, OnPresentCountCallback callback) {
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .child(workerId)
                .child("Attendance");

        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int presentCount = 0;
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String attendanceStatus = dateSnapshot.getValue(String.class);
                    if ("present".equals(attendanceStatus)) {
                        presentCount++;
                    }
                }
                callback.onPresentCount(presentCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }

   private void payPayment(){
       AlertDialog.Builder builder = new AlertDialog.Builder(WorkerDetailsActivity.this);


       LayoutInflater inflater = getLayoutInflater();
       View view = inflater.inflate(R.layout.make_payment,null);
       builder.setView(view);

       EditText enter_payment = view.findViewById(R.id.enter_payment);


       builder.setTitle("Make Payment");

       builder.setPositiveButton("Save Payment", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog,int which) {
               String paymentAmount = enter_payment.getText().toString();

               // Create a Firebase database reference to the user's specific worker and the "Payments" node
               DatabaseReference workerPaymentsRef = FirebaseDatabase.getInstance()
                       .getReference("Users")
                       .child(phoneNo)
                       .child("Workers")
                       .child(workerId)
                       .child("Payments");

               // Generate a unique key for the payment entry
               String paymentId = workerPaymentsRef.push().getKey();

               // Create a HashMap to store the payment details
               Map<String, Object> paymentData = new HashMap<>();
               paymentData.put("amount", paymentAmount);
               paymentData.put("date", getCurrentDate()); // Use server timestamp for payment date


               // Save the payment data to the Firebase database
               workerPaymentsRef.child(paymentId).setValue(paymentData);

               Toast.makeText(WorkerDetailsActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
              updateRemainingWage();
           }
       });
       builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       });


       AlertDialog alert = builder.create();
       alert.show();

   }
    private void updateRemainingWage() {
        fetchTotalWage(workerId, new TotalWageCallback() {
            @Override
            public void onTotalWage(int totalWage) {
                fetchTotalPayments(workerId, new TotalPaymentsCallback() {
                    @Override
                    public void onTotalPayments(int totalPayments) {
                        // Calculate the remaining wage
                        int remainingWage = totalWage - totalPayments;
                        wageTextView.setText(String.valueOf(remainingWage));
                        saveRemainingWage(remainingWage);
                    }
                });
            }
        });
    }

    private void saveRemainingWage(int remainingWage) {
        remainingWageRef.setValue(remainingWage);
    }

    public interface TotalPaymentsCallback {
        void onTotalPayments(int totalPayments);
    }
    private void fetchTotalPayments(String workerId, TotalPaymentsCallback callback) {
        DatabaseReference workerPaymentsRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .child(workerId)
                .child("Payments");

        workerPaymentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalPayments = 0;

                for (DataSnapshot paymentSnapshot : dataSnapshot.getChildren()) {
                    String paymentAmount = paymentSnapshot.child("amount").getValue(String.class);
                    try {
                        int amount = Integer.parseInt(paymentAmount);
                        totalPayments += amount;
                    } catch (NumberFormatException e) {
                        // Handle invalid payment amount if needed
                    }
                }

                // Pass the result to the callback
                callback.onTotalPayments(totalPayments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
    }
    public interface TotalWageCallback {
        void onTotalWage(int totalWage);
    }

    private void fetchTotalWage(String workerId, TotalWageCallback callback) {
        DatabaseReference totalWageRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(phoneNo)
                .child("Workers")
                .child(workerId)
                .child("TotalWage");

        totalWageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalWage = dataSnapshot.getValue(Integer.class);

                // Pass the result to the callback
                callback.onTotalWage(totalWage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
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

    //this is also for toolbar when back button is pressed it goes to previous activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}