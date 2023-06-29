package com.example.wagepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OtpVarificationActivity extends AppCompatActivity {

    EditText input1,input2, input3, input4, input5, input6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);

        Button verifyOtp = findViewById(R.id.submit);

        input1 = findViewById(R.id.otp1);
        input2 = findViewById(R.id.otp2);
        input3 = findViewById(R.id.otp3);
        input4 = findViewById(R.id.otp4);
        input5 = findViewById(R.id.otp5);
        input6 = findViewById(R.id.otp6);


       verifyOtp.setOnClickListener(v -> {

           if(!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() && !input3.getText().toString().trim().isEmpty() && !input4.getText().toString().trim().isEmpty() && !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty()){
                   Toast.makeText(OtpVarificationActivity.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(OtpVarificationActivity.this, MainActivity.class);
                   startActivity(intent);
               }else{
                   Toast.makeText(OtpVarificationActivity.this, "Please enter all numbers.", Toast.LENGTH_SHORT).show();
               }
           });

       numberOtpMove();
    }

    private void numberOtpMove() {
        input1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    input2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    input3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    input4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    input5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    input6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}