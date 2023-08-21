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

public class SplashActivity extends AppCompatActivity {

    SharedPreferences introScreen;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isSessionValid()) {
            // User is not logged in, show login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
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

        new Handler().postDelayed(() -> {

            introScreen = getSharedPreferences("introScreen",MODE_PRIVATE);
            boolean isFirstTime = introScreen.getBoolean("firstTime",true);

            if(isFirstTime){

                SharedPreferences.Editor editor = introScreen.edit();
                editor.putBoolean("firstTime",false);
                editor.apply();

                Intent iNext = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(iNext);
                finish();

            }else {

                Intent iNext = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(iNext);
                finish();
            }

        },3000);
    }
}