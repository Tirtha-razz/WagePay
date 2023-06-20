package com.example.wagepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText enterNumber;
    Button getOtpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enterNumber = findViewById(R.id.mobile_number);
        getOtpBtn = findViewById(R.id.get_otp);

        getOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!enterNumber.getText().toString().trim().isEmpty()){
                    if((enterNumber.getText().toString().trim()).length() == 10){
                        Intent intent = new Intent(getApplicationContext(),OtpVarificationActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Please enter Correct mobile number.", Toast.LENGTH_SHORT).show();
                    }
                    }else{
                    Toast.makeText(LoginActivity.this, "Enter mobile number", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}