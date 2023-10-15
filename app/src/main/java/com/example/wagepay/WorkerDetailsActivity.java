package com.example.wagepay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class WorkerDetailsActivity extends AppCompatActivity {
    private TextView nameTextView;

    private CircleImageView imageView;

    private TextView addressTextView;
    private TextView phoneTextView;

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

        // Retrieve the data from the intent
        Intent intent = getIntent();
        String workerImage = intent.getStringExtra("workerImage");
        String workerName = intent.getStringExtra("workerName");
        String workerAddress = intent.getStringExtra("workerAddress");
        String workerPhone = intent.getStringExtra("workerNumber");

        // Initialize your views
        imageView = findViewById(R.id.worker_image);
        nameTextView = findViewById(R.id.worker_name);
        addressTextView = findViewById(R.id.worker_address);
        phoneTextView = findViewById(R.id.worker_contact);

        // Set the data in the views
        if (workerImage != null) {
            Glide.with(this).load(workerImage).into(imageView);
        }
        nameTextView.setText(workerName);
        addressTextView.setText(workerAddress);
        phoneTextView.setText(workerPhone);

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