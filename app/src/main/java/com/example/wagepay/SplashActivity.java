package com.example.wagepay;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.wagepay.IntroActivity;
import com.example.wagepay.MainActivity;
import com.example.wagepay.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences introScreen;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        // Always show the splash screen
        setContentView(R.layout.activity_splash);

        //set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));
        }

        //set bottom status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.green));
        }

        // Set the status bar icons' color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //set bottom status bar icons' color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            int flags = decorView.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            decorView.setSystemUiVisibility(flags);
        }

        // Check if the user has seen the intro screens before
        introScreen = getSharedPreferences("introScreen", MODE_PRIVATE);
        boolean isFirstTime = introScreen.getBoolean("firstTime", true);

        new Handler().postDelayed(() -> {
            if (isFirstTime) {
                // User hasn't seen the intro screens, show them
                introScreen.edit().putBoolean("firstTime", false).apply();
                Intent iNext = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(iNext);
            } else {
                // User has seen the intro screens, navigate to the main activity
                Intent iNext = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(iNext);
            }
            finish();
        }, 2000); // Show the splash screen for 2 seconds
    }
}
