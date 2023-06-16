package com.example.wagepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences introScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(() -> {

            introScreen = getSharedPreferences("introScreen",MODE_PRIVATE);
            boolean isFirstTime = introScreen.getBoolean("firstTime",true);

            if(isFirstTime){

                SharedPreferences.Editor editor = introScreen.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();

                Intent iNext = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(iNext);
                finish();

            }else {

                Intent iNext = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(iNext);
                finish();
            }

        }, 4000);
    }
}