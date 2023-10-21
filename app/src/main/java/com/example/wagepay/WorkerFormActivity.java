package com.example.wagepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerFormActivity extends AppCompatActivity {
    CircleImageView wImage;
    Uri selectedImageUri; // Store the selected image URI

    String imageUrl;
    EditText wName, wAddress, wNumber, wWageRate;
    Button addWorker;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String phoneNo;
    String selectedCategoryId;

    private static final int IMAGE_PICKER_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_form);
        // Initialize SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the selectedCategoryId from SharedPreferences
        selectedCategoryId = sharedPreferences.getString("selectedCategoryId", null);

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
        toolbar.setTitle("Add New Worker");
        setSupportActionBar(toolbar);



        // Initialize Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();

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

        wName = findViewById(R.id.name);
        wAddress = findViewById(R.id.address);
        wNumber = findViewById(R.id.number);
        wWageRate = findViewById(R.id.wage_rate);
        addWorker = findViewById(R.id.add_worker);

        addWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertWorker();
                clearAll();
            }
        });

        //for image picker
        wImage = findViewById(R.id.workerProfile);

        wImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(WorkerFormActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .cropSquare()
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start(IMAGE_PICKER_REQUEST_CODE);
            }
        });

    }

    private void insertWorker(){
        if (selectedCategoryId != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("wName", wName.getText().toString());
            map.put("wAddress", wAddress.getText().toString());
            map.put("wNumber", wNumber.getText().toString());
            map.put("wWageRate", wWageRate.getText().toString());
            map.put("wImage", imageUrl);
            map.put("categoryId",selectedCategoryId ); // Add the category ID

            FirebaseDatabase.getInstance().getReference("Users").child(phoneNo).child("Workers").push()
                    .setValue(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(WorkerFormActivity.this, "Data Inserted Successfully.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(WorkerFormActivity.this, "Error While Insertion.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
    private void clearAll(){
        wName.setText("");
        wAddress.setText("");
        wNumber.setText("");
        wWageRate.setText("");
        // Set the default profile icon image resource
        wImage.setImageResource(R.drawable.profile_icon);
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
        // Handle ImagePicker result
        // Inside your onActivityResult method
        if (requestCode == IMAGE_PICKER_REQUEST_CODE) { // Use your unique request code here
            if (resultCode == RESULT_OK && data != null) {
                // Retrieve the selected image's URI
                selectedImageUri = data.getData();

                // Set the image in your ImageView (wImage in your code)
                wImage.setImageURI(selectedImageUri);

                // Upload the selected image to Firebase Storage
                if (selectedImageUri != null) {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                    StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());

                    // Upload the image to Firebase Storage
                    UploadTask uploadTask = imageRef.putFile(selectedImageUri);

                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Image upload successful
                        // You can retrieve the download URL from taskSnapshot.getDownloadUrl()
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Store the download URL in the Realtime Database
                           imageUrl = uri.toString();

                            // Now you can use 'imageUrl' as needed (e.g., store it in the database)
                        });
                    }).addOnFailureListener(exception -> {
                        // Handle the error
                    });
                }
            } else {
                // Handle the case where the user canceled the image selection
                // You can show a message or take appropriate action
            }
        }
    }
}
