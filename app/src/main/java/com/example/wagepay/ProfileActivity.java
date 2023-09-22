package com.example.wagepay;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

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

import com.github.dhaval2404.imagepicker.ImagePicker;

public class ProfileActivity extends AppCompatActivity {
    ImageView imageView;

    EditText newName, newAddress, newBusiness;
    Button saveChanges;


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

        saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prepare the data to pass
                String newNameValue = newName.getText().toString();
                String newAddressValue = newAddress.getText().toString();
                String newBusinessValue = newBusiness.getText().toString();

                // Create a Bundle to hold the data
                Bundle bundle = new Bundle();
                bundle.putString("newName", newNameValue);
                bundle.putString("newAddress", newAddressValue);
                bundle.putString("newBusiness", newBusinessValue);

                // Create an intent to start the target activity
                Intent intent = new Intent(ProfileActivity.this, OtpVarificationActivity.class);

                // Attach the Bundle to the intent
                intent.putExtras(bundle);

                // Start the new activity
                startActivity(intent);
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

