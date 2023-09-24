package com.example.wagepay;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageView;

    EditText newName, newAddress, newBusiness;
    Button saveChanges;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        newName = findViewById(R.id.newName);
        newAddress= findViewById(R.id.newAddress);
        newBusiness = findViewById(R.id.newBusiness);

        //for image picker
        imageView = findViewById(R.id.profileImage);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropSquare()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        // Check if a user is logged in
        if (currentUser != null) {
            // User is logged in, retrieve their UID (user ID)
            String phoneNo = currentUser.getPhoneNumber();

            // Initialize EditText fields
            newName = findViewById(R.id.newName);
            newAddress = findViewById(R.id.newAddress);
            newBusiness = findViewById(R.id.newBusiness);

            // Retrieve and populate user data from the database using the phone number
            retrieveAndPopulateUserData(phoneNo);
        saveChanges = findViewById(R.id.saveChanges);

        // ya nera onclick listener laaune ani update garne
            saveChanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Prepare the updated data
                    String updatedName = newName.getText().toString();
                    String updatedAddress = newAddress.getText().toString();
                    String updatedBusiness = newBusiness.getText().toString();

                    // Update the user data in the database using the phone number
                    updateUserData(updatedName, updatedAddress, updatedBusiness, phoneNo);
                }
            });
        } else {
            // User is not logged in, handle this case (e.g., show a login screen)
        }
    }
    // Method to retrieve user data from the database and populate the form
    private void retrieveAndPopulateUserData(String phoneNo) {
        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users").child(phoneNo);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserHelperClass user = dataSnapshot.getValue(UserHelperClass.class);

                    // Populate the form fields with retrieved data
                    newName.setText(user.getName());
                    newAddress.setText(user.getAddress());
                    newBusiness.setText(user.getBusiness());
                } else {
                    // Handle the case where the data doesn't exist
                    // You can show a message to the user or handle it accordingly
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method to update user data in the database
    private void updateUserData(String updatedName, String updatedAddress, String updatedBusiness, String phoneNo) {
        // Prepare updated user data
        UserHelperClass updatedUser = new UserHelperClass(updatedName, updatedAddress, updatedBusiness, phoneNo);

        // Update the user data in the database
        reference.setValue(updatedUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data updated successfully
                            // You can add a toast or a success message here
                        } else {
                            // Handle the error if the update fails
                            // You can show an error message or log the error
                        }
                    }
                });
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

    // this is for image picker

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        imageView.setImageURI(uri);
    }
}

