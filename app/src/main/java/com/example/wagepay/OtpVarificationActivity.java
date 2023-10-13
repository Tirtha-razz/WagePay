package com.example.wagepay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class OtpVarificationActivity extends AppCompatActivity {

    EditText input1,input2, input3, input4, input5, input6;

    String getotpbackend;
    String phoneNo;

    String newName, newAddress, newBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_varification);

        //set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }

        //set bottom status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.white));
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

        final Button verifyOtp = findViewById(R.id.submit);

        input1 = findViewById(R.id.otp1);
        input2 = findViewById(R.id.otp2);
        input3 = findViewById(R.id.otp3);
        input4 = findViewById(R.id.otp4);
        input5 = findViewById(R.id.otp5);
        input6 = findViewById(R.id.otp6);


        TextView textView = findViewById(R.id.textNumberShow);
        String phoneNum = getIntent().getStringExtra("mobile");
        phoneNo= "+977" + phoneNum;
        textView.setText(phoneNo);

        getotpbackend = getIntent().getStringExtra("backendotp");

        final ProgressBar progressBarverifyotp = findViewById(R.id.progressbar_verify_otp);


       verifyOtp.setOnClickListener(v -> {

           if(!input1.getText().toString().trim().isEmpty() && !input2.getText().toString().trim().isEmpty() && !input3.getText().toString().trim().isEmpty() && !input4.getText().toString().trim().isEmpty() && !input5.getText().toString().trim().isEmpty() && !input6.getText().toString().trim().isEmpty()){
               String entercodeotp = input1.getText().toString()+
                       input2.getText().toString()+
                       input3.getText().toString()+
                       input4.getText().toString()+
                       input5.getText().toString()+
                       input6.getText().toString();

               if(getotpbackend!=null){
                   progressBarverifyotp.setVisibility(View.VISIBLE);
                   verifyOtp.setVisibility(View.INVISIBLE);

                   PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                           getotpbackend, entercodeotp
                   );
                   FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   progressBarverifyotp.setVisibility(View.GONE);
                                   verifyOtp.setVisibility(View.VISIBLE);

                                   if (task.isSuccessful()){
                                       //inserting data to firebase database

                                       storeNewUserData(newName, newAddress, newBusiness, phoneNo);
                                       Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       startActivity(intent);
                                   }else{
                                       Toast.makeText(OtpVarificationActivity.this, "Enter correct otp", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

               }else{
                   Toast.makeText(OtpVarificationActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
               }
               }else{
                   Toast.makeText(OtpVarificationActivity.this, "Please enter all numbers.", Toast.LENGTH_SHORT).show();
               }
           });

       numberOtpMove();

        TextView resendlabel = findViewById(R.id.resendOtp);

        resendlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                .setPhoneNumber(phoneNo)
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(OtpVarificationActivity.this)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(OtpVarificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String newbackendotp, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        getotpbackend = newbackendotp;
                                        Toast.makeText(OtpVarificationActivity.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

    }

    private void storeNewUserData(String newName, String newAddress, String newBusiness, String phoneNo) {
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Users");
// Check if the user already exists in the database
        reference.child(phoneNo).child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    UserHelperClass addNewUser = new UserHelperClass("Howdy","Howdy City"," Tea Garden ", phoneNo);
                    reference.child(phoneNo).child("Profile").setValue(addNewUser);
                } else {
                   // User already exists and move on
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle any error
            }
        });
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
